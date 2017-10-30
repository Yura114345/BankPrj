package com.mybank.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class ReflectionCkecker {
	
	public void showClassName(Object object) {
		
		Class clazz = object.getClass();
		System.out.println(clazz.getName());
				
		}
	
	public void showClassFields(Object object) {
		
		Class clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			System.out.println(field.getName());
			System.out.println(Modifier.toString(field.getModifiers()));
		}
	}

	public void showClassMethods(Object object) {
		
		Class clazz = object.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method : methods) {
			System.out.println(method.getName());
			Parameter[] parameters = method.getParameters();
			for (Parameter parameter : parameters) {
				System.out.println(method.getName());
				System.out.println(method.getReturnType().getName());
				System.out.println(Modifier.toString(method.getModifiers()));
				}
		}
	}
	
	public Object createNewObject(Object object) throws InstantiationException, IllegalAccessException {
		
		Class clazz = object.getClass();
		return clazz.newInstance();
		
	}
	
}
