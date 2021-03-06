package com.klab.interpreter.commons.exception;

public class InterpreterCastException extends InterpreterException {

    public static final String COMPLEX_LOGICALS = "Complex values cannot be converted to logicals.";
    public static final String CANNOT_CAST_MATRIX_TO_SCALAR = "Cannot cast matrix to scalar";
    public static final String EXPECTED_INTEGER_VALUE = "Expected integer value.";
    private static final long serialVersionUID = 1L;

    public InterpreterCastException() {
        super();
    }

    public InterpreterCastException(String message) {
        super(message);
    }

    public InterpreterCastException(Throwable cause) {
        super(cause);
    }

}
