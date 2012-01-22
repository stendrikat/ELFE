package emperior.elfe.core;

public class AnalysisResult {
	public String getTaskName() {
		return taskName;
	}

	public String getAnalysisName() {
		return analysisName;
	}

	public String getAnalysisResult() {
		return analysisResult;
	}

	private String taskName;
	private String analysisName;
	private String analysisResult;
	
	public AnalysisResult(String taskName, String analysisName, String analysisResult) {
		this.taskName = taskName;
		this.analysisName = analysisName;
		this.analysisResult = analysisResult;
	}
}
