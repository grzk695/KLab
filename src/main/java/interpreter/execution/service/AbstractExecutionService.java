package interpreter.execution.service;

import interpreter.execution.handlers.InstructionHandler;
import interpreter.execution.model.ExecutionContext;
import interpreter.execution.model.InstructionPointer;
import interpreter.translate.model.Instruction;
import interpreter.translate.model.InstructionCode;

import java.util.Collection;
import java.util.Set;

import static java.util.Objects.nonNull;

public abstract class AbstractExecutionService {

    protected InstructionHandler[] instructionHandlers = new InstructionHandler[InstructionCode.values().length];
    protected ExecutionContext executionContext;
    protected InstructionPointer instructionPointer;

    public AbstractExecutionService(Set<InstructionHandler> instructionHandlers) {
        setupExecutionContext(new ExecutionContext());
        instructionHandlers.stream().filter(instructionHandler -> nonNull(instructionHandler.getSupportedInstructionCode()))
                .forEach(this::registerInstructionHandler);
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    public void addInstructions(Collection<? extends Instruction> instructions) {
        executionContext.addInstruction(instructions);
    }

    public void resetCodeAndStack() {
        executionContext.clearExecutionStack();
        executionContext.clearCode();
        instructionPointer = executionContext.newInstructionPointer();
    }

    private void setupExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
        this.instructionPointer = executionContext.newInstructionPointer();
    }

    private void registerInstructionHandler(InstructionHandler instructionHandler) {
        InstructionCode instructionCode = instructionHandler.getSupportedInstructionCode();
        instructionHandlers[instructionCode.getIndex()] = instructionHandler;
        instructionHandler.setExecutionContext(executionContext);
    }
}
