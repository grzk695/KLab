package com.klab.interpreter.functions.exception;

import com.klab.interpreter.commons.exception.InterpreterException;
import com.klab.interpreter.functions.InternalFunction;

public class WrongNumberOfArgumentsException extends InterpreterException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE_FORMAT = "Wrong number of argumens in '%s' function call.";
    private InternalFunction internalFunction;

    public WrongNumberOfArgumentsException(InternalFunction internalFunction) {
        this.internalFunction = internalFunction;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE_FORMAT, internalFunction.getName());
    }

}
