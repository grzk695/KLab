package com.klab.interpreter.lexer.service;

import com.klab.interpreter.lexer.exception.UnrecognizedTokenException;
import com.klab.interpreter.lexer.model.TokenList;
import com.klab.interpreter.lexer.model.TokenizerContext;
import com.klab.interpreter.lexer.utils.TokenRegexReader;
import com.klab.interpreter.lexer.utils.TokenStartMatcher;

public abstract class AbstractTokenizer implements Tokenizer {
    private static final String UNRECOGNIZED_TOKEN_MESSAGE = "Unrecognized token.";
    protected TokenizerContext tC;
    protected TokenizerContextManager tCM;
    protected TokenRegexReader tokenRegexReader;
    private TokenStartMatcher tokenStartMatcher;


    public synchronized TokenList readTokens(String inputText) {
        setContext(inputText);
        setState();
        process();
        return tC.getTokenList();
    }

    public abstract void onNumber();

    public abstract void onWord();

    public abstract void onSpaceOrTabulator();

    public abstract void onNewLine();

    public abstract boolean tryReadOperator();

    public abstract boolean tryReadOtherSymbol();

    private void setState() {
        tokenStartMatcher = new TokenStartMatcher(tC);
        tCM = new TokenizerContextManager(tC);
        tokenRegexReader = new TokenRegexReader(tC);
    }

    private void process() {
        while (!tC.isInputEnd()) {
            takeAction();
        }
    }

    private void takeAction() {
        if (tokenStartMatcher.isNumberStart()) {
            onNumber();
            return;
        }
        if (tokenStartMatcher.isWordStart()) {
            onWord();
            return;
        }
        if (tokenStartMatcher.isSpaceOrTabulatorStart()) {
            onSpaceOrTabulator();
            return;
        }
        if (tryReadOperator()) {
            return;
        }
        if (tokenStartMatcher.isNewLineStart()) {
            onNewLine();
            return;
        }
        if (tryReadOtherSymbol()) {
            return;
        }
        throw new UnrecognizedTokenException(UNRECOGNIZED_TOKEN_MESSAGE, tC);
    }

    protected void setContext(String inputText) {
        tC = new TokenizerContext(inputText);
    }
}