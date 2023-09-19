package com.agent.Mac.InitializeSetup;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MacBasePageObject {
	protected static Logger log = Logger.getLogger(MacBasePageObject.class.getName());

	Node node;
	protected static boolean enrollment=false;
	protected static boolean internalSP=false;
	public static Node parent= null;
	public static HashMap<String, String> identifierKeyValueMap= new HashMap<String,String>();
	public static HashMap<String, String> localeStringsKeyValueMap= new HashMap<String,String>();
	protected TestInput testInput;
	protected static HashMap<String, String> testData;
	public static boolean executionMode=false;
	public MacBasePageObject() {
		//InitializeSetup.setLoggerLevel();

		if(executionMode) {
			beforeClass();
		}
	}


	protected void beforeClass() {
		try {
			GlobalVariables.currentClassName = this.getClass().getSimpleName();
			if(this.getClass().getSimpleName().contains("STO") || this.getClass().getSimpleName().contains("ITR")) {
				testInput=new TestInput(this.getClass().getSimpleName());
				testData=testInput.getTestData("testData","0");
			}

			if (System.getProperty("SuiteDisplayName").equalsIgnoreCase("endtoend")) {
				testInput=new TestInput(this.getClass().getSimpleName());
				testData=testInput.getTestData("testData","0");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//This method acts as a recurssive function which will parse each node that has values in it and put the nodename as key and nodevalue as value for a hashmap
	public static HashMap<String,String> parseXML(Node node, Node parent)
	{

		try
		{
			if (node.hasChildNodes())
			{
				//System.out.println(node.getNodeName());
				NodeList childrens = node.getChildNodes();
				for (int i = 0; i < childrens.getLength(); i++)
				{
					parseXML(childrens.item(i), node);           
				}
			}
			else {
				String nodeValue = node.getNodeValue().trim();
				if (nodeValue.length() > 0){
					identifierKeyValueMap.put(parent.getNodeName(), nodeValue);
				}
			}
		}
		catch(Exception e)
		{
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			e.printStackTrace();
		}
		return identifierKeyValueMap;
	}


	public void afterClass() {
		
		
	}
}
