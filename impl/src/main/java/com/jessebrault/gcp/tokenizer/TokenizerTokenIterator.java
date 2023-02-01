package com.jessebrault.gcp.tokenizer;

import java.util.ConcurrentModificationException;

public final class TokenizerTokenIterator extends AbstractTokenIterator {

    private final Tokenizer tokenizer;
    private final CharSequence initialInput;

    public TokenizerTokenIterator(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.initialInput = this.tokenizer.getCurrentInput();
    }

    private void checkInput() {
        if (!this.initialInput.equals(this.tokenizer.getCurrentInput())) {
            throw new ConcurrentModificationException(
                    "this.initialInput does not equal this.tokenizer.getCurrentInput()"
            );
        }
    }

    @Override
    protected boolean underlyingHasNext() {
        this.checkInput();
        return this.tokenizer.hasNext();
    }

    @Override
    protected Token underlyingNext() {
        this.checkInput();
        return this.tokenizer.next();
    }

}
