package interpreter.translate.model.instruction;

import interpreter.commons.ObjectData;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    private InstructionCode instructionCode;
    private Integer argumentsNumber;
    private List<ObjectData> objectDataList = new ArrayList<>();

    public InstructionCode getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(InstructionCode instructionCode) {
        this.instructionCode = instructionCode;
    }

    public Integer getArgumentsNumber() {
        return argumentsNumber;
    }

    public void setArgumentsNumber(Integer argumentsNumber) {
        this.argumentsNumber = argumentsNumber;
    }

    public void add(ObjectData objectData) {
        objectDataList.add(objectData);
    }
}
