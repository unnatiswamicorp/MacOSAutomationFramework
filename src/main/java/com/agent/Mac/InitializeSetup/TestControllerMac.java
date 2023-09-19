package com.agent.Mac.InitializeSetup;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

public class TestControllerMac {

	public static String SuiteDisplayName;
	public static String deviceProfile = System.getProperty("deviceProfile");
	public static String platform = System.getProperty("platform");
	MacAgentActions actions;

	@BeforeSuite
	public void beforeSuite() {

		actions = new MacAgentActions();

		try {
			initializeAllConfigurations();

			InitializeSetup.baseDir = System.getProperty("user.dir");
			System.out.println("base dir : " + InitializeSetup.baseDir);
			InitializeSetup.setSuiteName(SuiteDisplayName);
			InitializeSetup.properties.put("platform", platform);

		} catch (Exception e) {
			e.printStackTrace();
			AgentReporter.logStackTrace(e);
			Assert.assertTrue(false,
					"Error in Initializing the TestInstance. Please check the Connections & Test inputs");
		}
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));

				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public void initializeAllConfigurations() {

		MacBasePageObject.executionMode = true;
		System.out.println("User Dir : " + System.getProperty("user.dir"));
		System.out.println("SuiteName : " + System.getProperty("SuiteDisplayName"));
		String platform = System.getProperty("platform");
		InitializeSetup.properties.put("platform", platform);
		deviceProfile = System.getProperty("deviceProfile");
		SuiteDisplayName = System.getProperty("SuiteDisplayName");
		InitializeSetup.setSuiteName(SuiteDisplayName);

		try {
			UpdateMetadata_Mac.main(new String[2]);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}

		try {
			InitializeSetup.getProperties(UpdateMetadata_Mac.PathToConfig_Agent);
			InitializeSetup.getProperties(UpdateMetadata_Mac.PathToDeviceProfile);
			InitializeSetup.properties.put("testNGFailedRunXml", UpdateMetadata_Mac.PathTotestNGFailedRunXml);
			InitializeSetup.properties.put("testNGFailedXmlTemplate", UpdateMetadata_Mac.PathtotestNGFailedXmlTemplate);

			String suiteFolderPath = UpdateMetadata_Mac.PathToSuiteFolder;

			File suiteDir = new File(suiteFolderPath);
			if (suiteDir.exists()) {
				System.out.println("Deleting existing suite folder.");
				if (!deleteDir(suiteDir))
					System.out.println("Failed to delete existing suite folder");
			}

			boolean dirCreated = suiteDir.mkdir();
			if (dirCreated) {
				AgentReporter.log("Created Suites directory for " + InitializeSetup.SuiteName, 2, true);
			} else {
				AgentReporter.logWarning("Error in creating Suites directory for " + InitializeSetup.SuiteName, 2,
						true);
			}

			File originalCSSFolder = new File(UpdateMetadata_Mac.PathToAgentRepo + "Temp/AgentLogs/DataTables-1.10.7");
			File moveToCSSFolder = new File(UpdateMetadata_Mac.PathToSuiteFolder + "/DataTables-1.10.7");

			try {
				org.apache.commons.io.FileUtils.copyDirectory(originalCSSFolder, moveToCSSFolder);
			} catch (Exception e) {
				System.out.println("Unable to copy CSS folder" + e);
			}

			InitializeSetup.report = new IOSReport(
					UpdateMetadata_Mac.PathToAgentRepo + "/Temp/AgentLogs/Template - iOS Sanity.html", "121");

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			InitializeSetup.currentSystemTime = dateFormat.format(date).toString();
			System.out.println("Current SystemTime : " + InitializeSetup.currentSystemTime);

			if (!InitializeSetup.SuiteName.toLowerCase().contains("staging")) {
				System.out.println(
						"Current Suite : " + UpdateMetadata_Mac.PathToSuites + InitializeSetup.SuiteName + ".xml");
				System.out.println("Current Suite Mappings File : " + UpdateMetadata_Mac.PathToSuites + "Mappings/"
						+ InitializeSetup.SuiteName + ".xml");
				try {
					new TestInput(InitializeSetup.SuiteName + ".xml", "mappings");
				} catch (Exception e) {
					System.out.println("\nMapping suite file does not exist \n");
				}
			}

			InitializeSetup.getPropertiesFromTxt(UpdateMetadata_Mac.PathToReRunDataFile);

		} catch (Exception e) {
			System.out.println("Error  : \n" + e.getMessage());
		}

	}

}
