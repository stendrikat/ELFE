package emperior.elfe.fileparser;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import emperior.elfe.core.EntryType;
import emperior.elfe.core.LogFileEntry;

public class ElfeFileParser {
	public static ArrayList<LogFileEntry> parseLogFile(File logFile) throws IOException {
		
		if (!logFile.exists())
			throw new FileNotFoundException("Could not find File: " + logFile.getAbsolutePath());
		
		String completeLogFileString = readTextFile(logFile);
		
		String [] allLogFileLines = completeLogFileString.split("\n");
		ArrayList<LogFileEntry> allLogEntries = new ArrayList<LogFileEntry>(allLogFileLines.length);
		
		for (String line : allLogFileLines) {
			allLogEntries.add(parseLineAndCreateEntry(line));
		}
		
		tryToCorrectLogFileDefects(allLogEntries);
		
		return allLogEntries;
	}

	private static void tryToCorrectLogFileDefects(ArrayList<LogFileEntry> allLogEntries) {

		for (int i = 0; i < allLogEntries.size(); i++) {
			LogFileEntry entry = allLogEntries.get(i);
			
			//TODO: Check if error handling routines do the correct thing for start/start and pause/close
			
			if (entry.getType() != EntryType.Start && entry.getType() != EntryType.Close && i > 0) {
				LogFileEntry nextEntry = allLogEntries.get(i + 1);
				
				if (nextEntry.getType() == EntryType.Start) {
					//Try error handling of single start tag due to possible crash of emperior during runtime
					//Example:
					//10.08.2011 13:35:09.715 [File] viewing: application/src/mail/content/MailElement.java
					//10.08.2011 13:36:43.631 [Start] Emperior
					//10.08.2011 13:36:49.396 [File] opening: application/src/task/Task.java
					//should be transformed to:
					//10.08.2011 13:35:09.715 [File] viewing: application/src/mail/content/MailElement.java
					//10.08.2011 13:35:09.715 [Close] Emperior
					//10.08.2011 13:36:43.631 [Start] Emperior
					//10.08.2011 13:36:49.396 [File] opening: application/src/task/Task.java
					LogFileEntry newEntry = new LogFileEntry(EntryType.Close, entry.getDate(), 
							entry.getTime(), nextEntry.getValue());
					allLogEntries.add(i + 1, newEntry);
					
				}
			}
			
			if (entry.getType() == EntryType.StartTask)
			{
				LogFileEntry nextEntry = allLogEntries.get(i + 1);
				if (nextEntry.getType() == EntryType.StartTask) {
					
					//Try Error handling of two sequential start tags which may appear if emperior
					//crashed during startup. Example:
					//05.08.2011 12:58:34.235 [Start] Emperior
					//05.08.2011 12:58:41.724 [Start] Emperior
					//should get transformed to:
					//05.08.2011 12:58:41.724 [Start] Emperior
					allLogEntries.remove(i);
				}
			}
			
			if (entry.getType() == EntryType.CloseTask && i < allLogEntries.size() - 1)
			{
				LogFileEntry nextEntry = allLogEntries.get(i + 1);
				if (nextEntry.getType() == EntryType.CloseTask) {
					
					//Try Error handling of two sequential close tags which may can appear because of
					//uncertain circumustances. Possibly multiple clicks on the close button. Example:
					//26.08.2011 13:11:52.314 [Close] Emperior
					//26.08.2011 13:11:55.529 [Close] Emperior
					//should get transformed to:
					//26.08.2011 13:11:52.314 [Close] Emperior
					allLogEntries.remove(i + 1);
				}
			}
			
			if (entry.getType() == EntryType.Pause)
			{
				LogFileEntry nextEntry = allLogEntries.get(i + 1);
				if (nextEntry.getType() == EntryType.CloseTask) {
					
					//Try error handling of emperior getting closed after a pause which leads
					//to wrong analysis.
					//The single pause tag is removed and the time of the close event reset to
					//the pause time. Example:
					//03.08.2011 16:15:07.142 [Pause] Emperior
					//03.08.2011 16:15:32.188 [Close] Emperior
					//03.08.2011 16:16:26.179 [Start] Emperior
					//should get transformed to:
					//03.08.2011 16:15:07.142 [Close] Emperior
					//03.08.2011 16:16:26.179 [Start] Emperior
					LogFileEntry newEntry = new LogFileEntry(EntryType.CloseTask, entry.getDate(),
							entry.getTime(), nextEntry.getValue()); 
					allLogEntries.set(i, newEntry);
					allLogEntries.remove(i + 1);
				}
			}
			
		}
		

		
	}

	private static String readTextFile(File file) throws IOException {
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader reader = new BufferedReader(new FileReader(file));
				
		String line = null;
		while( (line = reader.readLine()) != null){
			sb.append(line + "\n");	
		}

		reader.close();

		return sb.toString();
	}
	
	private static LogFileEntry parseLineAndCreateEntry(String line) {
		
		String [] lineParts = line.split(" ");
		String datePart = lineParts[0];
		String timeStamp = lineParts[1];
		String type = lineParts[5];
		String value = "";
		for (int i = 3; i < lineParts.length; i++) {
			value += lineParts[i];	
		}
		
		Date date;
		Date time;
		EntryType entryType;
		try {
			DateFormat dateformat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
			date = dateformat.parse(datePart); 
			DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
			time = timeformat.parse(timeStamp);
			//Remove Brackets [] and parse
			entryType = EntryType.valueOf(type.substring(1, type.length() - 1));
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return new LogFileEntry(entryType, date, time, value);
	}
}
