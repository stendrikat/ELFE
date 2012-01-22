package emperior.elfe.commandline;

public class InvalidArgumentParametersException extends Exception {

	private static final long serialVersionUID = 1L;
	
	String argument;
	
	public InvalidArgumentParametersException(String argument) {
		super("Invalid Parameters or wrong number of Parameters for Command Line Argument: " + argument);
		
		this.argument = argument;
	}
	
	public String getArgument() {
		return argument;
	}

}
