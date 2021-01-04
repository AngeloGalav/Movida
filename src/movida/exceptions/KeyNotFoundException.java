package movida.exceptions;

public class KeyNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Key not found. Try another key.";
	}
}
