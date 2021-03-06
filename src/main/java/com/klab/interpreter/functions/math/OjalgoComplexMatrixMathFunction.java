package com.klab.interpreter.functions.math;

import com.klab.interpreter.types.NumericObject;
import com.klab.interpreter.types.NumericType;
import com.klab.interpreter.types.matrix.Matrix;
import com.klab.interpreter.types.matrix.ojalgo.OjalgoComplexMatrix;
import com.klab.interpreter.types.scalar.ComplexScalar;
import org.ojalgo.matrix.store.ComplexDenseStore;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.task.DeterminantTask;
import org.ojalgo.matrix.task.InverterTask;
import org.ojalgo.scalar.ComplexNumber;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OjalgoComplexMatrixMathFunction extends OjalgoAbstractMatrixMathFunction<ComplexNumber> {
    @PostConstruct
    public void init() {
        setInverterFactory(InverterTask.COMPLEX);
        setDeterminantFactory(DeterminantTask.COMPLEX);
        setFunctionSet(ComplexDenseStore.FACTORY.function());
    }

    @Override
    public NumericType supports() {
        return NumericType.COMPLEX_MATRIX;
    }

    @Override
    protected Matrix<ComplexNumber> create(MatrixStore<ComplexNumber> store) {
        return new OjalgoComplexMatrix(store);
    }

    @Override
    protected NumericObject create(ComplexNumber scalar) {
        return new ComplexScalar(scalar);
    }
}
