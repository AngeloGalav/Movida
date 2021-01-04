package movida.exceptions;

public class ObjectNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Object not found. Try another Object.";
	}
}
