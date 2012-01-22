package emperior.elfe.commandline;

public class UnknownCommandLineArgumentException extends Exception {

	
	private static final long serialVersionUID = 1L;
	String argument;
	
	public UnknownCommandLineArgumentException(String argument) {
		super("Unknown Command Line Argument: " + argument);
		
		this.argument = argument;
	}
	
	public String getArgument() {
		return argument;
	}
}
