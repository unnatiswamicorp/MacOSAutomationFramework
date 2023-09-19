package com.agent.Mac.InitializeSetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MacAgentTerminalUtils {

	public static List<String> buildProperties = new ArrayList<String>();
	public static String buildVersion;
	public static String MaaSVersion;
	public static String BuildsVersionOnDevice;
	public static String BaseURL;


	public static int runScript(String command) {

		System.out.println("Executing command " + command);

		int iExitValue = 0;
		String sCommandString;
		sCommandString = command;
		CommandLine oCmdLine = CommandLine.parse(sCommandString);
		DefaultExecutor oDefaultExecutor = new DefaultExecutor();
		oDefaultExecutor.setExitValue(0);
		try {
			iExitValue = oDefaultExecutor.execute(oCmdLine);
			System.out.println("Command Executed");

		} catch (ExecuteException e) {
			System.err.println("Execution failed.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("permission denied.");

		}
		return iExitValue;
	}

	public static void executeScript(String command) {

		try {

			System.out.println("Executing command " + command);

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(new String[] { "bash", "-c", command });

			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null)
				System.out.println(line);

			proc.waitFor();

			System.out.println("\n");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void executePlainScript(String command) {

		try {

			System.out.println("Executing command " + command);

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);

			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null)
				System.out.println(line);

			proc.waitFor();

			System.out.println("Command executed");

		} catch (Exception e) {

			System.out.println("Command failed");
			e.printStackTrace();
		}

	}


	public static List<String> parseXML(Node node, Node parent) {
		try {
			if (node.hasChildNodes()) {
				// System.out.println(node.getNodeName());
				NodeList childrens = node.getChildNodes();
				for (int i = 0; i < childrens.getLength(); i++) {
					parseXML(childrens.item(i), node);
				}
			} else {
				String nodeValue = node.getNodeValue().trim();

				if (nodeValue.length() > 0) {

					if (nodeValue.contains(InitializeSetup.properties.get("buildVersion"))) {
						buildProperties.add(nodeValue);
					}

				}
			}
		} catch (Exception e) {

		}
		return buildProperties;
	}





	public static class CustomAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			String username = null;
			String password = null;

			if (UpdateMetadata_Mac.LocalRun) {
				username = InitializeSetup.properties.get("buildServerUsername");
				password = InitializeSetup.properties.get("buildServerPassword");
			} else {
				username = System.getenv("Artifactory_UserName");
				password = System.getenv("Artifactory_Password");
			}
			return new PasswordAuthentication(username, password.toCharArray());
		}
	}



}
