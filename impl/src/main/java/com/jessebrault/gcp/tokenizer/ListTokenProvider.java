package com.jessebrault.gcp.tokenizer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ListTokenProvider implements TokenProvider {

    private final List<Token> tokens;
    private int currentIndex;

    public ListTokenProvider(List<? extends Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
    }

    @Override
    public @Nullable Token getCurrent() {
        if (this.currentIndex < this.tokens.size()) {
            return this.tokens.get(this.currentIndex);
        } else {
            return null;
        }
    }

    @Override
    public boolean peekCurrent(Token.Type type) {
        final var current = this.getCurrent();
        return current != null && current.getType() == type;
    }

    @Override
    public boolean peekSecond(Token.Type type) {
        if (this.currentIndex + 1 < this.tokens.size()) {
            final var second = this.tokens.get(this.currentIndex + 1);
            return second.getType() == type;
        } else {
            return false;
        }
    }

    @Override
    public boolean peekInfinite(Token.Type type, Collection<Token.Type> failOn) {
        int peekIndex = this.currentIndex;
        while (peekIndex < this.tokens.size()) {
            final var peeked = this.tokens.get(peekIndex);
            if (peeked.getType() == type) {
                return true;
            } else if (peeked.getType().isAnyOf(failOn)) {
                return false;
            } else {
                peekIndex++;
            }
        }
        return false;
    }

    @Override
    public void advance() {
        this.currentIndex++;
    }

}
