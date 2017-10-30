package com.mybank.reflection;

public class ReflectionExample {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
		ReflectionCkecker ckecker = new ReflectionCkecker();
		Castomer castomer = new Castomer();
		
		
		Object clone = ckecker.createNewObject(castomer);
		
		ckecker.showClassName(clone);
		ckecker.showClassFields(clone);
		ckecker.showClassMethods(clone);
		}

}
