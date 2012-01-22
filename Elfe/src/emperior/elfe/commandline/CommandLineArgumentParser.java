package emperior.elfe.commandline;

import java.io.File;

public class CommandLineArgumentParser {

	private static final String ARGS_SOURCE_DIRECTORY = "-sd";
	private static final String ARGS_TARGET_DIRECTORY = "-td";
	private static final String ARGS_TASK_CLASS_DIRECTORYNAMES = "-tc";
	private static final String ARGS_TASKS_DIRECTORYNAMES = "-tn";
	private static final String ARGS_LOGFILE = "-lf";
	
	private File defaultSourceDirectory = new File("."); 
	private File defaultTargetDirectory = new File(".");
	private String defaultLogFileName = "experiment.log";
	
	public String getLogFileName() {
		if (sourceDirectory != null)
			return logFileName;
		else
			return defaultLogFileName;
	}
	
	private void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	public File getSourceDirectory() {
		if (sourceDirectory != null)
			return sourceDirectory;
		else
			return getDefaultSourceDirectory();
	}

	private void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getTargetDirectory() {
		if (targetDirectory != null)
			return targetDirectory;
		else
			return getDefaultTargetDirectory();
	}

	private void setTargetDirectory(File targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public String[] getTaskClasses() {
		return taskClasses;
	}

	private void setTaskClasses(String[] taskClasses) {
		this.taskClasses = taskClasses;
	}

	public String getTaskDirectories() {
		return taskDirectories;
	}

	private void setTaskDirectories(String taskDirectories) {
		this.taskDirectories = taskDirectories;
	}

	private File getDefaultSourceDirectory() {
		return defaultSourceDirectory;
	}

	private File getDefaultTargetDirectory() {
		return defaultTargetDirectory;
	}

	private File sourceDirectory;
	private File targetDirectory;
	private String [] taskClasses;
	private String taskDirectories;
	private String logFileName;
	
	public void parseCommandLineArguments(String [] arguments) throws UnknownCommandLineArgumentException, InvalidArgumentParametersException {
		
		for (String arg : arguments) {

			if (arg.startsWith(ARGS_SOURCE_DIRECTORY)) {
				evaluateSourceDirectoryArgument(arg);
				continue;
			}
			
			if (arg.startsWith(ARGS_TARGET_DIRECTORY)) {
				evaluateTargetDirectoryArgument(arg);
				continue;
			}
			
			if (arg.startsWith(ARGS_TASK_CLASS_DIRECTORYNAMES)) {
				evaluateTaskClassDirectoryNames(arg);
				continue;
			}
			
			if (arg.startsWith(ARGS_TASKS_DIRECTORYNAMES)) {				
				evaluateTaskDirectoryNamesArgument(arg);
				continue;
			}
			
			if (arg.startsWith(ARGS_LOGFILE)) {				
				evaluateLogFile(arg);
				continue;
			}
			
			throw new UnknownCommandLineArgumentException(arg);
		}
	}

	public static String getCommandLineHelp() {
		StringBuilder builder = new StringBuilder();
		builder.append("ELFE - Emperior Log File Evaluator\r\n");
		builder.append("\r\n");
		builder.append("Please use the following command line arguments\r\n");
		builder.append("\r\n");
		builder.append("Hint: The child directories of the source's root are treated as participant names!\r\n");
		
		return builder.toString();
	}
	
	
	private void evaluateLogFile(String arg) throws InvalidArgumentParametersException {
		String [] argumentParts = arg.split(" ");
		
		if (argumentParts.length <= 1 || argumentParts.length > 2 || argumentParts[1].equals(""))
			throw new InvalidArgumentParametersException(arg);
		
		String logFileName = argumentParts[1].trim();
		
		setLogFileName(logFileName);
	}

	private void evaluateTaskDirectoryNamesArgument(String arg) throws InvalidArgumentParametersException {
		String [] argumentParts = arg.split(" ");
		
		//skip first entry because it is just the argument delimiter
		if (argumentParts.length <= 1 || argumentParts.length > 2 || argumentParts[1].equals(""))
			throw new InvalidArgumentParametersException(arg);
		
		String taskDirectoriesString = argumentParts[1].trim();
		
		setTaskDirectories(taskDirectoriesString);
	}

	private void evaluateTaskClassDirectoryNames(String arg) throws InvalidArgumentParametersException {
		String [] argumentParts = arg.split(" ");
		
		//skip first entry because it is just the argument delimiter
		if (argumentParts.length <= 1)
			throw new InvalidArgumentParametersException(arg);
		
		String [] allTaskClasses = new String [argumentParts.length - 1];
		for (int i = 0; i < allTaskClasses.length; i++) {
			allTaskClasses[i] = argumentParts[i + 1].trim();
		}
		
		setTaskClasses(allTaskClasses);
		
	}

	private void evaluateTargetDirectoryArgument(String arg) throws InvalidArgumentParametersException {
		String [] argumentParts = arg.split(" ");
		
		//skip first entry because it is just the argument delimiter
		if (argumentParts.length <= 1 || argumentParts.length > 2 || argumentParts[1].equals(""))
			throw new InvalidArgumentParametersException(arg);
		
		String directory = argumentParts[1].trim();
		
		setTargetDirectory(new File(directory));
	}

	private void evaluateSourceDirectoryArgument(String arg) throws InvalidArgumentParametersException {
		String [] argumentParts = arg.split(" ");
		
		//skip first entry because it is just the argument delimiter
		if (argumentParts.length <= 1 || argumentParts.length > 2 || argumentParts[1].equals(""))
			throw new InvalidArgumentParametersException(arg);
		
		String directory = argumentParts[1].trim();
		
		setSourceDirectory(new File(directory));
		
	}
}
