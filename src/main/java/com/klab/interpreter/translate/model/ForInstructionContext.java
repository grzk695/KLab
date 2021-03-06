package com.klab.interpreter.translate.model;

import com.klab.interpreter.lexer.model.CodeAddress;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class ForInstructionContext {
    private Deque<ForIterator> iterators = new ArrayDeque<>();

    public void push(int flhnextAddress, JumperInstruction falseJumper) {
        iterators.addFirst(new ForIterator(flhnextAddress, falseJumper));
    }

    public void addFalseJumper(JumperInstruction falseJumper) {
        iterators.peekFirst().falseJumpers.add(falseJumper);
    }

    public String getName() {
        return iterators.peekFirst().name;
    }

    public void setName(String name) {
        iterators.peekFirst().name = name;
    }

    public String getIteratorDataName() {
        return iterators.peekFirst().iteratorDataName;
    }

    public void setIteratorDataName(String iteratorDataName) {
        iterators.peekFirst().iteratorDataName = iteratorDataName;
    }

    public int getFlhNextAddress() {
        return iterators.peekFirst().flhNextAddress;
    }

    public void pop() {
        iterators.removeFirst();
    }

    public int size() {
        return iterators.size();
    }

    public void setJumpsOnFalse(int i) {
        iterators.peekFirst().falseJumpers.forEach(jmp -> jmp.setJumpIndex(i));
    }

    public CodeAddress getCodeAddress() {
        return iterators.peekFirst().codeAddress;
    }

    public void setCodeAddress(CodeAddress codeAddress) {
        iterators.peekFirst().codeAddress = codeAddress;
    }

    public String getScriptId() {
        return iterators.peekFirst().scriptId;
    }

    public void setScriptId(String scriptId) {
        iterators.peekFirst().scriptId = scriptId;
    }

    private static class ForIterator {
        public String name;
        String iteratorDataName;
        CodeAddress codeAddress;
        String scriptId;
        Set<JumperInstruction> falseJumpers = new HashSet<>();
        int flhNextAddress;

        ForIterator(int flhNextAddress, JumperInstruction falseJumper) {
            this.flhNextAddress = flhNextAddress;
            falseJumpers.add(falseJumper);
        }
    }
}
