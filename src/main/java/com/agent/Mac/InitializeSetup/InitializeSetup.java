package com.agent.Mac.InitializeSetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class InitializeSetup{

	protected static Logger log = Logger.getLogger(InitializeSetup.class.getName());
	public static Node parent=null;
	public static Node node;
	public static String baseDir=null;
	public static String baseDirCore=null;
	public static IOSReport report;
	public static HashMap<String,String> properties=new HashMap<String,String>();
	public static Properties customerData = new Properties();
	public static String SuiteName=null;
	public static String suiteFolderName=null;
	public static String className=null;
	public static String imagesFolder = "";
	public static String securePIN = "1111";
	public static HashMap<String, String> preUpdateStatus = new HashMap<String, String>();
	public static boolean jiraCredsValid;
	public static String jiraCreds;
	public static int runCount = 1;

	public static String currentSystemTime; 
	public static String oldRunTime;


	public InitializeSetup(){
//		setLoggerLevel();
	}

	public static void setClassName(String className){
		InitializeSetup.className = className;
	}

	public static String getClassName(){
		return InitializeSetup.className;
	}

	public static void setSuiteName(String suiteName)
	{
		InitializeSetup.SuiteName=suiteName;
		suiteFolderName = SuiteName.replace(".xml", "");
	}

	//It parses the XML file and return all the nodes in XML file as Key Value pairs
	public static boolean getProperties(String filePath){
		boolean isFileParsed = false;
		try{
			File inputFile = new File(filePath);
			DocumentBuilderFactory dbFactory 
			= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
		
			NodeList node1=doc.getDocumentElement().getChildNodes();
			for (int i=0;i<node1.getLength();i++){
				if(node1.item(i).getNodeType()==Node.ELEMENT_NODE){
					node=node1.item(i);
					properties=parseXML(node,parent);
				}
			}
			isFileParsed = true;;
		}
		catch(Exception e)
		{
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(),e);
			e.printStackTrace();
		}
		return isFileParsed;
	} 


	public static void getPropertiesFromTxt(String filePath)
	{
		try
		{

			BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
			String line;
			while(fileReader.ready()){
				//Read file contents
				line = fileReader.readLine();
				String[] splits = line.split("=");
				log.info(splits[0] +":" +splits[1]);
				properties.put(splits[0] , splits[1]);

			}

			fileReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		try {
			InputStream is = new FileInputStream(filePath);
			customerData.load(is);
		} catch (FileNotFoundException e) {
			log.error("The file, " + filePath + " is not found");
		}catch(IOException ioe){
			log.error("Error in reading from the properties file, " + filePath + ": " + ioe.getMessage());
		}
	}

	// This method acts as a recurssive function which will parse each node that has values in it and put the nodename as key and nodevalue as value for a hashmap
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
				if(!TextUtils.isEmpty(node.getNodeValue())) {
					String nodeValue = node.getNodeValue().trim();
					if (nodeValue.length() > 0) {
						//System.out.println(parent.getNodeName() + "::" + nodeValue);
						properties.put(parent.getNodeName(), nodeValue);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return properties;
	}

	public static void setImagesDirectory(){
		imagesFolder = baseDir+"/"+ properties.get("platform")+"/Images/"+ properties.get("imagesFolder");
	}



	
	
}