package com.klab.interpreter.types;

public interface ObjectData {
    default String getName() {
        throw new UnsupportedOperationException();
    }

    default void setName(String name) {
        throw new UnsupportedOperationException();
    }

    default boolean isTrue() {
        throw new UnsupportedOperationException();
    }

    void setAddress(int index);

    int getAddress();

    default boolean ansSupported() {
        return true;
    }

    boolean isTemp();

    void setTemp(boolean temp);

    ObjectData copy();
}
