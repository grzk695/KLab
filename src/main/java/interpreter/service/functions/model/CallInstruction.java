package interpreter.service.functions.model;

import interpreter.translate.model.Instruction;
import interpreter.translate.model.InstructionCode;

public class CallInstruction extends Instruction {
    private CallToken callToken;
    private int expectedOutputSize;

    public CallInstruction(CallToken identifierToken) {
        this.callToken = identifierToken;
        setInstructionCode(InstructionCode.CALL);
    }

    public Integer getVariableAddress() {
        return callToken.getVariableAddress();
    }

    public Integer getInternalFunctionAddress() {
        return callToken.getInternalFunctionAddress();
    }

    public String getName() {
        return callToken.getCallName();
    }

    public int getExpectedOutputSize() {
        return expectedOutputSize;
    }

    public void setExpectedOutputSize(int expectedOutputSize) {
        this.expectedOutputSize = expectedOutputSize;
    }

    @Override
    public String toString() {
        return super.toString() +
                callToken.getCallName() + "@" +
                callToken.getVariableAddress() + "$" +
                callToken.getInternalFunctionAddress() + "#" +
                getArgumentsNumber();
    }

}
