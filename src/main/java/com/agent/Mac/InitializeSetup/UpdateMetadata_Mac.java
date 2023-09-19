package com.agent.Mac.InitializeSetup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class UpdateMetadata_Mac {

	public List<String> fileList;
	static List<String> webServiceList;
	public List<String> pageObjectfileList;

	public List<String> MacAgentPageObjectClasses;
	public static String platform = "Macagent";
	public static String PathToAgentRepo;
	public static String PathToCoreRepo;

	public static String PathToAgentData;
	public static String PathToTargetClasses;

	public static String PathToConfig_Agent;
	public static String PathToDeviceProfile;
	public static String PathToCustomerProfile;
	public static String PathToServiceFile;
	
	public static String PathToGateWayCredentialsPropertiesFile;

	public static String UpgradeXML;
	public static String InitialUpgradesScripts;
	public static String PostUpgradeValidationScripts;
	public static String UpgradeSuitesFolder;

	public static String PathToReRunSuites;

	public static String PathToReRunDataFile;
	public static String PathTotestNGFailedRunXml;
	public static String PathtotestNGFailedXmlTemplate;

	public static String PathToSuiteFolder;


	public static String PathToReports;
	public static String PathToConfigFiles;
	public static String PathToSuites;
	public static String PathToTestData;

	
	public static String pathWhereAllRepositories;

	
	public static boolean LocalRun = false;

	UpdateMetadata_Mac() {


		System.out.println("USER DIR : " + System.getProperty("user.dir"));
		pathWhereAllRepositories = System.getProperty("user.dir"); 
		
		System.out.println("Path where all Repos are present :" + pathWhereAllRepositories);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pathWhereAllRepositories = System.getProperty("user.dir");
		PathToAgentRepo = getAgentRepoPath();
	

		PathToAgentData = PathToAgentRepo + "AgentData/";
		System.out
				.println("Path to PathToAgentData : " + PathToAgentData + " - > " + new File(PathToAgentData).exists());		
		
		PathToTargetClasses = getTargetClassPath();
		System.out.println("Path to PathToTargetClasses : " + PathToTargetClasses + " - > "
				+ new File(PathToTargetClasses).exists());
		

		PathToConfig_Agent = PathToAgentData + "Mac/Suites/" + "Config_Agent.xml";
		System.out.println(
				"Path to Config_Agent : " + PathToConfig_Agent + " - > " + new File(PathToConfig_Agent).exists());

		PathToDeviceProfile = PathToAgentData + "Mac/"+"DeviceProfiles/" 
				+ System.getProperty("deviceProfile") + ".xml";
		System.out.println(
				"Path to DeviceProfile : " + PathToDeviceProfile + " - > " + new File(PathToDeviceProfile).exists());

//		PathToCustomerProfile = PathToTargetClasses + "CustomerProfiles/" + InitializeSetup.properties.get("platform")
//				+ "/" + System.getProperty("customer") + ".xml";
		
		PathToCustomerProfile = PathToAgentData + "Mac/CustomerProfiles/" + System.getProperty("customer") + ".xml";
		
		
		System.out.println("Path to CustomerProfile : " + PathToCustomerProfile + " - > "
				+ new File(PathToCustomerProfile).exists());

//		PathToServiceFile = PathToTargetClasses + "CustomerProfiles/" + InitializeSetup.properties.get("platform") + "/"
//				+ "ServicePath.xml";
		
		PathToServiceFile = PathToAgentData + "Mac/CustomerProfiles/" + "ServicePath.xml";
		
		PathToGateWayCredentialsPropertiesFile = PathToAgentData + "Mac/ConfigFiles/" + "GateWayCredentials.properties";
		
		System.out
				.println("Path to ServiceFile : " + PathToServiceFile + " - > " + new File(PathToServiceFile).exists());

		PathToSuites = PathToAgentData + "Mac/Suites/";
		
		PathToTestData = PathToAgentData + "Mac/TestData/";
	

		PathToReRunSuites = PathToSuites + "SuiteReRun/";
		System.out.println(
				"Path to PathtoReRunSuites : " + PathToReRunSuites + " - > " + new File(PathToReRunSuites).exists());

		PathToReRunDataFile = PathToReRunSuites + "RunData.txt";
		System.out.println("Path to PathToReRunDataFile : " + PathToReRunDataFile + " - > "
				+ new File(PathToReRunDataFile).exists());

		PathTotestNGFailedRunXml = PathToAgentRepo + "test-output/testng-failed.xml";
		System.out.println("Path to PathTotestNGFailedRunXml : " + PathTotestNGFailedRunXml + " - > "
				+ new File(PathTotestNGFailedRunXml).exists());

		PathtotestNGFailedXmlTemplate = PathToReRunSuites + "Template-Failed.xml";
		System.out.println("Path to PathtotestNGFailedXmlTemplate : " + PathtotestNGFailedXmlTemplate + " - > "
				+ new File(PathtotestNGFailedXmlTemplate).exists());

		PathToSuiteFolder = PathToAgentRepo + "Temp/AgentLogs/Reports/" + InitializeSetup.suiteFolderName + "/";
		System.out.println(
				"Path to PathToSuiteFolder : " + PathToSuiteFolder + " - > " + new File(PathToSuiteFolder).exists());

		UpgradeXML = PathToAgentData + "Mac/Upgrades/" + "Upgrade.xml";
		System.out.println("Path to UpgradeXML : " + UpgradeXML + " - > " + new File(UpgradeXML).exists());

		InitialUpgradesScripts = PathToAgentData + "Mac/Upgrades/" + "InitialUpgradesScripts.txt";
		System.out.println("Path to InitialUpgradesScripts : " + InitialUpgradesScripts + " - > "
				+ new File(InitialUpgradesScripts).exists());

		PostUpgradeValidationScripts = PathToAgentData + "Mac/Upgrades/" + "PostUpgradeValidationScripts.txt";
		System.out.println("Path to PostUpgradeValidationScripts : " + PostUpgradeValidationScripts + " - > "
				+ new File(PostUpgradeValidationScripts).exists());

		UpgradeSuitesFolder = PathToAgentData + "Mac/UpgradeSuitesFolder/";
		System.out.println("Path to UpgradeSuitesFolder : " + UpgradeSuitesFolder + " - > "
				+ new File(UpgradeSuitesFolder).exists());
		

		PathToReports = PathToTargetClasses + "Others/Mac/Reports/";
		PathToConfigFiles = PathToTargetClasses + "Others/Mac/ConfigFiles/";

		fileList = new ArrayList<String>();
		pageObjectfileList = new ArrayList<String>();

		
	}

	public String getTargetClassPath() {
		File file = new File(".");

		try {
			file = new File(file.getCanonicalPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String Path = "";

		ClassLoader classLoader = getClass().getClassLoader();
		try {
			file = new File(classLoader.getResource("commonUtils/XpathValidator.class").getFile());

		} catch (Exception e) {
			System.out.println("Exception : \n " + e.getStackTrace());
			System.out.println("FirstPath: " + file.getAbsolutePath());

		}

		String filePath = file.getAbsolutePath();
		if (filePath.contains(".jar")) {
			System.out.println("-----THIS IS JAR RUN ( ONLY PORTALAUTOMATION / Automation RUN MACHINE )");
			System.out.println("FirstPath: " + filePath);
			String str[] = filePath.split("file");
			Path = str[0] + "coreresources/";
			LocalRun = false;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("-----THIS IS LOCAL / DEVELOPMENT RUN");
			LocalRun = true;

			String coreToolKitResourceFolderPath = file.getAbsolutePath().split("target")[0];
			Path = coreToolKitResourceFolderPath + "src/test/resources/";
		}

		return Path;
	}

	private String getAgentRepoPath() {
		String pathToAgent;
		pathToAgent = pathWhereAllRepositories + "/";
		System.out.println("Agent Repository Path :" + pathToAgent);

		return pathToAgent;
	}



	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, ParserConfigurationException, SAXException, IOException {

		UpdateMetadata_Mac updateJS = new UpdateMetadata_Mac();
		
	}
	public void writeToJS(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> stringList = null;
		// PrintWriter writer = new
		// PrintWriter(System.getProperty("user.dir")+"/HTML/"+fileName+".js",
		// "UTF-8");
		PrintWriter writer = new PrintWriter(PathToAgentRepo + "Mac/IOS_Agent_LetsAutomate/HTML/" + fileName + ".js",
				"UTF-8");
		writer.println("var " + fileName + "Map={\n\t");

		if (fileName.equals("module")) {
			stringList = fileList;
		} else if (fileName.equals("webService")) {
			stringList = webServiceList;
		}

		for (int count = 0; count < stringList.size(); count++) {
			if (fileName.equals("module")) {
				writer.print("\"" + stringList.get(count) + "\":{\"value\":[\"testInputFileName\"]}");
			} else if (fileName.equals("webService")) {
				writer.print("\"" + stringList.get(count) + "\":{\"value\":[\"" + stringList.get(count) + "\"]}");
			}
			if (count != stringList.size() - 1) {
				writer.println(",");
			}
		}
		writer.print("\n};");
		writer.close();
	}


	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(node.getName().split("\\.")[0]);
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}


}
