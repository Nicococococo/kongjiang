package com.lagou.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.lagou.annotations.Select;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.pojo.SqlCommandType;

public class MapperAnnotationBuilder {
	
	private Configuration configuration;
	
	private String packagePath;
	
	public MapperAnnotationBuilder(Configuration configuration,String packagePath){
		this.configuration = configuration;
		this.packagePath = packagePath;
	}
	
	public void parse() {
		Reflections reflections = new Reflections(packagePath, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodParameterNamesScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(Select.class);
		for(Method method : methods){  
			Select annotation = method.getAnnotation(Select.class);  
            if(annotation == null)  
                continue;  
            Method[] me = annotation.annotationType().getDeclaredMethods();  
            for(Method meth : me){  
                try {  
                    String sql = (String) meth.invoke(annotation,null); 
                    
                    MappedStatement mappedStatement = new MappedStatement();
                    mappedStatement.setId(method.getName());
                    mappedStatement.setResultType(getResultType(method));
                    if(method.getParameterTypes().length!=0){
                    	mappedStatement.setParamterType(method.getParameterTypes()[0].getName());
                    }
                    mappedStatement.setSql(sql);
                    mappedStatement.setSqlCommandType(SqlCommandType.SELECT);
                    String key = method.getDeclaringClass().getName()+"."+method.getName();
                    configuration.getMappedStatementMap().put(key,mappedStatement);
                    
                } catch (IllegalAccessException e) {  
                    e.printStackTrace();  
                } catch (IllegalArgumentException e) {  
                    e.printStackTrace();  
                } catch (InvocationTargetException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
	}
	
	public String getResultType(Method method){
		
		return method.getReturnType().getName();
	}
	

}
