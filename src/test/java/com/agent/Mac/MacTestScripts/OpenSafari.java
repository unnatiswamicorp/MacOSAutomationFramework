package com.agent.Mac.MacTestScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.agent.Mac.InitializeSetup.GlobalVariables;
import com.agent.Mac.InitializeSetup.MacAgentActions;
import com.agent.Mac.InitializeSetup.MacBasePageObject;
import com.agent.Mac.InitializeSetup.TestInput;

public class OpenSafari extends MacBasePageObject {

	TestInput testInput;
	String randomMessage;
	String MessageTitle;
	String Message;
	MacAgentActions actions_obj;

	@BeforeClass()
	public void beforeClass() {
		super.beforeClass();
	}

	@Test(priority = 1)
	public void OpenSafariAndValidate() throws InterruptedException {
		
		System.out.println("<---Start Test : OpenSafariAndValidate-->");
		actions_obj =new MacAgentActions();
		
		// ------------------AppleScript----------------------------------

		@SuppressWarnings("unused")
		Runtime runtime = Runtime.getRuntime();
		String pathToScript = GlobalVariables.getAppleScriptPath()
				+ "/AppleScripts/Test.scpt";

		System.out.println("PathToScript-->" + pathToScript);
		String[] openSafari = { "osascript", pathToScript,""};
		Assert.assertEquals(actions_obj.executeAppleScript(openSafari, "Verified openSafari successfully"), true,
				"Failed to openSafari");

		System.out.println("<---Finish Test : OpenSafariAndValidate-->");

	}

	@AfterClass()
	public void afterClass() {
		super.afterClass();
	}
}
