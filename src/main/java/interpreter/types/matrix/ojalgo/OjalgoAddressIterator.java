package interpreter.types.matrix.ojalgo;

import interpreter.types.AddressIterator;
import org.ojalgo.matrix.store.MatrixStore;

import java.util.Iterator;
import java.util.stream.Stream;

public class OjalgoAddressIterator<N extends Number> implements AddressIterator {
    private Iterator<N> iterator;
    private long dataLength;
    private MatrixStore<N> data;
    private Long max;

    public OjalgoAddressIterator(MatrixStore<N> data) {
        this.data = data;
        iterator = data.iterator();
        dataLength = data.count();
    }

    @Override
    public long max() {
        if(max == null) {
            max = 0L;
            for (N n : data) {
                if (n.longValue() > max) {
                    max = n.longValue();
                }
            }
        }
        return this.max;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public long getNext() {
        return iterator.next().longValue();
    }

    @Override
    public long length() {
        return dataLength;
    }

    @Override
    public void reset() {
        iterator = data.iterator();
    }
}
