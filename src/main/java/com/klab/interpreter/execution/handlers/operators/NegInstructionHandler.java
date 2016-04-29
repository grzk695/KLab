package com.klab.interpreter.execution.handlers.operators;

import com.klab.interpreter.core.arithmetic.factory.NumericObjectsOperatorFactory;
import com.klab.interpreter.execution.handlers.AbstractInstructionHandler;
import com.klab.interpreter.execution.model.InstructionPointer;
import com.klab.interpreter.translate.model.InstructionCode;
import com.klab.interpreter.types.NumericObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NegInstructionHandler extends AbstractInstructionHandler {
    private NumericObjectsOperatorFactory numericObjectsOperatorFactory;

    @Override
    public InstructionCode getSupportedInstructionCode() {
        return InstructionCode.NEG;
    }

    @Override
    public void handle(InstructionPointer instructionPointer) {
        NumericObject a = (NumericObject) executionContext.executionStackPop();
        executionContext.executionStackPush(numericObjectsOperatorFactory.getOperator(a.getNumericType()).negate(a));
        instructionPointer.increment();
    }

    @Autowired
    public void setNumericObjectsOperatorFactory(NumericObjectsOperatorFactory numericObjectsOperatorFactory) {
        this.numericObjectsOperatorFactory = numericObjectsOperatorFactory;
    }
}
