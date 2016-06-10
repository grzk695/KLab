package com.klab.interpreter.parsing.handlers;

import com.klab.interpreter.lexer.model.TokenClass;
import com.klab.interpreter.parsing.model.BalanceType;
import com.klab.interpreter.parsing.model.ParseClass;
import com.klab.interpreter.parsing.model.ParseToken;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EndWordParseHandler extends AbstractParseHandler {
    @Override
    public void handle() {
        //TODO
        if (parseContextManager.getBalanceContext().isBalanceType(BalanceType.FUNCTION_ARGUMENTS)) {
            parseContextManager.addExpressionValue(new ParseToken(parseContextManager.tokenAt(0), ParseClass.LAST_CELL));
            parseContextManager.incrementTokenPosition(1);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public TokenClass getSupportedTokenClass() {
        return TokenClass.END_KEYWORD;
    }
}
