package com.agent.Mac.InitializeSetup;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import okhttp3.MultipartBody.Part;

public class MacAgentTestInput {

	public static String inputMappingFile;
	public static int runCount = 0;
	public List<WebServiceInfo> webServices = new ArrayList<WebServiceInfo>();
	public static HashMap<String, HashMap<String, String>> macTestInputFiles = new HashMap<String, HashMap<String, String>>();
	public List<HashMap<String, String>> testData = new ArrayList<HashMap<String, String>>();
	public HashMap<String, String> datamap = new HashMap<String, String>();
	public WebServiceInfo wsimap;
	public HashMap<String, String> webServiceResponse = null;
	public String testInputFile;
	public String LOCALE=null;
	protected static Logger log = Logger.getLogger(MacAgentTestInput.class.getName());

	public MacAgentTestInput() {

	}

	public MacAgentTestInput(String fileName) {
		super();

		String workingDirectory = System.getProperty("user.dir");

		String platform = InitializeSetup.properties.get("platform");

		this.testInputFile = workingDirectory + "/AgentData/" + platform + "/TestData/" + fileName + ".xml";

		if (new File(this.testInputFile).isFile()) {
			initTestData();
		}
	}

	public MacAgentTestInput(String fileName, String fileType) {
		// Method Used only by iOS Agent Classes, Hence updating.
		String workingDirectory = UpdateMetadata_Mac.PathToAgentData;
		MacAgentTestInput.inputMappingFile = workingDirectory 
				+ InitializeSetup.properties.get("platform") + "/Suites/Mappings/"
				+ fileName;
		log.info("Reading mappings file " + MacAgentTestInput.inputMappingFile);

		if (new File(MacAgentTestInput.inputMappingFile).isFile()) {
			initTestMappingsData();
		}
	}

	public void addWebService(WebServiceInfo wsInfo){
		webServices.add(wsInfo);
	}

	public void addTestData(HashMap<String, String> map){
		testData.add(map);
	}

	public void initializeData(HashMap<?, ?> map)
	{

	}


	
	public boolean writeWebserviceResponseToXML(HashMap<String, String> response, WebServiceInfo wsInfo) {
		boolean status = false;

		int statusCode = Integer.parseInt(response.get("StatusCode"));
		wsInfo.setResponse(response.get("Response"));
		String responseXML = response.get("Response");

		if (statusCode == HttpStatus.SC_OK) {
			try {
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				Document xmlDocument = builder.parse(new InputSource(new StringReader(responseXML)));

				Transformer tf = TransformerFactory.newInstance().newTransformer();
				tf.setOutputProperty(OutputKeys.INDENT, "yes");
				tf.setOutputProperty(OutputKeys.METHOD, "xml");
				tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				DOMSource domSource = new DOMSource(xmlDocument);

			} catch (ParserConfigurationException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (SAXException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (IOException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}

		} else {
			AgentReporter.logWarning("Webservice Failed. Status code:" + statusCode, 1, true);
		}

		return status;
	}

	public boolean validateWebServiceResponse(HashMap<String, String> response, WebServiceInfo wsInfo){
		boolean status = false;

		int statusCode = Integer.parseInt(response.get("StatusCode"));
		wsInfo.setResponse(response.get("Response"));
		String responseXML = response.get("Response");

		if (statusCode == HttpStatus.SC_OK){
			try {
				boolean tagsStatus = true;
				for (Map.Entry<String,String> tag : wsInfo.tagsToVerify.entrySet()) {
					String expectedTag = tag.getKey();
					String expectedValue = getPlaceHolderValue(tag.getValue());
					//					System.out.println(expectedTag + " ---- " + expectedValue);

					DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
					DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
					Document xmlDocument = builder.parse(new InputSource(new StringReader(responseXML)));

					XPath xPath =  XPathFactory.newInstance().newXPath();

					String expression = "//" + expectedTag;
					//					System.out.println(expression);
					NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

					if(nodeList.getLength() == 0){
						AgentReporter.logError("The tag, <" + expectedTag + "> is not present in the response", 1, true);
						tagsStatus = false;
						break;
					}else{
						String actualResponse = nodeList.item(0).getTextContent();

						if(actualResponse.equalsIgnoreCase(expectedValue)){
							//							System.out.println("The value of tag, " + expectedTag + " in the response is as expected: " + actualResponse);
						}else{
							//							System.out.println("Incorrect Response:: Expected value for <" + expectedTag + ">: " + expectedValue +  "; Actual value for <" + expectedTag + ">: " + actualResponse);
							tagsStatus = false;
							break;
						}
					}
				}

				if(tagsStatus){
					//					System.out.println("The WebService executed successfully.");
					status = true;
				}else{
					//					System.out.println("The WebService failed");
					AgentReporter.logError(wsInfo.getWsName() +  ": WebService Call failed");
				}


			} catch (ParserConfigurationException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (SAXException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (IOException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			} catch (XPathExpressionException e) {
				log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			}

		}
		else
		{
			AgentReporter.logWarning("Webservice Failed. Status code:"+statusCode, 1, true);
		}

		return status;
	}


	public void initTestData(){
		//Parse and fetch the data		
		fetchWebservices();
		fetchTestData();
	}

	public String getPlaceHolderValue(String placeholder){
		String value = placeholder;

		String propRegEx = "\\#\\{properties.(.+)\\}";
		Pattern propPattern = Pattern.compile(propRegEx);

		String custDataRegEx = "\\#\\{custData.(.+)\\}";
		Pattern custDataPattern = Pattern.compile(custDataRegEx);

		Matcher queryMatcher = propPattern.matcher(placeholder);
		Matcher custDataMatcher = custDataPattern.matcher(placeholder);
		if(queryMatcher.find()){
			String propName = queryMatcher.group(1);
			value = InitializeSetup.properties.get(propName);
		}else if(custDataMatcher.find()){
			String propName = custDataMatcher.group(1);
			value = InitializeSetup.customerData.getProperty(propName);
		}else{
			//			System.out.println(value + " is not a placeholder");
		}

		return value;
	}

	public void fetchTestData(){		

		try {
			FileInputStream file = new FileInputStream(new File(testInputFile));        
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(file);

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = "/testcase/testData";
			//			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			//			System.out.println( nodeList.getLength());

			if (nodeList.getLength() == 0){
				//				System.out.println("There are no testData in this TestInputFile");
				return;
			}

			for (int i = 0; i < nodeList.getLength(); i++) {
				HashMap<String, String> dataSet = new HashMap<String, String>();

				//				System.out.println(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
				//				System.out.println(nodeList.item(i).getAttributes().getNamedItem("value").getNodeValue());

				String name = nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue();
				String dataValue = nodeList.item(i).getAttributes().getNamedItem("value").getNodeValue();

				dataSet.put("name", name);
				dataSet.put("value", dataValue);				

				NodeList children = nodeList.item(i).getChildNodes();   

				for(int j = 0; j< children.getLength(); j++){
					if(children.item(j).getNodeName().equals("input")){
						//						System.out.println(children.item(j).getAttributes().getNamedItem("key").getNodeValue() + " : " + children.item(j).getAttributes().getNamedItem("value").getNodeValue());
						String key = children.item(j).getAttributes().getNamedItem("key").getNodeValue();
						String value = children.item(j).getAttributes().getNamedItem("value").getNodeValue();												            			
						dataSet.put(key, getPlaceHolderValue(value));
					}            		   		            		
				}

				testData.add(dataSet);
			}
		} catch (Exception e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		}
	}

	public void fetchWebServiceTemplat(WebServiceInfo wsInfo){
		try {
			FileInputStream file = null;
//			if(InitializeSetup.isiOSAgent)
//				file = new FileInputStream(new File(UpdateMetadata_Mac.PathToConfigFiles + "WebServices.xml"));
//			else	
//				file = new FileInputStream(new File(GlobalVariables.baseDirPath+"/AgentData/Mac/Files/WebServices.xml"));

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(file);

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = "/webservices/webService[@id=\"" + wsInfo.getWsID() + "\"]";
			//			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			//			System.out.println( nodeList.getLength());

			String wsNameValue = nodeList.item(0).getAttributes().getNamedItem("name").getNodeValue();


			NodeList children = nodeList.item(0).getChildNodes();       

			for(int j = 0; j< children.getLength(); j++){
				if(children.item(j).getNodeName().equals("tagToVerify")){        			
					String key = children.item(j).getAttributes().getNamedItem("key").getNodeValue();
					String value = children.item(j).getAttributes().getNamedItem("value").getNodeValue();
					//					System.out.println(children.item(j).getAttributes().getNamedItem("key").getNodeValue() + " : " + children.item(j).getAttributes().getNamedItem("value").getNodeValue());

					wsInfo.tagsToVerify.put(key, value);;
				}            		   		            		
			}

		} catch (FileNotFoundException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (XPathExpressionException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (ParserConfigurationException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (SAXException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (IOException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		}
	}


	public static List<String> fetchWebServiceList(){
		List<String> webServiceNames=new ArrayList<String>();
		
		// Method used only by iOSAgent, hence changing the path.
		try {
			FileInputStream file = new FileInputStream(new File(UpdateMetadata_Mac.PathToConfigFiles + "WebServices.xml"));        
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(file);
			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = "/webservices/webService";
			//			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);	

			for(int count=0;count<nodeList.getLength();count++)
			{
				webServiceNames.add(nodeList.item(count).getAttributes().getNamedItem("id").getNodeValue());	
			}


		} catch (FileNotFoundException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (XPathExpressionException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (ParserConfigurationException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (SAXException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		} catch (IOException e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		}

		return webServiceNames;

	}




	public void fetchWebservices(){
		try {						
			FileInputStream file = new FileInputStream(new File(testInputFile));        
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(file);

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression =  "/testcase/webService";
			//			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			//			System.out.println( nodeList.getLength());

			if (nodeList.getLength() == 0){
				//				System.out.println("There are no webservices in this TestInputFile");
				return;
			}

			String wsID = "";
			String wsValue = "";
			HashMap<String, String> params;
			HashMap<String, String> uriParams;
			List<Part> parts;

			for (int i = 0; i < nodeList.getLength(); i++) {                           	      	

				/*wsName = nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue();
            	wsValue = nodeList.item(i).getAttributes().getNamedItem("value").getNodeValue();
            	request = MethodType.valueOf(nodeList.item(i).getAttributes().getNamedItem("requestType").getNodeValue());*/

				params = new HashMap<String, String>();
				uriParams = new HashMap<String, String>();   
				parts = new ArrayList<Part>();
				wsID = nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
				wsValue = nodeList.item(i).getAttributes().getNamedItem("value").getNodeValue();

				WebServiceInfo wsInfo = new WebServiceInfo(wsID, wsValue);
				fetchWebServiceTemplat(wsInfo);


				NodeList children = nodeList.item(i).getChildNodes();       

				for(int j = 0; j< children.getLength(); j++){
					if(children.item(j).getNodeName().equals("input")){
						//						System.out.println(children.item(j).getAttributes().getNamedItem("key").getNodeValue() + " : " + children.item(j).getAttributes().getNamedItem("value").getNodeValue());
						String key = children.item(j).getAttributes().getNamedItem("key").getNodeValue();
						String value = children.item(j).getAttributes().getNamedItem("value").getNodeValue();
						String type = children.item(j).getAttributes().getNamedItem("type").getNodeValue();


						
						if(type.equalsIgnoreCase("parameter")){
							params.put(key, getPlaceHolderValue(value));
						}else if(type.equalsIgnoreCase("filepart")){
							String partFilePath = new File(testInputFile).getParent() + "/" + getPlaceHolderValue(value);
							//String partFilePath = new File(InitializeSetup.properties.get("platform") + "/Binaries").getParent() + "/" + value;
							//							System.out.println(partFilePath);
//							FilePart fp = new FilePart  (key, new File(partFilePath), "multipart/form-data", "UTF-8");
//							parts.add(fp);
						}else if(type.equalsIgnoreCase("uriParameter")){
							uriParams.put(key, getPlaceHolderValue(value));
						}						
					}            		   		            		
				}

				wsInfo.parameters = params;
				wsInfo.fileParts = parts;
				wsInfo.uriParameters = uriParams;
				//webServices.add(new WebServiceInfo(WebServices.valueOf(wsName), wsValue, request, params));
				//				System.out.println(wsInfo.toString());
				webServices.add(wsInfo);
			}

		} catch (Exception e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
		}
	}

	public HashMap<String, String> getTestData(String name, String value){
		HashMap<String, String> data = new HashMap<String, String>();

		for(HashMap<String, String> map: testData){
			if(map.get("name").equals(name) && map.get("value").equals(value)){
				data = map;
			}
		}

		return data;
	}

	public WebServiceInfo getWSInfo(String wsID, String value){
		WebServiceInfo wsi = null;
		for (WebServiceInfo wsInfo: webServices){
			//			System.out.println(wsInfo.getWsName() + " = " + wsID + " -- " + wsInfo.getValue() + " = " + value);
			if (wsInfo.getWsID().toString().equals(wsID) && wsInfo.getValue().equals(value)){
				wsi = wsInfo;
				break;
			}
		}

		return wsi;
	}

	@Override
	public String toString(){
		return "TestInputFile: " + this.testInputFile + ";\nWebServices: " + this.webServices + ";\nTestData: " + this.testData;
	}

	/**
	 * getPatternMatches method matches the patternToMatch in response of the request 
	 * 
	 * @param patternToMatch		String patternToMatch
	 * @param response				String response of the reqeuest
	 * @param relpaceWhiteSpaces	flag used whether to replace white spaces in the response or not
	 * @return string
	 * @author mjoshi
	 * @throws Exception 
	 */
	public String getPatternMatches(String patternToMatch, String response, boolean relpaceWhiteSpaces) throws Exception {
		Pattern pattern = null;
		String patternValue = null;
		try {
			patternToMatch = "(?i)"+patternToMatch+"(?i)";
			if(relpaceWhiteSpaces) {
				response = response.replaceAll("\\s","");
			}

			pattern = Pattern.compile(patternToMatch);

			// Now create matcher object.
			Matcher matcher = pattern.matcher(response);
			while (matcher.find()) {
				patternValue = matcher.group(1);
				break;
			}
			if(null != patternValue) {
				if(patternValue.contains("</string>")) {
					patternValue = patternValue.split("</string>")[0];
				}
			} 
		} catch (Exception e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			throw new Exception("Exception in getPatternMatches() :: " + e.getMessage());
		}
		return patternValue;
	}


	public void initTestMappingsData() {
		// Parse and fetch the data
		try {
			FileInputStream file = new FileInputStream(new File(MacAgentTestInput.inputMappingFile));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);

			XPath xPath = XPathFactory.newInstance().newXPath();

			String expression = "/testcase/testdata";
			log.info(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					xmlDocument, XPathConstants.NODESET);
			log.info("Total nodes :" + nodeList.getLength());

			if (nodeList.getLength() == 0) {
				log.info("There are no testData in this TestInputFile");
				return;
			}

			for (int i = 0; i < nodeList.getLength(); i++) {
				HashMap<String, String> dataSet = new HashMap<String, String>();

				log.info(nodeList.item(i).getAttributes()
						.getNamedItem("count").getNodeValue());
				String dataValue = nodeList.item(i).getAttributes()
						.getNamedItem("count").getNodeValue();

				NodeList children = nodeList.item(i).getChildNodes();

				for (int j = 0; j < children.getLength(); j++) {
					if (children.item(j).getNodeName().equals("input")) {
						log.info(children.item(j).getAttributes()
								.getNamedItem("key").getNodeValue()
								+ " : "
								+ children.item(j).getAttributes()
								.getNamedItem("value").getNodeValue());
						String key = children.item(j).getAttributes()
								.getNamedItem("key").getNodeValue();
						String value = children.item(j).getAttributes()
								.getNamedItem("value").getNodeValue();
						dataSet.put(key, getPlaceHolderValue(value));
					}
				}

				macTestInputFiles.put(dataValue, dataSet);

			}

			log.info("Total test cases count in mapping file : "
					+ macTestInputFiles.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
