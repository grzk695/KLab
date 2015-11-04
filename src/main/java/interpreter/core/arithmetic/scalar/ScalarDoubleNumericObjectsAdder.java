package interpreter.core.arithmetic.scalar;

import interpreter.core.arithmetic.NumericObjectsAdder;
import interpreter.types.NumericObject;
import interpreter.types.NumericType;
import interpreter.types.scalar.DoubleScalar;
import org.springframework.stereotype.Component;

@Component
public class ScalarDoubleNumericObjectsAdder implements NumericObjectsAdder {
    @Override
    public NumericObject add(NumericObject a, NumericObject b) {
        Double first = ((DoubleScalar) a).getValue();
        Double second = ((DoubleScalar) b).getValue();
        return new DoubleScalar(first + second);
    }

    @Override
    public NumericType getSupportedType() {
        return NumericType.DOUBLE;
    }
}
