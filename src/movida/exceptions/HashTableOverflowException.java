package movida.exceptions;

public class HashTableOverflowException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Hashtable overflow. Cannot insert anymore elements.";
	}

}
