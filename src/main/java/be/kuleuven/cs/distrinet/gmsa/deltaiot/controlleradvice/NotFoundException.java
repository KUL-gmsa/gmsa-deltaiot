package be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice;

public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
	}
	
	public NotFoundException(String message) {
		super(message);
	}
	
}
