package me.ztiany.javassist.runtime;

import javassist.*;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class SampleMain2 {

    @Test
    public void testGetClassInfo() throws NotFoundException {
        // 获取默认类型池对象
        ClassPool classPool = ClassPool.getDefault();

        // 获取指定的类型
        CtClass ctClass = classPool.get("java.lang.String");

        System.out.println(ctClass.getName());  // 获取类名
        System.out.println("\tpackage " + ctClass.getPackageName());    // 获取包名
        System.out.print("\t" + Modifier.toString(ctClass.getModifiers()) + " class " + ctClass.getSimpleName());   // 获取限定符和简要类名
        System.out.print(" extends " + ctClass.getSuperclass().getName());  // 获取超类

        // 获取接口
        if (ctClass.getInterfaces() != null) {
            System.out.print(" implements ");
            boolean first = true;
            for (CtClass c : ctClass.getInterfaces()) {
                if (first) {
                    first = false;
                } else {
                    System.out.print(", ");
                }
                System.out.print(c.getName());
            }
        }
        System.out.println();
    }

    @Test
    public void testModifyMethod() throws Exception {

        // 实例化类型池对象
        ClassPool classPool = ClassPool.getDefault();
        // 从类型池中读取指定类型
        CtClass ctClass = classPool.get("me.ztiany.javassist.runtime.TestLib");

        // 获取String类型参数集合
        CtClass[] paramTypes = {classPool.get(String.class.getName())};
        // 获取指定方法名称
        CtMethod method = ctClass.getDeclaredMethod("show", paramTypes);
        // 赋值方法到新方法中
        CtMethod newMethod = CtNewMethod.copy(method, ctClass, null);
        // 修改源方法名称
        String oldName = method.getName() + "$Impl";
        method.setName(oldName);

        // 修改原方法
        newMethod.setBody("{System.out.println(\"执行前\");" + oldName + "($$);System.out.println(\"执行后\");}");
        // 将新方法添加到类中
        ctClass.addMethod(newMethod);

        // 加载编译的类
        Class<?> clazz = ctClass.toClass();      // 注意，这一行会将类冻结，无法在对字节码进行编辑
        // 执行方法
        clazz.getMethod("show", String.class).invoke(clazz.newInstance(), "hello");
        ctClass.defrost();  // 解冻一个类，对应freeze方法
    }

    /**
     * 动态的创建类
     */
    @Test
    public void testNewClass() throws Exception {
        ClassPool classPool = ClassPool.getDefault();

        // 创建一个类
        CtClass ctClass = classPool.makeClass("me.ztiany.javassist.runtime.DynamicClass");

        // 为类型设置接口
        //ctClass.setInterfaces(new CtClass[] {classPool.get(Runnable.class.getName())});

        // 为类型设置字段
        CtField field = new CtField(classPool.get(String.class.getName()), "value", ctClass);
        field.setModifiers(Modifier.PRIVATE);
        // 添加getter和setter方法
        ctClass.addMethod(CtNewMethod.setter("setValue", field));
        ctClass.addMethod(CtNewMethod.getter("getValue", field));
        ctClass.addField(field);

        // 为类设置构造器
        // 无参构造器
        CtConstructor constructor = new CtConstructor(null, ctClass);
        constructor.setModifiers(Modifier.PUBLIC);
        constructor.setBody("{}");
        ctClass.addConstructor(constructor);
        // 参数构造器
        constructor = new CtConstructor(new CtClass[]{classPool.get(String.class.getName())}, ctClass);
        constructor.setModifiers(Modifier.PUBLIC);
        constructor.setBody("{this.value=$1;}");
        ctClass.addConstructor(constructor);

        // 为类设置方法
        CtMethod method = new CtMethod(CtClass.voidType, "run", null, ctClass);
        method.setModifiers(Modifier.PUBLIC);
        method.setBody("{System.out.println(\"执行结果\" + this.value);}");
        ctClass.addMethod(method);

        // 加载和执行生成的类
        Class<?> clazz = ctClass.toClass();
        Object obj = clazz.newInstance();
        clazz.getMethod("setValue", String.class).invoke(obj, "hello");
        clazz.getMethod("run").invoke(obj);

        obj = clazz.getConstructor(String.class).newInstance("OK");
        clazz.getMethod("run").invoke(obj);
    }

    @Test
    public void testChangeClass() throws NotFoundException, IOException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass pointClass1 = pool.get("me.ztiany.javassist.runtime.Point");
        CtClass pointClass2 = pool.get("me.ztiany.javassist.runtime.Point");
        pointClass1.setName("Pair");
        CtClass pointClass3 = pool.get("Pair");
        CtClass pointClass4 = pool.get("me.ztiany.javassist.runtime.Point");

        System.out.println(pointClass1 == pointClass2);
        System.out.println(pointClass1 == pointClass3);
        System.out.println(pointClass1 == pointClass4);
        //E:\code\studio\my_github\Repository\Java\Javassist-Base\compile-time\build\classes\java\main
        String path = getClass().getClassLoader().getResource("").toString();
        path = path.replace("file:/", "");
        System.out.println(path);
        System.out.println(path);
        pointClass1.writeFile(path);
    }

    @Test
    public void testToClassWithClassLoader() throws NotFoundException, CannotCompileException {
        Class pointClass = Point.class;
        ClassPool aDefault = ClassPool.getDefault();
        CtClass ctClass = aDefault.get(pointClass.getName());
        ctClass.toClass(pointClass.getClassLoader());
    }


}
