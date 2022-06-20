package antihackerman.exceptions;

public class BlacklistedTokenException extends RuntimeException{
	
	public BlacklistedTokenException(String message) {
		super(message);
	}

}
