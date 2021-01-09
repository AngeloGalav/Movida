package movida.exceptions;

public class GraphLinkNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "The Link does not appear to exist";
	}

}
