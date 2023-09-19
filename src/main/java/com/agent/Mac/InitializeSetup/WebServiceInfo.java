package com.agent.Mac.InitializeSetup;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.agent.Mac.Webservices.EnumerationConstants.MethodType;
import com.agent.Mac.Webservices.EnumerationConstants.WebServices;

import okhttp3.MultipartBody.Part;

public class WebServiceInfo {
	private WebServices wsName;
	private String wsID;
	private String value;
	private MethodType methodType;
	public HashMap<String, String> parameters;
	public HashMap<String, String> uriParameters;
	public List<Part> fileParts;
	public HashMap<String, String> tagsToVerify;
	private String body;
	private String response;


	public WebServiceInfo(String wsID, String value){
		this.setWsID(wsID);
		this.setValue(value);
		parameters = new HashMap<String, String>();
		uriParameters = new HashMap<String, String>();
		tagsToVerify = new HashMap<String, String>();
		fileParts = new ArrayList<Part>();
	}

	public WebServiceInfo(WebServices wsName, String value, MethodType methodType, HashMap<String, String> params){
		this.setWsName(wsName);
		this.setValue(value);
		this.setMethodType(methodType);
		this.parameters = params;
	}

	public WebServiceInfo(WebServices wsName, String value, MethodType methodType, HashMap<String, String> params,String body){
		this.setWsName(wsName);
		this.setValue(value);
		this.setMethodType(methodType);
		this.parameters = params;
		this.setBody(body);
	}

	public WebServices getWsName() {
		return wsName;
	}

	public void setWsName(WebServices wsName) {
		this.wsName = wsName;
	}

	public String getWsID() {
		return wsID;
	}

	public void setWsID(String wsID) {
		this.wsID = wsID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseValue(String key) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{

		String responseValue = null;

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(new InputSource(new StringReader(this.response)));

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = "//" + key;
			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

			if(nodeList.getLength() == 0){
				AgentReporter.logError("The tag, <" + key + "> is not present in the response", 1, true);
			}else{
				responseValue = nodeList.item(0).getTextContent();
			}


		return responseValue;
	}

	public String getResponseValue(String refKey, String refValue, String expectedKey) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{

		String responseValue = "";

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(new InputSource(new StringReader(this.response)));

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = "//" + refKey;
			System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

			if(nodeList.getLength() == 0){
				AgentReporter.logError("The tag, <" + refKey + "> is not present in the response", 1, true);
			}else{
				for(int i=0;i<nodeList.getLength();i++){
					if(nodeList.item(i).getTextContent().equals(refValue)){
						Node node = nodeList.item(i);
						while(!node.getNodeName().equalsIgnoreCase(expectedKey)){
							node = node.getNextSibling();
						}
						responseValue = node.getTextContent();	
					}
				}
			}
		

		return responseValue;
	}

	public String getResponseValueUsingXpath(String xPathExpr) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{

		String responseValue = "";

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();         
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();         
			Document xmlDocument = builder.parse(new InputSource(new StringReader(this.response)));

			XPath xPath =  XPathFactory.newInstance().newXPath();

			String expression = xPathExpr;
			//System.out.println(expression);
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

			if(nodeList.getLength() == 0){
				AgentReporter.logError("The xPath, <" + xPathExpr + "> is not present in the response", 1, true);
			}else{
				responseValue = nodeList.item(0).getTextContent();
			}
		


		return responseValue;
	}


	@Override
	public String toString(){

		return "WebServiceID: " + wsID + "; WebServiceName: " + wsName + "; Value: " + value + "; MethodType: " + methodType + "; Params: " + parameters  + "; uriParams: " + uriParameters + "; TagsToVerify: " + tagsToVerify + "; Parts: " + fileParts ;
	}
}
