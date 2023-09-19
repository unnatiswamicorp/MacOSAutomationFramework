package com.agent.Mac.InitializeSetup;

import java.util.ArrayList;
import java.util.List;

public class TestResult implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TestStatus teststatus;
	private String testScriptName;
	private String status;
	private String duration;
	private long durationInSeconds;
	private List<String> passedCheckPoints;
	private List<String> failedCheckPoints;
	private List<String> passedwithWarningsTests;
	private List<String> detailedLogs;
	private String resultID;
	private String videoLocation;
	
	
	public TestResult(String scriptName){
		this.testScriptName = scriptName;
		passedCheckPoints = new ArrayList<String>();
		failedCheckPoints = new ArrayList<String>();
		passedwithWarningsTests = new ArrayList<String>();
		detailedLogs = new ArrayList<String>();
	}
	
	public TestResult()
	{
		passedCheckPoints = new ArrayList<String>();
		failedCheckPoints = new ArrayList<String>();
		passedwithWarningsTests = new ArrayList<String>();
		detailedLogs = new ArrayList<String>();
	}
	
	public String getTestScriptName() {
		return testScriptName;
	}
	
	public void setTestScriptName(String testScriptName) {
		this.testScriptName = testScriptName;
	}
		
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public void setPassedCheckPoint(String passedCheckPoint){
		this.passedCheckPoints.add(passedCheckPoint);
	}
	
	public List<String> getPassedCheckPoints() {
		return passedCheckPoints;
	}
	
	public List<String> getWarningCheckPoints() {
		return passedwithWarningsTests;
	}
	
	public void setWarningCheckPoints(String passedwithWarningsTests){
		this.passedwithWarningsTests.add(passedwithWarningsTests);
	}
	
	public void setFailedCheckPoint(String failedCheckPoint){
		this.failedCheckPoints.add(failedCheckPoint);
	}
	
	public List<String> getFailedCheckPoints() {
		return failedCheckPoints;
	}
	
	public void setDetailedLog(String message){
		this.detailedLogs.add(message);
	}
	
	public List<String> getDetailedLogs() {
		return detailedLogs;
	}

	public void setStatus(String status) {
		this.status=status;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setresultStatus(TestStatus resultstatus) {
		 this.teststatus=resultstatus;
	}
	public TestStatus getresultStatus() {
		return this.teststatus;
	}

	
	@Override
	public String toString(){
		return "scriptName:" + this.testScriptName + "; status:" + this.teststatus + "; duration:"
				+this.duration+ "; passedLogs:" + this.passedCheckPoints + "; failedLogs:" + this.failedCheckPoints + "detailedLogs:" + this.detailedLogs;
	}

	public long getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(long durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	public String getResultID() {
		return resultID;
	}

	public void setResultID(String resultID) {
		this.resultID = resultID;
	}

	/**
	 * @return the videoLocation
	 */
	public String getVideoLocation() {
		return videoLocation;
	}

	/**
	 * @param videoLocation the videoLocation to set
	 */
	public void setVideoLocation(String videoLocation) {
		this.videoLocation = videoLocation;
	}

	
	
}
