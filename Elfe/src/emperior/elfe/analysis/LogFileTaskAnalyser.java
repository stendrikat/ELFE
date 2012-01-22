package emperior.elfe.analysis;

import java.util.ArrayList;
import java.util.Date;

import emperior.elfe.core.AnalysisResult;
import emperior.elfe.core.EntryType;
import emperior.elfe.core.LogFileEntry;

public class LogFileTaskAnalyser {

	private String taskName;
	private ArrayList<LogFileEntry> logData;
	int lastTestCaseEntryIndex;
	
	ArrayList<LogFileEntry> startList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> closeList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> pauseList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> resumeList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> testList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> runList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> fileList = new ArrayList<LogFileEntry>();
	ArrayList<LogFileEntry> searchList = new ArrayList<LogFileEntry>();
	
	public AnalysisResult getTimeResult() {
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

	AnalysisResult timeTakenResult;
	AnalysisResult testCaseResult;
	AnalysisResult runResult;
	AnalysisResult fileViewResult;
	AnalysisResult searchResult;
	
	public LogFileTaskAnalyser (String taskName, ArrayList<LogFileEntry> logData) {
		this.taskName = taskName;
		this.logData = logData;
		
		//set all indices first and get index of last test case entry
		lastTestCaseEntryIndex = 0;
		for (int i = 0; i < logData.size(); i++) {
			if (logData.get(i).getType() == EntryType.TestCase)
				lastTestCaseEntryIndex = i;
			
			logData.get(i).setIndexNumberInAllEntries(i);
		}
		
		for (LogFileEntry entry : logData) {
			switch (entry.getType())
			{
				case Start:
					startList.add(entry);
					break;
				case Close:
					closeList.add(entry);
					break;
				case Pause:
					pauseList.add(entry);
					break;
				case Resume:
					resumeList.add(entry);
					break;
				case TestCase:
					testList.add(entry);
					break;
				case Project:
					runList.add(entry);
					break;
				case Search:
					searchList.add(entry);
					break;
				case File:
					fileList.add(entry);
					break;
			}
		}
		
		timeTakenResult = doTimeTakenEvaluation();
		testCaseResult = doTestCaseEvaluation();
		runResult = doRunEvaluation();
		fileViewResult = doFileViewEvaluation();
		searchResult = doSearchEvaluation();
	}
	
	private AnalysisResult doSearchEvaluation() {
		int searches = 0;
		for (LogFileEntry entry : searchList) {
			if (!entry.getValue().contains("found in:"))
				searches++;
		}
		
		return new AnalysisResult(taskName, "Searches", String.valueOf(searches));
	}
	
	private AnalysisResult doFileViewEvaluation() {
		
		//TODO: Analyze per file
		int fileViews = 0;
		for (LogFileEntry entry : fileList) {
			if (entry.getValue().contains("viewing:"))
				fileViews++;
		}
		
		return new AnalysisResult(taskName, "File Views", String.valueOf(fileViews));
	}
	
	private AnalysisResult doRunEvaluation() {
		int finishedRuns = 0;
		for (LogFileEntry entry : runList) {
			if (entry.getValue().contains("finished"))
				finishedRuns++;
		}
		
		return new AnalysisResult(taskName, "Project Runs", String.valueOf(finishedRuns));
	}
	
	private AnalysisResult doTestCaseEvaluation() {
		
		int finishedRuns = 0;
		for (LogFileEntry entry : testList) {
			if (entry.getValue().contains("finished"))
				finishedRuns++;
		}
		
		return new AnalysisResult(taskName, "Test Runs", String.valueOf(finishedRuns));
	}
	
	private AnalysisResult doTimeTakenEvaluation() {
	
		//additional start/closes
		long startCloseDiffTime = 0;
		if (startList.size() > 1) {
			if (startList.size() != closeList.size()) {
				return new AnalysisResult(taskName, "TimeTaken", "Invalid Start/Close Count");
			}
			
			for (int i = 1; i < startList.size(); i++) {
				//Reminder: All Start/Close times after last test case entry should be ignored
				if (startList.get(i).getIndexNumberInAllEntries() < lastTestCaseEntryIndex) { 
					long tempDiff = startList.get(i).getTime().getTime() - closeList.get(i-1).getTime().getTime();
					startCloseDiffTime += tempDiff;
				}
			}
		}
		
		//pause/resume times
		long pauseDiffTime = 0;
		if (pauseList.size() > 0) {
			if (resumeList.size() != pauseList.size()) {
				return new AnalysisResult(taskName, "TimeTaken", "Invalid Pause/Resume Count");
			}

			for (int i = 0; i < pauseList.size(); i++) {
				//Reminder: All Pause/Resume times after last test case entry should be ignored
				if (pauseList.get(i).getIndexNumberInAllEntries() < lastTestCaseEntryIndex) { 
					long tempDiff = resumeList.get(i).getTime().getTime() - pauseList.get(i).getTime().getTime();
					pauseDiffTime += tempDiff;
				}
			}
		}
		
		Date startTime = startList.get(0).getTime();
		Date endTime = testList.get(testList.size() - 1).getTime();
		
		long timeStamp = endTime.getTime() - startTime.getTime();
		timeStamp -= startCloseDiffTime;
		timeStamp -= pauseDiffTime;
		double seconds = timeStamp / 1000;
		int roundedSeconds = (int) seconds;
		return new AnalysisResult(taskName, "TimeTaken", String.valueOf(roundedSeconds));
	}
}
