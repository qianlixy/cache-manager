package io.github.qianlixy.cache.exception;

public class SQLParseException extends RuntimeException {

	private static final long serialVersionUID = 6769724160813159324L;

	public SQLParseException() {
		super();
	}
	
	public SQLParseException(String message) {
		super(message);
	}

	public SQLParseException(Throwable cause) {
		super(cause);
	}

}
