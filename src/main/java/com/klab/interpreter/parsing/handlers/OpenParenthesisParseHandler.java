package com.klab.interpreter.parsing.handlers;

import com.klab.interpreter.lexer.model.Token;
import com.klab.interpreter.lexer.model.TokenClass;
import com.klab.interpreter.parsing.model.ParseClass;
import com.klab.interpreter.parsing.model.tokens.ParenthesisParseToken;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpenParenthesisParseHandler extends AbstractParseHandler {

    @Override
    public void handle() {
        Token token = pCtxMgr.tokenAt(0);
        pCtxMgr.stackPush(new ParenthesisParseToken(token, ParseClass.OPEN_PARENTHESIS));
        pCtxMgr.incrementTokenPosition(1);
    }

    @Override
    public TokenClass getSupportedTokenClass() {
        return TokenClass.OPEN_PARENTHESIS;
    }
}