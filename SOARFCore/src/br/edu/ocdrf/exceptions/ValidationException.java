package br.edu.ocdrf.exceptions;

public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2819083945871846751L;

	/**
	 * 
	 */
	public ValidationException() {
		super();
	}

	/**
	 * 
	 * @param e
	 */
	public ValidationException(Exception e) {
		super(e);
	}
}
