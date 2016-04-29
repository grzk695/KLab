package com.klab.interpreter.execution.handlers.operators;

import com.klab.interpreter.translate.model.InstructionCode;
import com.klab.interpreter.types.NumericObject;
import com.klab.interpreter.types.NumericType;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AAndInstructionHandler extends AbstractOperatorInstructionHandler {
    @Override
    public InstructionCode getSupportedInstructionCode() {
        return InstructionCode.AAND;
    }

    @Override
    protected NumericObject calculate(NumericObject a, NumericObject b, NumericType type) {
        return operatorFactory.getOperator(type).aand(convert(a, type), convert(b, type));
    }
}
