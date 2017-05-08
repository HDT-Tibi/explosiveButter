package backend.exceptions;

public class CustomException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}

}

