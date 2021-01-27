package movida.exceptions;

public class UnknownMapException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Data Structure is unknown. Please select a known datatype.";
	}

}
