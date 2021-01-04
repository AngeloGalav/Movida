package movida.exceptions;

public class UnknownSortException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Sorting Algorithm is unknown. Please select a known Sort Algprithm.";
	}

}
