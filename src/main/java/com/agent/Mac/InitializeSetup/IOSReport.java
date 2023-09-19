package com.agent.Mac.InitializeSetup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class IOSReport extends TestReport{

	public IOSReport(String templateFile, String appBuildNumber) {
		super(templateFile);
		
		customAttrs.putAll(commonAttrs);
		generateSummary();
		updateItem("//body/h1", "Execution Summary for " + getTodayDate());
		if(customAttrs.get("SUITE NAME").contains("IOSUpgradeSuite")){
		
			
		}
	}

	public void updateDuration(){

	}

	@Override
	public void addResults(TestResult tr) {
		counter++;
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "//table[@id=\"myTable\"]/tbody";
		System.out.println(expression);
		NodeList nodeList;
		Node tBody = null;
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			System.out.println( nodeList.getLength());
			tBody = nodeList.item(0);

		}catch(Exception e){
			e.printStackTrace();
		}
		//status=tr.getStatus().toUpperCase();
		Element tdNode = doc.createElement("tr");


		tdNode.setAttribute("Class", tr.getresultStatus().className);
		tdNode.appendChild(addSerialNumber(counter));
		tdNode.appendChild(addVideo(tr));
		tBody.appendChild(tdNode);

		makeVideo(tr);

		updateTestStatus(tr);

		// write the content into xml file
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			resultHTML = resultHTML.replace("%20", " ");
			File file = new File(this.resultHTML);
			StreamResult result = new StreamResult(new FileOutputStream(file));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	private Node addVideo(TestResult testResult) {
		String videoPath = "ftp://" + this.ftpServer + "/"+ testResult.getResultID() + "/" + testResult.getTestScriptName() + ".webm";

		Element tdNode = doc.createElement("td");

		String  videoHTML = "<video width=\"240\" height=\"240\" controls=\"\"> <source src=\"" + videoPath + "\" type=\"video/webm\"></source> Your browser does not support the video tag.</video>";
		Node video = generateNodeFromString(videoHTML);
		tdNode.appendChild(video);			
		return tdNode;
	}

	private Node addSerialNumber(int number) {

		Element tdNode = doc.createElement("td"); 
		tdNode.setTextContent(String.valueOf(number));			
		return tdNode;
	}

	private Node generateNodeFromString(String html){
		Node node =  doc.createElement("br");
		try {
			Document doc2 = docBuilder.parse(new ByteArrayInputStream(html.getBytes()));
			node = doc.importNode(doc2.getDocumentElement(), true);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return node;
	}

	@Override
	public void generateReport() {}


	
}