package com.klab.interpreter.parsing.handlers;

import com.klab.interpreter.lexer.model.Token;
import com.klab.interpreter.lexer.model.TokenClass;
import com.klab.interpreter.parsing.model.ParseToken;
import com.klab.interpreter.parsing.service.ParseContextManager;

public abstract class AbstractParseHandler implements ParseHandler {
    protected ParseContextManager parseContextManager;

    TokenClass tokenClassAt(int offset) {
        Token token = parseContextManager.tokenAt(offset);
        return token != null ? token.getTokenClass() : null;
    }

    @Override
    public void setParseContextManager(ParseContextManager parseContextManager) {
        this.parseContextManager = parseContextManager;
    }

    protected Token tokenAt(int offset) {
        return parseContextManager.tokenAt(offset);
    }

    protected void addExpressionValue(ParseToken parseToken) {
        parseContextManager.addExpressionValue(parseToken);
    }

    protected void incrementTokenPosition(int value) {
        parseContextManager.incrementTokenPosition(value);
    }
}
