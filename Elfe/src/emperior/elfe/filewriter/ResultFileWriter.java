package emperior.elfe.filewriter;

import java.io.*;
import java.util.ArrayList;

import emperior.elfe.core.*;

public class ResultFileWriter {

	private String targetDirectory;
	private ArrayList<ParticipantResults> completeResultSetsForParticipants;
	
	public ResultFileWriter(String targetDirectory) {
		this.completeResultSetsForParticipants = new ArrayList<ParticipantResults>();
		this.targetDirectory = targetDirectory;
	}
	
	public void bufferData(ParticipantResults analyseResults) {
		completeResultSetsForParticipants.add(analyseResults);		
	}
	
	public void saveResultsToFile() throws IOException {
		saveTimeTakenResultsToFile();

	}

	private void saveTimeTakenResultsToFile() throws IOException {
		
		File newTimeTakenResultsFile = new File(targetDirectory + File.separator + "timetaken.csv");
		File newTestRunsResultsFile = new File(targetDirectory + File.separator + "testruns.csv");
		File newProjectRunsResultsFile = new File(targetDirectory + File.separator + "projectruns.csv");
		File newFileViewsResultsFile = new File(targetDirectory + File.separator + "fileviews.csv");
		File newSearchesResultsFile = new File(targetDirectory + File.separator + "searches.csv");
		
		FileWriter timeTakenWriter = new FileWriter(newTimeTakenResultsFile);
		FileWriter testRunsWriter = new FileWriter(newTestRunsResultsFile);
		FileWriter projectRunsWriter = new FileWriter(newProjectRunsResultsFile);
		FileWriter fileViewsWriter = new FileWriter(newFileViewsResultsFile);
		FileWriter searchesWriter = new FileWriter(newSearchesResultsFile);
		
		for (ParticipantResults results : completeResultSetsForParticipants) {
			timeTakenWriter.write(results.getParticipantName() + ";");
			testRunsWriter.write(results.getParticipantName() + ";");
			projectRunsWriter.write(results.getParticipantName() + ";");
			fileViewsWriter.write(results.getParticipantName() + ";");
			searchesWriter.write(results.getParticipantName() + ";");
			
			for (AllTaskResults taskResults : results.getResultsForAllTasks()) {
				writeParticipantResultsToFile(timeTakenWriter, taskResults.getTimeTakenResult());
				writeParticipantResultsToFile(testRunsWriter, taskResults.getTestCaseResult());
				writeParticipantResultsToFile(projectRunsWriter, taskResults.getRunResult());
				writeParticipantResultsToFile(fileViewsWriter, taskResults.getFileViewResult());
				writeParticipantResultsToFile(searchesWriter, taskResults.getSearchResult());
			}
			
			timeTakenWriter.write("\n");
			testRunsWriter.write("\n");
			projectRunsWriter.write("\n");
			fileViewsWriter.write("\n");
			searchesWriter.write("\n");
		}
		
		timeTakenWriter.close();
		testRunsWriter.close();
		projectRunsWriter.close();
		fileViewsWriter.close();
		searchesWriter.close();
	}
	
	private void writeParticipantResultsToFile(FileWriter writer, AnalysisResult result) throws IOException {
		//writer.write(result.getTaskName() + ";");
		if (result == null)
			writer.write("Error;");
		else
			writer.write(result.getAnalysisResult() + ";");
	}
}
