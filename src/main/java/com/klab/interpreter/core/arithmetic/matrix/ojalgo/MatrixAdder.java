package com.klab.interpreter.core.arithmetic.matrix.ojalgo;

import com.klab.interpreter.types.matrix.ojalgo.OjalgoAbstractMatrix;
import com.klab.interpreter.types.matrix.ojalgo.OjalgoMatrixCreator;
import org.ojalgo.matrix.store.MatrixStore;

public class MatrixAdder<T extends Number> extends OjalgoOperator<T> {
    public MatrixAdder(OjalgoMatrixCreator<T> creator) {
        super(creator);
    }

    @Override
    protected MatrixStore<T> operate(OjalgoAbstractMatrix<T> first, OjalgoAbstractMatrix<T> second) {
        if (first.isScalar())
            return second.getLazyStore().add(new OjalgoMatrixScalarWrapper<>(first, second));
        else if (second.isScalar())
            return first.getLazyStore().add(new OjalgoMatrixScalarWrapper<>(second, first));
        return first.getLazyStore().add(second.getLazyStore());
    }
}
