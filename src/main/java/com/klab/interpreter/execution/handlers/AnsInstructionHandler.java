package com.klab.interpreter.execution.handlers;

import com.klab.interpreter.commons.memory.IdentifierMapper;
import com.klab.interpreter.commons.memory.MemorySpace;
import com.klab.interpreter.execution.model.InstructionPointer;
import com.klab.interpreter.translate.model.InstructionCode;
import com.klab.interpreter.types.ObjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnsInstructionHandler extends AbstractInstructionHandler {

    private Integer ansAddress;
    private MemorySpace memorySpace;

    @Override
    public InstructionCode getSupportedInstructionCode() {
        return InstructionCode.ANS;
    }

    @Override
    public void handle(InstructionPointer instructionPointer) {
        if (executionContext.executionStackSize() > 0) {
            ObjectData data = executionContext.executionStackPop();
            memorySpace.set(ansAddress, data, "ans");
            executionContext.executionStackPush(data);
        }
        instructionPointer.increment();
    }

    @Autowired
    private void setIdentifierMapper(IdentifierMapper identifierMapper) {
        ansAddress = identifierMapper.registerMainIdentifier("ans");
    }

    @Autowired
    public void setMemorySpace(MemorySpace memorySpace) {
        this.memorySpace = memorySpace;
    }

}
