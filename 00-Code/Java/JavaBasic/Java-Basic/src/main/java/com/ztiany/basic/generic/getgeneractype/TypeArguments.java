package com.ztiany.basic.generic.getgeneractype;

import java.io.IOException;
import java.lang.reflect.Constructor;  
import java.lang.reflect.Field;  
import java.lang.reflect.GenericArrayType;  
import java.lang.reflect.Method;  
import java.lang.reflect.ParameterizedType;  
import java.lang.reflect.Type;  
import java.lang.reflect.TypeVariable;  
import java.lang.reflect.WildcardType;  
import java.util.List;  
import java.util.Map;

/**
 * 作者：http://jisonami.iteye.com/blog/2282650
 * @param <T>
 */
public class TypeArguments<T> {

    public TypeArguments(){}  
    public <E> TypeArguments(E e){}

    public Map<T, String> genericField;

    public <B> Map<Integer, String>[] genericMethod(List<? extends Integer> list, List<String> list2, String str, B[] tArr) throws IOException, NoSuchMethodException{  
        return null;  
    }  
      
    public static void main(String[] args) throws Exception {  
        Class<?> clazz = TypeArguments.class;  
          
        System.out.println("一．  成员变量类型的泛型参数");  
        Field field = clazz.getField("genericField");  
        Type fieldGenericType = field.getGenericType();  
        instanceActualTypeArguments(fieldGenericType);  
        System.out.println();  
          
        System.out.println("二．  成员方法返回值的泛型参数。");  
        Method method = clazz.getMethod("genericMethod", new Class<?>[]{List.class, List.class, String.class, Object[].class});  
        Type genericReturnType = method.getGenericReturnType();  
        instanceActualTypeArguments(genericReturnType);  
        System.out.println();  
          
        System.out.println("三．  成员方法参数类型的泛型参数。");  
        Type[] genericParameterTypes = method.getGenericParameterTypes();  
        for (int i = 0; i < genericParameterTypes.length; i++) {  
            System.out.println("该方法的第" + (i+1) + "个参数：");  
            instanceActualTypeArguments(genericParameterTypes[i]);  
        }  
        System.out.println();  
          
        System.out.println("三．  构造方法参数类型的泛型参数。");  
        Constructor<?> constructor = clazz.getConstructor(new Class<?>[]{Object.class});  
        Type[] constructorParameterTypes = constructor.getGenericParameterTypes();  
        for (int i = 0; i < constructorParameterTypes.length; i++) {  
            System.out.println("该构造方法的第" + (i+1) + "个参数：");  
            instanceActualTypeArguments(constructorParameterTypes[i]);  
        }
        System.out.println();


        System.out.println("四．  类上泛型参数。");
        TypeArguments<String> typeArguments = new TypeArguments<>();
        TypeVariable<? extends Class<? extends TypeArguments>>[] typeParameters = typeArguments.getClass().getTypeParameters();
        for (int i = 0; i < typeParameters.length; i++) {
            System.out.println(typeParameters[i]);
        }
        System.out.println();  
    }  
      
    /** 
     * 实例化泛型的实际类型参数 
     * @param type 
     * @throws Exception  
     */  
    private static void instanceActualTypeArguments(Type type) throws Exception{  
        System.out.println("该类型是"+ type);  
        // 参数化类型  
        if ( type instanceof ParameterizedType ) {  
            Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();  
            for (int i = 0; i < typeArguments.length; i++) {  
                // 类型变量  
                if(typeArguments[i] instanceof TypeVariable){  
                    System.out.println("第" + (i+1) +  "个泛型参数类型是类型变量" + typeArguments[i] + "，无法实例化。");  
                }   
                // 通配符表达式  
                else if(typeArguments[i] instanceof WildcardType){  
                    System.out.println("第" + (i+1) +  "个泛型参数类型是通配符表达式" + typeArguments[i] + "，无法实例化。");  
                }  
                // 泛型的实际类型，即实际存在的类型  
                else if(typeArguments[i] instanceof Class){  
                    System.out.println("第" + (i+1) +  "个泛型参数类型是:" + typeArguments[i] + "，可以直接实例化对象");  
                }  
            }  
        // 参数化类型数组或类型变量数组  
        } else if ( type instanceof GenericArrayType) {  
            System.out.println("该泛型类型是参数化类型数组或类型变量数组，可以获取其原始类型。");  
            Type componentType = ((GenericArrayType)type).getGenericComponentType();  
            // 类型变量  
            if(componentType instanceof TypeVariable){  
                System.out.println("该类型变量数组的原始类型是类型变量" + componentType + "，无法实例化。");  
            }   
            // 参数化类型，参数化类型数组或类型变量数组  
            // 参数化类型数组或类型变量数组也可以是多维的数组，getGenericComponentType()方法仅仅是去掉最右边的[]  
            else {  
                // 递归调用方法自身  
                instanceActualTypeArguments(componentType);  
            }  
        } else if( type instanceof TypeVariable){  
            System.out.println("该类型是类型变量");  
        }else if( type instanceof WildcardType){  
            System.out.println("该类型是通配符表达式");  
        } else if( type instanceof Class ){  
            System.out.println("该类型不是泛型类型");  
        } else {  
            throw new Exception();  
        }  
    }  
}  
