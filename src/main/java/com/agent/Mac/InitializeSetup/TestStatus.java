package com.agent.Mac.InitializeSetup;

public enum TestStatus {
	
	//PASS("success"),FAIL("danger"),PASSEDWITHWARNINGS("warning"),SKIPPED("warning");
	PASS(1, "success"),FAIL(2, "danger"),PASSEDWITHWARNINGS(4, "warning"),SKIPPED(3, "warning");
	public int value;
	public String className;
	
	private TestStatus(int value, String className)
	{  
		this.value=value;
		this.className = className;
	}
	
	public static TestStatus getItem(int statusCode){
		TestStatus status = TestStatus.PASS;
		for(TestStatus ts: TestStatus.values()){
			if (ts.value == statusCode){
				return ts;
			}
		}
		
		return status;
	}
			
}

