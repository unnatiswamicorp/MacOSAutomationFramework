package com.agent.Mac.InitializeSetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class MacAgentActions {

	
	public boolean executeAppleScript(String[] Applescript, String VerifyMessageDisplayed) {

		boolean isReaderValueRead = false;
		try {

		ProcessBuilder builder = new ProcessBuilder(Applescript);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		Thread.sleep(10000);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

		while (true) {
			line = reader.readLine();

			if (line == null) {
				break;
			} else if (!line.equalsIgnoreCase(VerifyMessageDisplayed)) {
				boolean displayedMessage = line.equalsIgnoreCase(VerifyMessageDisplayed);
				Assert.assertEquals(displayedMessage, false, "Failed to Verify message ");


			} else if (line.equalsIgnoreCase(VerifyMessageDisplayed)) {
				boolean displayedMessage = line.equalsIgnoreCase(VerifyMessageDisplayed);
				Assert.assertEquals(displayedMessage, true, "Verified message successfully");


				isReaderValueRead = true;

			} else {

				Assert.assertEquals("Failed to verify message", false);
			}
		}

		reader.close();
	
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
	return isReaderValueRead;

	}
 
	public static void executeCommandOnTerminal(String command) {

		try {

			System.out.println("Started Execution!");

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);

			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null)
				System.out.println(line);

			System.out.println("Waiting for process termination");
			proc.waitFor();
			System.out.println("Process terminated");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}


	
	
	
}