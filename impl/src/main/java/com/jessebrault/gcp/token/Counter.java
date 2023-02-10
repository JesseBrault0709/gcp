package com.jessebrault.gcp.token;

final class Counter {

    private int count = 0;

    public void increment() {
        this.count++;
    }

    public void decrement() {
        this.count--;
    }

    public boolean isZero() {
        return this.count == 0;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public String toString() {
        return "Counter(" + this.count + ")";
    }

}
