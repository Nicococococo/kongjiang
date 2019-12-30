package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.pojo.SqlCommandType;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration =configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

        List<Element> list = rootElement.selectNodes("//select|//insert|//update|//delete");
        for (Element element : list) {
        	String name = element.getName();
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setSql(sqlText);
            switch(name){
				case "insert": {
					mappedStatement.setSqlCommandType(SqlCommandType.INSERT);
					break;
				}
				case "update": {
					mappedStatement.setSqlCommandType(SqlCommandType.UPDATE);
					break;
				}
				case "delete": {
					mappedStatement.setSqlCommandType(SqlCommandType.DELETE);
					break;
				}
				case "select": {
					mappedStatement.setSqlCommandType(SqlCommandType.SELECT);
					break;
				}
			}
            
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }
        
        System.out.println(1111);
        
    }

}
