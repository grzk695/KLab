package com.klab.interpreter.profiling.model;

public class ProfilingData<T> {
    private T subject;
    private long time = 0L;
    private long count = 0L;

    public ProfilingData(T t) {
        this.subject = t;
    }

    public void addTime(long time) {
        this.time += time;
    }

    public void addCount(long count) {
        this.count += count;
    }

    public T getSubject() {
        return subject;
    }

    public void setSubject(T subject) {
        this.subject = subject;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTimeSeconds() {
        return time / 1_000_000_000.0;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return time + " (" + count + ")";
    }
}
