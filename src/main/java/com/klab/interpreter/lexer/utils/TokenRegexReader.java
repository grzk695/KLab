package com.klab.interpreter.lexer.utils;

import com.klab.interpreter.lexer.exception.TokenReadException;
import com.klab.interpreter.lexer.model.Token;
import com.klab.interpreter.lexer.model.TokenClass;
import com.klab.interpreter.lexer.model.TokenizerContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenRegexReader {

    private TokenizerContext tokenizerContext;

    public TokenRegexReader(TokenizerContext tokenizerContext) {
        this.tokenizerContext = tokenizerContext;
    }

    public Token readToken(final Pattern pattern, TokenClass tokenClass) {
        Token token = new Token();
        token.setLexeme(tryMatchLexeme(pattern));
        token.setTokenClass(tokenClass);
        return token;
    }

    public String tryMatchLexeme(final Pattern pattern) {
        Matcher matcher = pattern.matcher(tokenizerContext.getInputText());
        if (!matcher.find()) {
            throw new TokenReadException("Cannot read token", tokenizerContext);
        }
        return matcher.group();
    }
}
