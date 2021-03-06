package com.klab.interpreter.parsing.model;

import com.klab.interpreter.lexer.model.Token;

public class CallToken extends ParseToken {
    private Integer variableAddress;
    private Integer externalAddress;
    private Integer internalFunctionAddress;

    public CallToken(Token token) {
        super(token, ParseClass.CALL);
    }

    public CallToken(Token token, ParseClass parseClass) {
        super(token, parseClass);
    }

    public Integer getExternalAddress() {
        return externalAddress;
    }

    public void setExternalAddress(Integer externalAddress) {
        this.externalAddress = externalAddress;
    }

    public String getCallName() {
        return getToken().getLexeme();
    }

    public Integer getVariableAddress() {
        return variableAddress;
    }

    public void setVariableAddress(Integer variableAddress) {
        this.variableAddress = variableAddress;
    }

    public Integer getInternalFunctionAddress() {
        return internalFunctionAddress;
    }

    public void setInternalFunctionAddress(Integer internalFunctionAddress) {
        this.internalFunctionAddress = internalFunctionAddress;
    }
}
