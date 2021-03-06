package com.klab.interpreter.types.scalar;

import com.klab.interpreter.types.NumericObject;
import org.springframework.stereotype.Service;

@Service("numberScalarFactory")
public class StandardNumberScalarFactory implements NumberScalarFactory {
    @Override
    public NumericObject getDouble(double value) {
        return new DoubleScalar(value);
    }

    @Override
    public NumericObject getDouble(String value) {
        if (value.endsWith("i")) {
            value = value.replace("i", "");
            return new ComplexScalar(0D, Double.valueOf(value));
        }
        return new DoubleScalar(Double.valueOf(value));
    }

    @Override
    public Scalar<Double> getDouble(boolean value) {
        return new DoubleScalar(value ? 1D : 0D);
    }
}
