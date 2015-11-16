package interpreter.types.matrix.ojalgo;

import interpreter.types.*;
import interpreter.types.matrix.Matrix;
import interpreter.types.scalar.Scalar;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;

import java.util.function.Consumer;

public abstract class OjalgoAbstractMatrix<T extends Number> extends AbstractNumericObject implements Matrix<T>, Sizeable {
    private PhysicalStore<T> matrixStore;
    private MatrixStore<T> lazyStore;
    private PhysicalStore.Factory<T, ? extends PhysicalStore<T>> factory;

    public OjalgoAbstractMatrix(NumericType numericType) {
        super(numericType);
    }

    protected abstract OjalgoAbstractMatrix<T> create(MatrixStore<T> matrixStore);

    @Override
    public boolean isTrue() {
        for (T value : getMatrixStore()) {
            if (value.doubleValue() == 0.0D) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ObjectData get(AddressIterator cell) {
        if (cell.length() == 1) {
            return createScalar(getLazyStore().get(cell.getNext() - 1));
        }
        PhysicalStore<T> matrix = createMatrixStore(cell.length(), 1);
        int address = 0;
        while (cell.hasNext()) {
            matrix.set(address++, lazyStore.get(cell.getNext() - 1));
        }
        return create(matrix);
    }

    @Override
    public ObjectData get(AddressIterator row, AddressIterator column) {
        if (row.length() == 1 && column.length() == 1) {
            return createScalar(getLazyStore().get(row.getNext() - 1, column.getNext() - 1));
        }
        PhysicalStore<T> matrix = createMatrixStore(row.length(), column.length());
        long rowIndex = 0l;
        long colIndex = 0l;
        while (row.hasNext()) {
            long rowAddress = row.getNext() - 1;
            while (column.hasNext()) {
                matrix.set(rowIndex, colIndex++, lazyStore.get(rowAddress, column.getNext() - 1));
            }
            column.reset();
            colIndex = 0;
            rowIndex++;
        }
        return create(matrix);
    }

    protected abstract Scalar createScalar(Number number);

    @Override
    public String toString() {
        return OjalgoMatrixPrinter.toString(getMatrixStore());
    }

    @Override
    public long getRows() {
        return getLazyStore().countRows();
    }

    @Override
    public long getColumns() {
        return getLazyStore().countColumns();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        getMatrixStore().forEach(action);
    }

    @Override
    public T getNumber(int m) {
        return getLazyStore().get(m);
    }

    @Override
    public void set(int m, int n, T value) {
        getMatrixStore().set(m, n, value);
    }

    public PhysicalStore<T> getMatrixStore() {
        if (matrixStore == null) {
            matrixStore = getLazyStore().copy();
        }
        return matrixStore;
    }

    public MatrixStore<T> getLazyStore() {
        return lazyStore;
    }

    public void setLazyStore(MatrixStore<T> lazyStore) {
        this.lazyStore = lazyStore;
    }

    public void setFactory(PhysicalStore.Factory<T, ? extends PhysicalStore<T>> factory) {
        this.factory = factory;
    }

    private PhysicalStore<T> createMatrixStore(long rows, long columns) {
        return factory.makeZero(rows, columns);
    }
}
