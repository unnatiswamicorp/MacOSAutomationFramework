package com.agent.Mac.InitializeSetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class TestReport {
	public String customerID;
	public String customerType;
	public String environment;
	public String deviceName;
	public String osVersion;
	public String suiteName;
	public int totalTests;
	public int passedTests;
	public int failedTests;
	public int passedwithWarningsTests;
	public String passedLogs;
	List<TestResult> testResults;
	public String testScriptName;
	public String status;
	public double duration;
	public List<String> passedCheckPoints;
	public List<String> failedCheckPoints;
	public List<String> detailedLogs;
	public String templateFile;
	public Document doc;
	public DocumentBuilderFactory docFactory;
	public DocumentBuilder docBuilder;
	public HashMap<String, String> commonAttrs;
	public HashMap<String, String> customAttrs;
	public int counter=0;
	protected long totalDurationInSeconds;
	protected String ftpServer;
	protected String resultHTML;
	protected static Logger log = Logger.getLogger(TestReport.class.getName());
	File file = null;
	public static int passCount=0;
	public static int failCount=0;
	public static int skipCount=0;
	public TestReport(String templateFile) {
		commonAttrs = new HashMap<String, String>();
		customAttrs = new HashMap<String, String>();
		totalDurationInSeconds = 0;
		commonAttrs.put("Customer ID", InitializeSetup.properties.get("customerLoginName"));
		commonAttrs.put("Customer Type", InitializeSetup.properties.get("customerType"));
		commonAttrs.put("Environment", InitializeSetup.properties.get("envName"));
		commonAttrs.put("Device Model", InitializeSetup.properties.get("model"));
		commonAttrs.put("SUITE NAME", InitializeSetup.SuiteName);
		commonAttrs.put("OS Version", InitializeSetup.properties.get("deviceOS"));
		commonAttrs.put("PASS COUNT", "0");
		commonAttrs.put("FAIL COUNT", "0");
		commonAttrs.put("PASSEDWITHWARNINGS COUNT", "0");
		commonAttrs.put("SKIPPED COUNT", "0");
		commonAttrs.put("TOTAL COUNT", "0");
		commonAttrs.put("DURATION", "0");

		this.templateFile = templateFile;
		this.templateFile = this.templateFile.replaceAll("%20", " ");
		this.ftpServer = InitializeSetup.properties.get("ftpServer");
		if (InitializeSetup.properties.get("platform").equalsIgnoreCase("ios")) {
			if (InitializeSetup.properties.containsKey("EndToEndExecution")) {
				if (InitializeSetup.properties.get("EndToEndExecution").equalsIgnoreCase("false")) {
					this.resultHTML = UpdateMetadata_Mac.PathToSuiteFolder +"testreports.html";	
				} else {
					this.resultHTML = System.getProperty("user.dir") + "Temp/AgentLogs/testreports.html";		
				}
			} else {
				this.resultHTML = System.getProperty("user.dir") + "Temp/AgentLogs/testreports.html";	
			}
		}else if (InitializeSetup.properties.get("platform").equalsIgnoreCase("Mac")) {
			if (InitializeSetup.properties.containsKey("EndToEndExecution")) {
				if (InitializeSetup.properties.get("EndToEndExecution").equalsIgnoreCase("false")) {
					this.resultHTML = UpdateMetadata_Mac.PathToSuiteFolder +"testreports.html";	
				} else {
					this.resultHTML = System.getProperty("user.dir") + "Temp/AgentLogs/testreports.html";		
				}
			} else {
				this.resultHTML = System.getProperty("user.dir") + "Temp/AgentLogs/testreports.html";	
			}
		}
		else {
			this.resultHTML = System.getProperty("user.dir") + "/Temp/AgentLogs/testreports.html";	
		}
		
		this.resultHTML = this.resultHTML.replaceAll("%20", " ");
		file = new File(this.resultHTML);
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(this.templateFile);
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
	}

	protected void makeVideo(TestResult testResult){
		String suiteName = System.getProperty("SuiteDisplayName");
		InitializeSetup.properties.put("isAgentExecutionCompleted", "true");
		String baseDir = InitializeSetup.baseDir.replaceAll("%20", " ");
		String runFolder = baseDir +"/"+ InitializeSetup.properties.get("platform") +"/Reports/" + testResult.getTestScriptName() + "/" + testResult.getResultID();
		String ftpFolder=baseDir + "/"+InitializeSetup.properties.get("platform") +"/Reports/" +testResult.getTestScriptName();
		System.out.println(ftpFolder);
		File dir = new File(runFolder);

		String targetVideoDirectory = null;
		if(suiteName.equalsIgnoreCase("endtoend")) {
			targetVideoDirectory = System.getProperty("functional.dir") + "Logs/" + System.getProperty("functionalScriptName")+".webm";
		} else {
			targetVideoDirectory = runFolder+ "/" + testResult.getTestScriptName() + ".webm";
		}

		if (dir.exists()) {
			String ffmpegBin = (System.getProperty("os.name").toLowerCase().contains("win"))?baseDir+"/jars/ffmpeg-20150610-git-913685f-win32-static/bin/ffmpeg.exe":baseDir+"jars/ffmpeg";
			renameImageFiles(runFolder);
			ffmpegBin = ffmpegBin.replace("%20", " ");
			runFolder = runFolder.replace("%20", " ");
			targetVideoDirectory = targetVideoDirectory.replace("%20", " ");
			//runCommands("\"" + ffmpegBin + "\" -y -framerate 1 -i \"" + runFolder + "/img%03d.png\"" + " -c:v libvpx -crf 10 -b:v 1M -c:a libvorbis \"" +  targetVideoDirectory+"\"");
			runCommands("\"" + ffmpegBin + "\" -y -framerate 1 -i " + runFolder + "/img%03d.png" + " -c:v libvpx -crf 10 -b:v 1M -c:a libvorbis  " +  targetVideoDirectory);
			/*try {
				FileUtils.deleteDirectory(new File(baseDir + InitializeSetup.properties.get("platform") +"/Reports/" + testResult.getTestScriptName()));
			} catch (IOException e) {
				log.error(e, e.getCause());
			}*/
		}

		if(!suiteName.equalsIgnoreCase("endtoend")) {
			uploadVideoToFTPServer(testResult.getResultID(),ftpFolder);
		}

	}

	public void uploadVideoToFTPServer(String resultId,String ftpFolder) {
		try {
			if(resultId!=null) {
				// AppActions.executeADBCommands("jars\\ftpUpload.exe "+"\""+this.ftpServer+" \""+ InitializeSetup.baseDir + "/" + InitializeSetup.properties.get("platform") +"\\Reports\\" + testResult.getTestScriptName()+" "+testResult.getResultID());
				runCommands("jars\\ftpUpload.bat \"" + this.ftpServer + "\" \""+ftpFolder+"\" \""+resultId+"\"");
			}
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
	}

	public static void runCommands(String cmd){
		try{
			log.info("Executing Command: " + cmd);
			String shell = (System.getProperty("os.name").toLowerCase().contains("win"))?"cmd":"/bin/sh";
			String options = (System.getProperty("os.name").toLowerCase().contains("win"))?"/c":"-c";

			ProcessBuilder builder = new ProcessBuilder(shell, options, cmd);	
			builder.redirectErrorStream(true);
			Process process = builder.start();

			String line = "";
			BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line = reader.readLine()) != null){				
				System.out.println(line);
				log.info(line);
			}
		}catch(Exception e){
			log.error(e, e.getCause());
		}		
	}

	public void renameImageFiles(String runFolder){
		File dir = new File(runFolder);
		int counter = 1;

		if (dir.exists()) {
			for (final File f : dir.listFiles()) {
				try {
					File newfile =new File(runFolder + "/img" + String.format("%03d", counter++) + ".png");

					if(f.renameTo(newfile)){						
						AgentReporter.log("Successfully renamed the screenshot", 3, true);
					}else{
						AgentReporter.log("Error in renaming the screenshot", 3, true);
					}
				} catch (Exception e) {
					log.error(e, e.getCause());
				}
			}
		}
	}

	protected void updateTestStatus(TestResult testResult){
		updateCount("TOTAL COUNT");
		if (testResult.getresultStatus() == TestStatus.PASS){
			updateCount("PASS COUNT");
			passCount++;
		}else if (testResult.getresultStatus() == TestStatus.FAIL){
			updateCount("FAIL COUNT");
			failCount++;
		}else if (testResult.getresultStatus() == TestStatus.PASSEDWITHWARNINGS){
			updateCount("PASSEDWITHWARNINGS COUNT");
		}else if (testResult.getresultStatus() == TestStatus.SKIPPED){
			updateCount("SKIPPED COUNT");
			skipCount++;
		}

		totalDurationInSeconds += testResult.getDurationInSeconds();
		String totalTime=String.valueOf(totalDurationInSeconds/60)+":"+String.valueOf(totalDurationInSeconds%60);
		updateSummaryItem("DURATION", totalTime);
		customAttrs.put("DURATION", totalTime);		
	}

	private void updateCount(String category){
		int currentCount = Integer.parseInt(customAttrs.get(category));
		updateSummaryItem(category, String.valueOf(currentCount + 1));
		customAttrs.put(category, String.valueOf(currentCount + 1));
	}

	public abstract void generateReport();
	public abstract void addResults(TestResult tr);

	public void generateSummary() {
		Iterator<Entry<String,String>> it = customAttrs.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pair = (Entry<String, String>)it.next();
			addSummaryItem(pair.getKey(), pair.getValue());
		}
	}

	public void addSummaryItem(String key, String value){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "//table[@id=\"Summary\"]/tbody/tr/th";
		NodeList nodeList;
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {

				if(nodeList.item(i).getTextContent().equalsIgnoreCase(key)){
					Element tdNode = doc.createElement("td"); 
					tdNode.setTextContent(value);					

					nodeList.item(i).getParentNode().insertBefore(tdNode, nodeList.item(i).getNextSibling());

					if (nodeList.item(i).getNextSibling().getNodeName().equals("td")){
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			/*docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(templateFile);*/
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(file));
			result.setSystemId(file);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e.getCause());
		}
	}

	public void updateSummaryItem(String key, String value){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "//table[@id=\"Summary\"]/tbody/tr/th";
		NodeList nodeList;
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				if(nodeList.item(i).getTextContent().equalsIgnoreCase(key)){
					Node node = nodeList.item(i).getNextSibling();
					try {
						while(!node.getNodeName().equalsIgnoreCase("td")){						
							node = node.getNextSibling();
						}					
						node.setTextContent(value);
					} catch(Exception e) {
						continue;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(file));
			transformer.transform(source, result);

		} catch (Exception e) {
			log.error(e, e.getCause());
		}
	}

	public void updateItem(String xPathExpression, String value){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList nodeList;
		try {
			nodeList = (NodeList) xPath.compile(xPathExpression).evaluate(doc, XPathConstants.NODESET);
			if(nodeList.getLength() == 0){
				AgentReporter.logError("The xPath, " + xPathExpression + " is not found");
			}else{
				nodeList.item(0).setTextContent(value);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(file));
			transformer.transform(source, result);
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
	}

	public static String getTodayDate(){
		Date date = new Date();
		String today = new SimpleDateFormat("E").format(date) + ", " + new SimpleDateFormat("MMM dd").format(date) + ", " + new SimpleDateFormat("Y").format(date);
		System.out.println(today);

		return today;
	}
}