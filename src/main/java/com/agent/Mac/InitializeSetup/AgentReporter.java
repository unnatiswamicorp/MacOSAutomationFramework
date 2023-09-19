package com.agent.Mac.InitializeSetup;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.testng.Reporter;



public class AgentReporter extends Reporter {

	public static void logSuccess(String message){
		log("LogSuccess:" + message);
	}

	public static void logSuccess(String message, boolean logToStandardOut){
		log("LogSuccess:" + message, logToStandardOut);
	}

	public static void logSuccess(String message, int level){
		log("LogSuccess:" + message, level);
	}

	public static void logSuccess(String message, int level, boolean logToStandardOut){
		log("LogSuccess:" + message, level, logToStandardOut);
	}

	public static void logWarning(String message){
		log("LogWarning:" + message);
		captureScreenForMac();
	}

	public static void logWarning(String message, boolean logToStandardOut){
		log("LogWarning:" + message, logToStandardOut);
		captureScreenForMac();
	}

	public static void logWarning(String message, int level){
		log("LogWarning:" + message, level);
		captureScreenForMac();
	}

	public static void logWarning(String message, int level, boolean logToStandardOut){
		log("LogWarning:" + message, level, logToStandardOut);
		captureScreenForMac();
	}

	public static void logError(String message){
		log("LogError:" + message);
		captureScreenForMac();
	}

	public static void logError(String message, boolean logToStandardOut){
		log("LogError:" + message, logToStandardOut);
		captureScreenForMac();
	}

	public static void logError(String message, String testCaseName){
		log("LogError:" + message,1);
		captureScreenForMac();
	}

	public static void logError(String message, int level){
		log("LogError:" + message, level);
		captureScreenForMac();
	}

	public static void logError(String message, int level, boolean logToStandardOut){
		log("LogError:" + message, level, logToStandardOut);
		captureScreenForMac();
	}


	public static void logStackTrace(Exception e){
		StringWriter stack = new StringWriter();
		e.printStackTrace(new PrintWriter(stack));	
		AgentReporter.log(stack.toString(), 4, true);
	}

	private static void captureScreenForMac(){
		try {
			if(System.getProperty("toolName").equalsIgnoreCase("sikuli") || System.getProperty("toolName").equalsIgnoreCase("winium") || System.getProperty("toolName").equalsIgnoreCase("winAppDriver"))
			{

			}
		} catch (Exception e) {
			AgentReporter.log("Error in taking screenshot: " + e.getMessage(), 2, true);
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));	
			AgentReporter.log(stack.toString(), 4, true);
		}		
	}


}
