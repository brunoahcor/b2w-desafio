package br.com.b2w.desafio.exception;

public class PlanetNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PlanetNotFoundException(String exception) {
		super(exception);
	}
	
}
