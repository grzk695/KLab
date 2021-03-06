package com.klab.interpreter.parsing.exception;

import com.klab.interpreter.parsing.model.ParseContext;

public abstract class ParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected ParseContext parseContext;

    public ParseException(String message, ParseContext parseContext) {
        super(message);
        this.parseContext = parseContext;
    }
}
