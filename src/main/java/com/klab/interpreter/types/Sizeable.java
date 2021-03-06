package com.klab.interpreter.types;

public interface Sizeable {
    int getRows();

    int getColumns();

    long getCells();

    default long length() {
        return Math.max(getRows(), getColumns());
    }

    default boolean isScalar() {
        return getRows() == 1 && getColumns() == 1;
    }

    default boolean isColumn() {
        return getColumns() == 1;
    }

    default boolean isRow() {
        return getRows() == 1;
    }

    default boolean isEmpty() {
        return getCells() == 0;
    }


    default boolean isVector() {
        return getColumns() == 1 || getRows() == 1;
    }

    default String formatToString() {
        return String.format("<%d x %d>", getRows(), getColumns());
    }
}
