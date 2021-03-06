package com.klab.interpreter.translate.model;

import com.klab.interpreter.lexer.model.CodeAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class MacroInstruction implements Iterable<Instruction> {
    private List<Instruction> instructions = new ArrayList<>();

    public Instruction get(int index) {
        return instructions.get(index);
    }

    public List<Instruction> get() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public Iterator<Instruction> iterator() {
        return instructions.iterator();
    }

    public MacroInstruction add(Instruction instruction, CodeAddress codeAddress) {
        instruction.setCodeAddress(codeAddress);
        instructions.add(instruction);
        return this;
    }

    public MacroInstruction add(MacroInstruction macroInstruction) {
        instructions.addAll(macroInstruction.instructions);
        return this;
    }

    public void forEach(Consumer<? super Instruction> consumer) {
        instructions.forEach(consumer);
    }

    public int size() {
        return instructions.size();
    }
}
