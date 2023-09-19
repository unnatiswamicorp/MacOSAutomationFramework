package com.agent.Mac.InitializeSetup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class GlobalVariables {
	static Logger log = Logger.getLogger(GlobalVariables.class.getName());

	private static String testInputFilesFolderPath = null;
	private static String libFolderPath = null;
	public static String endUserEmailId = null;
	public static String locale = null;
	public static String baseDirPath = null;
	public static String systemUserName = null;
	public static String javaVersion = null;
	public static String userHomeFolderPath = null;
	public static String userWorkingFolderPath = null;
	public static String operatingSystem = null;
	public static String driverFolderPath=null;
	public static String currentClassName = null;
	public static String setAppleScriptPath = null;

		public static boolean isTestNGExecution = false;
	public static HashMap<String, Integer> testStatusList = new HashMap<String, Integer>();

	public static HashMap<String, Integer> testCasesInstanceList = new HashMap<String, Integer>();
	public static boolean isGridSetupCompleted = false;
	public static boolean useCustomerJsonFile = false;
	
	public static boolean retryFailedTestCases = false;
	
	static {
		operatingSystem = System.getProperty("os.name").toLowerCase();
		System.out.println("Operating System: " + operatingSystem);
		userHomeFolderPath = System.getProperty("user.home");
		System.out.println("User Home: " + userHomeFolderPath);
		userWorkingFolderPath = System.getProperty("user.dir");
		System.out.println("User Working: " + userWorkingFolderPath);
		javaVersion = System.getProperty("java.version");
		System.out.println("Java Version: " + javaVersion);
		systemUserName = System.getProperty("user.name");
		System.out.println("System User Name: " + systemUserName);

		File file = new File(".");
		try {
			baseDirPath = file.getCanonicalPath();
			file = new File(baseDirPath);
			baseDirPath = baseDirPath.replace("\\", "/");
			baseDirPath = new String(baseDirPath.replaceAll("%20", " "));
			log.info("FilePath" + baseDirPath);
			if(!baseDirPath.substring(baseDirPath.length()-1, baseDirPath.length()).equalsIgnoreCase("/")) {
				baseDirPath = baseDirPath + "/";
			}
			System.out.println("BaseDirPath: " + baseDirPath);
			setAppleScriptPath(baseDirPath+"src/main/java/com/agent/Mac");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getAppleScriptPath() {
		return setAppleScriptPath;
	}

	public static void setAppleScriptPath(String setAppleScriptPath) {
		GlobalVariables.setAppleScriptPath = setAppleScriptPath;
	}

	public static String getDriverFolderPath() {
		return driverFolderPath;
	}

	public static void setDriverFolderPath(String driverFolderPath) {
		if(operatingSystem.contains("mac") || operatingSystem.contains("linux")) {
			driverFolderPath = driverFolderPath.replace("\\", "/");
		}
		GlobalVariables.driverFolderPath = driverFolderPath;
	}

	public static String getLibFolderPath() {
		return libFolderPath;
	}

	public static void setLibFolderPath(String libFolderPath) {
		if(operatingSystem.contains("mac") || operatingSystem.contains("linux")) {
			libFolderPath = libFolderPath.replace("\\", "/");
		}
		GlobalVariables.libFolderPath = libFolderPath;
	}

	public static String getTestInputFilesFolderPath() {
		return testInputFilesFolderPath;
	}

	public static void setTestInputFilesFolderPath(String testInputFilesFolderPath) {
		if(operatingSystem.contains("mac") || operatingSystem.contains("linux")) {
			testInputFilesFolderPath = testInputFilesFolderPath.replace("\\", "/");
		}
		GlobalVariables.testInputFilesFolderPath = testInputFilesFolderPath;
	}
}
