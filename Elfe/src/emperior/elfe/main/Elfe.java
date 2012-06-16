package emperior.elfe.main;

import java.io.*;

import emperior.elfe.commandline.CommandLineArgumentParser;

public class Elfe {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String debugSourceDirectory = null;
		String debugTargetDirectory = null;
		String [] debugTaskClasses = null;
		String debugTaskDirectories = null;
		String debugLogFileName = null;
		
		if (args == null || args.length == 0)
		{
			System.out.println(CommandLineArgumentParser.getCommandLineHelp());
			//return;

			//TODO: Parsing and evaluating of command line values currently not implemented
			//TODO: Use Default Values here during debugging
			debugSourceDirectory="D:\\logfiles";
			debugTargetDirectory="D:\\auswertungen";
			debugTaskClasses= new String []{ "v1", "v2" };
			debugTaskDirectories="Task";
			debugLogFileName = "experiment.log";
		}
		
		ElfeDispatcher dispatcher = new ElfeDispatcher(debugSourceDirectory,
				debugTargetDirectory, debugTaskClasses, debugTaskDirectories, debugLogFileName);
		dispatcher.executeLogFileParsingAnalyzingSaving();
	}

}
