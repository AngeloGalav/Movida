package movida.exceptions;

public class HashKeyNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Hash key not found.";
	}

}
