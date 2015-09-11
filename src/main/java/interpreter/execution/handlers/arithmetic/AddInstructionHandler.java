package interpreter.execution.handlers.arithmetic;

import interpreter.commons.ObjectData;
import interpreter.execution.model.InstructionPointer;

public class AddInstructionHandler extends AbstractArithmeticInstructionHandler {

    @Override
    public void handle(InstructionPointer instructionPointer) {
        ObjectData b = executionContext.executionStackPop();
        ObjectData a = executionContext.executionStackPop();
        ObjectData result = arithmeticOperationsFactory.getAdder(numberType(a, b)).add(a, b);
        executionContext.executionStackPush(result);
        instructionPointer.increment();
    }
}
