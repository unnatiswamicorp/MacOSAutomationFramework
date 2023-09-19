package com.agent.Mac.InitializeSetup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.testng.ITestNGService;
import com.epam.reportportal.testng.TestNGService;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class TestListenerMac extends TestListenerAdapter implements ITestListener, IInvokedMethodListener {
	public TestResult tR = null;
	public TestListenerAdapter testlistenerAdapter = new TestListenerAdapter();
	public ArrayList<String> aL;
	public TestStatus resultstatus = null;
	public TestStatus configurationStatus = null;
	public long totalDuration;
	public static long testResultID = 0;
	public static int totalStories = 0;
	public static String testResultFolder = "";
	public static LinkedHashSet<String> PassedScripts = new LinkedHashSet<String>();
	public static LinkedHashSet<String> failureScripts = new LinkedHashSet<String>();
	protected static Logger log = Logger.getLogger(TestListenerMac.class.getName());
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	DateFormat jiraDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	Date date = new Date();
	String oldReportPath;
	String newReportPath;
	String ReportZipFile;
	String ReportURL;
	static double PassPercnetageThreshold = 70;
	static double PassPercentage = 0;
	static boolean dontsendreport = false;

	String Channel_Needs_Attention = "iosautomationreports";
	String DevQAChannel = "fl-iosagentautomation";
	private ITestNGService testNGService;

	public static LinkedHashSet<String> PassedScriptNames = new LinkedHashSet<String>();
	public static LinkedHashSet<String> failureScriptNames = new LinkedHashSet<String>();

	private static final AtomicInteger INSTANCES = new AtomicInteger(0);

	public static final Supplier<ITestNGService> SERVICE = Suppliers.memoize(new Supplier<ITestNGService>() {
		public ITestNGService get() {
			return new TestNGService();
		}
	});

	public TestListenerMac() {
		this(SERVICE.get());
	}

	public TestListenerMac(ITestNGService testNgService) {
		this.testNGService = testNgService;
		if (INSTANCES.incrementAndGet() > 1) {
			final String warning = "WARNING! More than one ReportPortal listener is added";
		}
	}

	public void onExecutionStart() {
		testNGService.startLaunch();
		GlobalVariables.isTestNGExecution = true;
	}

	public void onExecutionFinish() {
		testNGService.finishLaunch();
	}

	public void onStart(ISuite suite) {
		testNGService.startTestSuite(suite);
	}

	public void onFinish(ISuite suite) {
		testNGService.finishTestSuite(suite);
		// MasterPage.killBrowserInstances();
	}

	@Override
	public void onStart(ITestContext testContext) {
		testNGService.startTest(testContext);
	}

	@Override
	public void beforeConfiguration(ITestResult testResult) {
		testNGService.startConfiguration(testResult);
	}

	@Override
	public void onConfigurationSuccess(ITestResult testResult) {
		testNGService.finishTestMethod(ItemStatus.PASSED, testResult);
	}

	@Override
	public void onConfigurationSkip(ITestResult testResult) {
		testNGService.startConfiguration(testResult);
		testNGService.finishTestMethod(ItemStatus.SKIPPED, testResult);
	}

	@Override
	public void onFinish(ITestContext arg0) {

		testNGService.finishTest(arg0);

	}

	@Override
	public void onTestFailure(ITestResult arg0) {

		testNGService.sendReportPortalMsg(arg0);
		testNGService.finishTestMethod(ItemStatus.FAILED, arg0);

	}

	@Override
	public void onTestSkipped(ITestResult arg0) {

		testNGService.finishTestMethod(ItemStatus.SKIPPED, arg0);

	}

	@Override
	public void onTestStart(ITestResult arg0) {
		testNGService.startTestMethod(arg0);
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {

		testNGService.finishTestMethod(ItemStatus.PASSED, arg0);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

}