package com.klab.interpreter.core.arithmetic.matrix.ojalgo;

import com.klab.interpreter.types.NumericObject;
import com.klab.interpreter.types.Sizeable;
import com.klab.interpreter.types.matrix.ojalgo.OjalgoAbstractMatrix;
import com.klab.interpreter.types.matrix.ojalgo.OjalgoMatrixCreator;
import org.ojalgo.matrix.store.MatrixStore;

public abstract class OjalgoOperator<T extends Number> {
    private OjalgoMatrixCreator<T> creator;

    public OjalgoOperator(OjalgoMatrixCreator<T> creator) {
        this.creator = creator;
    }

    private <N extends OjalgoAbstractMatrix<T>> N cast(NumericObject a) {
        return (N) a;
    }

    public NumericObject operate(NumericObject a, NumericObject b) {
        OjalgoAbstractMatrix<T> first = cast(a);
        OjalgoAbstractMatrix<T> second = cast(b);
        checkSize(first, second);
        return creator.create(operate(first, second));
    }

    protected NumericObject operate(NumericObject a, NumericObject b, OjalgoBinaryAction<T> action) {
        OjalgoAbstractMatrix<T> first = cast(a);
        OjalgoAbstractMatrix<T> second = cast(b);
        checkSize(first, second);
        return creator.create(action.operate(first, second));
    }

    protected NumericObject operate(NumericObject a, NumericObject b, MatrixStoreAction<T> action) {
        OjalgoAbstractMatrix<T> first = cast(a);
        OjalgoAbstractMatrix<T> second = cast(b);
        if (first.isScalar()) {
            return creator.create(action.operate(new OjalgoMatrixScalarWrapper<>(first, second), second.getLazyStore()));
        }
        if (second.isScalar()) {
            return creator.create(action.operate(first.getLazyStore(), new OjalgoMatrixScalarWrapper<>(second, first)));
        }
        return creator.create(action.operate(first.getLazyStore(), second.getLazyStore()));
    }

    protected abstract MatrixStore<T> operate(OjalgoAbstractMatrix<T> first, OjalgoAbstractMatrix<T> second);

    protected void checkSize(Sizeable a, Sizeable b) {
        if (!a.isScalar() && !b.isScalar() && (a.getRows() != b.getRows() || a.getColumns() != b.getColumns())) {
            throw new RuntimeException("Sizes does not match!");
        }
    }
}
