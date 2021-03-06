package com.klab.interpreter.parsing.service;

import com.klab.interpreter.lexer.model.TokenList;
import com.klab.interpreter.parsing.model.ParseToken;
import com.klab.interpreter.parsing.model.expression.Expression;

import java.util.List;

public interface ParserService {
    List<Expression<ParseToken>> process();

    void setupTokenList(TokenList tokenList);

    boolean hasNext();
}
