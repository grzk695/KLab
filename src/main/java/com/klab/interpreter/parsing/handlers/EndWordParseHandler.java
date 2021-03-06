package com.klab.interpreter.parsing.handlers;

import com.klab.interpreter.lexer.model.TokenClass;
import com.klab.interpreter.parsing.model.BalanceType;
import com.klab.interpreter.parsing.model.KeywordBalance;
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
        if (parseContextManager.getBalanceContext().isBalanceType(BalanceType.FUNCTION_ARGUMENTS)) {
            parseContextManager.addExpressionValue(new ParseToken(parseContextManager.tokenAt(0), ParseClass.LAST_CELL));
            parseContextManager.incrementTokenPosition(1);
        } else {
            if (KeywordBalance.FOR_INSTRUCTION == parseContextManager.getBalanceContext().peekKeyword()) {
                parseContextManager.tokenAt(0).setTokenClass(TokenClass.END_FOR_KEYWORD);
            } else if (KeywordBalance.IF_INSTRUCTION == parseContextManager.getBalanceContext().peekKeyword()) {
                parseContextManager.tokenAt(0).setTokenClass(TokenClass.ENDIF_KEYWORD);
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public TokenClass supportedTokenClass() {
        return TokenClass.END_KEYWORD;
    }
}
