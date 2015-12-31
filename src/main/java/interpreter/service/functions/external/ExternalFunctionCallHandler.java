package interpreter.service.functions.external;

import interpreter.commons.MemorySpace;
import interpreter.execution.handlers.AbstractInstructionHandler;
import interpreter.execution.model.InstructionPointer;
import interpreter.service.functions.model.CallInstruction;
import interpreter.translate.model.InstructionCode;
import interpreter.types.ObjectData;
import interpreter.types.scalar.NumberScalarFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExternalFunctionCallHandler extends AbstractInstructionHandler {
    private ExternalFunctionService externalFunctionService;
    private MemorySpace memorySpace;
    private NumberScalarFactory numberScalarFactory;

    @Override
    public void handle(InstructionPointer instructionPointer) {
        CallInstruction instr = (CallInstruction) instructionPointer.currentInstruction();
        ExternalFunction extFunction = externalFunctionService.loadFunction(instr);
        if (extFunction != null) {
            final int nargin = instr.getArgumentsNumber();
            final ObjectData[] data = new ObjectData[extFunction.getMemoryLength()];
            for (int i = nargin - 1; i >= 0; i--) {
                data[i] = executionContext.executionStackPop();
            }
            data[extFunction.getNarginAddress()] = numberScalarFactory.getDouble(instr.getArgumentsNumber());
            data[extFunction.getNargoutAddress()] = numberScalarFactory.getDouble(instr.getExpectedOutputSize());
            executionContext.storeExecutionStackSize();
            memorySpace.newScope(data);
            instructionPointer.increment();
            instructionPointer.moveToCode(extFunction.getCode());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public InstructionCode getSupportedInstructionCode() {
        return null;
    }

    @Autowired
    public void setMemorySpace(MemorySpace memorySpace) {
        this.memorySpace = memorySpace;
    }

    @Autowired
    public void setExternalFunctionService(ExternalFunctionService externalFunctionService) {
        this.externalFunctionService = externalFunctionService;
    }

    @Autowired
    public void setNumberScalarFactory(NumberScalarFactory numberScalarFactory) {
        this.numberScalarFactory = numberScalarFactory;
    }
}
