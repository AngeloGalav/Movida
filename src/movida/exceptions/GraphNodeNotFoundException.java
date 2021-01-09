package movida.exceptions;

public class GraphNodeNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Node not found.";
	}
	
}
