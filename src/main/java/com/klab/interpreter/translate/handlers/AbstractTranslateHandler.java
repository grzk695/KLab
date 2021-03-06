package com.klab.interpreter.translate.handlers;

import com.klab.interpreter.lexer.model.CodeAddress;
import com.klab.interpreter.parsing.model.ParseToken;
import com.klab.interpreter.parsing.model.expression.Expression;
import com.klab.interpreter.translate.service.TranslateContextManager;

public abstract class AbstractTranslateHandler implements TranslateHandler {
    protected TranslateContextManager tCM;

    protected static CodeAddress address(Expression<ParseToken> expression) {
        return expression.getValue().getAddress();
    }

    @Override
    public void setTranslateContextManager(TranslateContextManager translateContextManager) {
        this.tCM = translateContextManager;
    }
}
