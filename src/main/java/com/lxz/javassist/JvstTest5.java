package com.lxz.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;

public class JvstTest5 extends JvstTestRoot {
    public JvstTest5(String name) {
        super(name);
    }

    public void testDollarClassInStaticMethod() throws Exception {
        CtClass cc = sloader.makeClass("test5.DollarClass");
        CtMethod m = CtNewMethod.make("public static int run(){ return $class.getName().length(); }", cc);
        cc.addMethod(m);
        m = CtNewMethod.make("public int run2(){ return $class.getName().length(); }", cc);
        cc.addMethod(m);
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(cc.getName().length(), invoke(obj, "run"));
        assertEquals(cc.getName().length(), invoke(obj, "run2"));
    }

    public void testSuperDefaultMethodCall() throws Exception {
        CtClass cc = sloader.get("test5.DefaultMethod");
        CtMethod m = CtNewMethod.make("public int run(){ return test5.DefaultMethodIntf.super.foo(); }", cc);
        cc.addMethod(m);
        m = CtNewMethod.make("public int run2(){ return test5.DefaultMethodIntf.baz(); }", cc);
        cc.addMethod(m);
        m = CtNewMethod.make("public int run3(){ return test5.DefaultMethodIntf.super.baz(); }", cc);
        cc.addMethod(m);
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(1, invoke(obj, "run"));
        assertEquals(10, invoke(obj, "run2"));
        assertEquals(10, invoke(obj, "run3"));
    }

    public void testTypeAnno() throws Exception {
        CtClass cc = sloader.get("test5.TypeAnno");
        cc.getClassFile().compact();
        cc.writeFile();
        Object obj = make(cc.getName());
        TypeVariable<?> t = obj.getClass().getTypeParameters()[0];
        Annotation[] annos = t.getAnnotations();
        assertEquals("@test5.TypeAnnoA()", annos[0].toString());
    }

    public void testJIRA241() throws Exception {
        CtClass cc = sloader.get("test5.JIRA241");
        CtMethod testMethod = cc.getDeclaredMethod("test");
        testMethod.insertAfter("System.out.println(\"inserted!\");");
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(10, invoke(obj, "run"));
    }

    public void testJIRA242() throws Exception {
        Boolean ss = new Boolean(2 > 3);
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("test5.JIRA242$Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Say Hello...\"); }");

        StringBuilder sb = new StringBuilder();
        sb.append("BOOL_SERIES = createBooleanSeriesStep();");
        //Below code cause the issue
        sb.append("BOOL_SERIES.setValue(3>=3);"); //lets comment this and run it will work 
        // Below code snippets will work
        // this cast into exact class and call the same function
        sb.append("((test5.JIRA242$BooleanDataSeries)BOOL_SERIES).setValue(3>=3);");
        // this code snippet will set exact boolean variable to the function.
        sb.append("boolean var = 3>=3;");
        sb.append("BOOL_SERIES.setValue(var);");

        m.insertBefore(sb.toString());
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(0, invoke(obj, "say"));
    }

    public void testJIRA249() throws Exception {
        CtClass cc = sloader.get("test5.BoolTest");
        CtMethod testMethod = cc.getDeclaredMethod("test");
        testMethod.insertBefore("i = foo(true & true);");
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(1, invoke(obj, "run"));
    }

    public void testJIRA248() throws Exception {
        CtClass cc = sloader.get("test5.JIRA248");
        String methodBody = "public int run() { return foo() + super.foo() + super.bar() + test5.JIRA248Intf2.super.baz(); }";
        CtMethod ctMethod = CtMethod.make(methodBody, cc);
        cc.addMethod(ctMethod);
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(40271, invoke(obj, "run"));
    }

    public void testInvalidCastWithDollar() throws Exception {
        String code = "{ new test5.JavassistInvalidCastTest().inspectReturn((Object) ($w) $_); } ";
        CtClass c = sloader.get("test5.InvalidCastDollar");
        for (CtMethod method : c.getDeclaredMethods())
            method.insertAfter(code);
    }

    public void testJIRA256() throws Exception {
        // CtClass ec = sloader.get("test5.Entity");

        CtClass cc = sloader.makeClass("test5.JIRA256");
        ClassFile ccFile = cc.getClassFile();
        ConstPool constpool = ccFile.getConstPool();
         
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        javassist.bytecode.annotation.Annotation entityAnno
            = new javassist.bytecode.annotation.Annotation("test5.Entity", constpool);
            // = new javassist.bytecode.annotation.Annotation(constpool, ec);

        entityAnno.addMemberValue("value", new javassist.bytecode.annotation.ArrayMemberValue(constpool));
        attr.addAnnotation(entityAnno);
        ccFile.addAttribute(attr);

        cc.writeFile();
        Object o = make(cc.getName());
        assertTrue(o.getClass().getName().equals("test5.JIRA256"));

        java.lang.annotation.Annotation[] annotations = o.getClass().getDeclaredAnnotations();
        assertEquals(1, annotations.length); 
    }

    public void testJIRA250() throws Exception {
        CtClass cc = sloader.makeClass("test5.JIRA250", sloader.get("test5.JIRA250Super"));
        cc.addMethod(CtNewMethod.make(
                "    public test5.JIRA250Bar getBar() {" + 
                "        return super.getBar();\n" +
                "    }\n", cc));
        cc.addMethod(CtNewMethod.make("public int run() { getBar(); return 1; }", cc));
        cc.writeFile();
        Object obj = make(cc.getName());
        assertEquals(1, invoke(obj, "run"));
    }


    public void testBadClass() throws Exception {
        CtClass badClass = ClassPool.getDefault().makeClass("badClass");
        String src = String.join(System.getProperty("line.separator"),
                "public void eval () {",
                "    if (true) {",
                "        double t=0;",
                "    } else {",
                "        double t=0;",
                "    }",
                "    for (int i=0; i < 2; i++) {",
                "        int a=0;",
                "        int b=0;",
                "        int c=0;",
                "        int d=0;",
                "        if (true) {",
                "            int e = 0;",
                "        }",
                "    }",
                "}");
        System.out.println(src);
        badClass.addMethod(CtMethod.make(src, badClass));
        Class clazzz = badClass.toClass();
        Object obj = clazzz.newInstance(); // <-- falls here
    }
}