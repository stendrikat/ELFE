package emperior.elfe.core;

public class AllTaskResults {
	
	private String taskName;
	
	public String getTaskName() {
		return taskName;
	}
	
	public AnalysisResult getTimeTakenResult() {
		return timeTakenResult;
	}
	public AnalysisResult getTestCaseResult() {
		return testCaseResult;
	}
	public AnalysisResult getRunResult() {
		return runResult;
	}
	public AnalysisResult getFileViewResult() {
		return fileViewResult;
	}
	public AnalysisResult getSearchResult() {
		return searchResult;
	}
	
	public AllTaskResults(String taskName, AnalysisResult timeTakenResult, AnalysisResult testCaseResult,
			AnalysisResult runResult, AnalysisResult fileViewResult, AnalysisResult searchResult) {
		this.taskName = taskName;
		this.timeTakenResult = timeTakenResult;
		this.testCaseResult = testCaseResult;
		this.runResult = runResult;
		this.fileViewResult = fileViewResult;
		this.searchResult = searchResult;
	}
	
	AnalysisResult timeTakenResult;
	AnalysisResult testCaseResult;
	AnalysisResult runResult;
	AnalysisResult fileViewResult;
	AnalysisResult searchResult;
}
