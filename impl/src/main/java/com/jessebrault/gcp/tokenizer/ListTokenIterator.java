package com.jessebrault.gcp.tokenizer;

import java.util.ConcurrentModificationException;
import java.util.List;

public final class ListTokenIterator extends AbstractTokenIterator {

    private final List<Token> underlying;
    private final int startSize;
    private int currentIndex;

    public ListTokenIterator(List<Token> underlying) {
        this.underlying = underlying;
        this.startSize = this.underlying.size();
    }

    private void checkSizes() {
        if (this.startSize != this.underlying.size()) {
            throw new ConcurrentModificationException("this.startSize is not equal to this.underlying.size()");
        }
    }

    @Override
    protected boolean underlyingHasNext() {
        this.checkSizes();
        return this.currentIndex < this.underlying.size();
    }

    @Override
    protected Token underlyingNext() {
        this.checkSizes();
        if (this.currentIndex >= this.underlying.size()) {
            throw new NullPointerException("this.hasNext() is false");
        }
        final var next = this.underlying.get(this.currentIndex);
        currentIndex++;
        return next;
    }

    @Override
    public String toString() {
        return String.format(
                "ListTokenIterator(underlying: %s, startSize: %s, currentIndex: %d, super: %s)",
                this.underlying,
                this.startSize,
                this.currentIndex,
                super.toString()
        );
    }

}
