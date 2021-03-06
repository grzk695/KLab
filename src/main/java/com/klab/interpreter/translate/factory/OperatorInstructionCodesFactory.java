package com.klab.interpreter.translate.factory;

import com.klab.interpreter.parsing.model.tokens.operators.OperatorCode;
import com.klab.interpreter.translate.model.InstructionCode;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class OperatorInstructionCodesFactory {
    private Map<OperatorCode, InstructionCode> instructionCodeMap = new EnumMap<>(OperatorCode.class);

    public OperatorInstructionCodesFactory() {
        instructionCodeMap.put(OperatorCode.ADD, InstructionCode.ADD);
        instructionCodeMap.put(OperatorCode.DIV, InstructionCode.DIV);
        instructionCodeMap.put(OperatorCode.MULT, InstructionCode.MULT);
        instructionCodeMap.put(OperatorCode.SUB, InstructionCode.SUB);
        instructionCodeMap.put(OperatorCode.ASSIGN, InstructionCode.STORE);
        instructionCodeMap.put(OperatorCode.EQ, InstructionCode.EQ);
        instructionCodeMap.put(OperatorCode.NEQ, InstructionCode.NEQ);
        instructionCodeMap.put(OperatorCode.GT, InstructionCode.GT);
        instructionCodeMap.put(OperatorCode.GE, InstructionCode.GE);
        instructionCodeMap.put(OperatorCode.LE, InstructionCode.LE);
        instructionCodeMap.put(OperatorCode.LT, InstructionCode.LT);
        instructionCodeMap.put(OperatorCode.RANGE, InstructionCode.RANGE);
        instructionCodeMap.put(OperatorCode.RANGE3, InstructionCode.RANGE3);
        instructionCodeMap.put(OperatorCode.ADIV, InstructionCode.ADIV);
        instructionCodeMap.put(OperatorCode.AMULT, InstructionCode.AMULT);
        instructionCodeMap.put(OperatorCode.POW, InstructionCode.POW);
        instructionCodeMap.put(OperatorCode.APOW, InstructionCode.APOW);
        instructionCodeMap.put(OperatorCode.TRANSPOSE, InstructionCode.TRANSPOSE);
        instructionCodeMap.put(OperatorCode.AAND, InstructionCode.AAND);
        instructionCodeMap.put(OperatorCode.AOR, InstructionCode.AOR);
        instructionCodeMap.put(OperatorCode.AND, InstructionCode.AAND);
        instructionCodeMap.put(OperatorCode.OR, InstructionCode.AOR);
        instructionCodeMap.put(OperatorCode.NOT, InstructionCode.NOT);
        instructionCodeMap.put(OperatorCode.NEG, InstructionCode.NEG);
    }

    public InstructionCode get(OperatorCode operatorCode) {
        return instructionCodeMap.get(operatorCode);
    }
}
