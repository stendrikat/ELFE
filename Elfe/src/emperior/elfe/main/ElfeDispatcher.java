package emperior.elfe.main;

import java.io.*;
import java.util.*;

import emperior.elfe.analysis.LogFileTaskAnalyser;
import emperior.elfe.core.*;
import emperior.elfe.fileparser.ElfeFileParser;
import emperior.elfe.filewriter.ResultFileWriter;

public class ElfeDispatcher {

	String sourceDirectory;
	String targetDirectory;
	String [] taskClasses;
	String taskDirectories;
	String logFileName;
	
	public ElfeDispatcher(String sourceDirectory, String targetDirectory,
			String [] taskClasses, String taskDirectories, String logFileName) {
		this.sourceDirectory = sourceDirectory;
		this.targetDirectory = targetDirectory;
		this.taskClasses = taskClasses;
		this.taskDirectories = taskDirectories;
		this.logFileName = logFileName;
	}
	
	//TODO: Get all necessary data, dispatch to parser, evaluator, writer for each entry in classes and participants
	public void executeLogFileParsingAnalyzingSaving() throws IOException {
		
		//Step 1: Get All Participant Directories under source root
		File sourceRoot = new File(sourceDirectory);
		
		if (!sourceRoot.exists())
		{
			throw new FileNotFoundException("Could not find Directory: " + sourceRoot.getAbsolutePath());
		}
		
		File [] allChildren = sourceRoot.listFiles();
		ArrayList<File> participantDirectories = new ArrayList<File>();
		for (File file : allChildren) {
			if (file.isDirectory())
				participantDirectories.add(file);
		}
		
		executeParseAnalyseSavingOnParticipantDirectories(participantDirectories);		
	}

	private void executeParseAnalyseSavingOnParticipantDirectories(
			ArrayList<File> directories) throws IOException {
		
		ResultFileWriter fileWriter = new ResultFileWriter(targetDirectory);
		for (File directory : directories) {
			System.out.println(directory);
			//Step 2 parse all files in participant directory
			TreeMap<String, ArrayList<LogFileEntry>> parseResults = 
				getAllLogFileParsingResultsForParticipant(directory);
			
			//Step 3 analyse parseResults
			ParticipantResults analyseResults = new ParticipantResults(
					directory.getName(), getAllAnalysisResultsForParticipant(parseResults));
			
			//Step 4 write/save analyseResults and dispatch to a writer class
			fileWriter.bufferData(analyseResults);
		}
		
		//Step 5 finally write all the buffered data to destination file(s)
		fileWriter.saveResultsToFile();
	}

	private ArrayList<AllTaskResults> getAllAnalysisResultsForParticipant(
			TreeMap<String, ArrayList<LogFileEntry>> parseResults) {
		
		ArrayList<AllTaskResults> taskResultsForParcipant = new ArrayList<AllTaskResults>();
		for (String key : parseResults.keySet()) {
			LogFileTaskAnalyser analyser = new LogFileTaskAnalyser(key, parseResults.get(key));
			System.out.println(key);
			AllTaskResults taskResults = new AllTaskResults(key, analyser.getTimeResult(),
					analyser.getTestCaseResult(), analyser.getRunResult(),
					analyser.getFileViewResult(), analyser.getSearchResult());
			taskResultsForParcipant.add(taskResults);
					
		}
		
		return taskResultsForParcipant;
	}

	private TreeMap<String, ArrayList<LogFileEntry>> getAllLogFileParsingResultsForParticipant(File directory) throws IOException { 
		
		TreeMap<String, ArrayList<LogFileEntry>> tempResults = new TreeMap<String, ArrayList<LogFileEntry>>();
		
		//TODO: Still missing dynamically added directories like Tasks or logs to use
		
			
		File[] allFiles = directory.listFiles();
		ArrayList<File> taskDirectories = new ArrayList<File>();
		for (File file : allFiles) {
			if (file.isDirectory())
				taskDirectories.add(file);
		}
			
		for (File dir : taskDirectories) {
			String taskName = dir.getName();
				
			File logFile = new File(dir.getAbsolutePath() + File.separator + "logs"
					+ File.separator + logFileName);
			ArrayList<LogFileEntry> parsingResults = ElfeFileParser.parseLogFile(logFile);
				
			tempResults.put(taskName, parsingResults);
		}
			
				
		return tempResults;
	}
	
}
