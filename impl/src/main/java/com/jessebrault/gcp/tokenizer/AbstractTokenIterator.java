package com.jessebrault.gcp.tokenizer;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.*;

public abstract class AbstractTokenIterator implements TokenIterator {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTokenIterator.class);
    private static final Marker enter = MarkerFactory.getMarker("ENTER");
    private static final Marker exit = MarkerFactory.getMarker("EXIT");

    private final Deque<Token> peekedAlready = new LinkedList<>();

    protected abstract boolean underlyingHasNext();

    protected abstract Token underlyingNext();

    private boolean peekedAlreadyHasNext() {
        return this.peekedAlready.size() > 0;
    }

    private void addFirst(Token token) {
        this.peekedAlready.addFirst(Objects.requireNonNull(token));
    }

    private void addLast(Token token) {
        this.peekedAlready.addLast(Objects.requireNonNull(token));
    }

    @Override
    public final boolean hasNext() {
        logger.trace(enter, "");
        final var result = this.peekedAlreadyHasNext() || this.underlyingHasNext();
        logger.trace(exit, "result: {}", result);
        return result;
    }

    @Override
    public final Token next() {
        logger.trace(enter, "");
        Token result;
        if (this.peekedAlreadyHasNext()) {
            result = this.peekedAlready.remove();
        } else if (this.underlyingHasNext()) {
            result = this.underlyingNext();
        } else {
            throw new NullPointerException("neither peekedAlready nor underlying has next");
        }
        logger.trace(exit, "result: {}", result);
        return result;
    }

    @Override
    public final @Nullable Token peekFirst(Collection<Token.Type> anyOf) {
        logger.trace(enter, "anyOf: {}", anyOf);
        Token result = null;
        if (this.peekedAlreadyHasNext()) {
            final var peeked = this.peekedAlready.peek();
            //noinspection DataFlowIssue // we checked and we don't add nulls (hopefully)
            if (peeked.getType().isAnyOf(anyOf)) {
                result = peeked;
            }
        } else if (this.underlyingHasNext()) {
            final var next = this.underlyingNext();
            if (next.getType().isAnyOf(anyOf)) {
                result = next;
            }
            this.addLast(next);
        }
        logger.trace(exit, "result: {}", result);
        return result;
    }

    @Override
    public final @Nullable Token peekSecond(Collection<Token.Type> anyOf) {
        logger.trace(enter, "anyOf: {}", anyOf);
        Token result = null;
        if (this.peekedAlready.size() >= 2) {
            final var first = this.peekedAlready.remove();
            final var second = this.peekedAlready.remove();
            this.addFirst(second);
            this.addFirst(first);
            if (second.getType().isAnyOf(anyOf)) {
                result = second;
            }
        } else if (this.peekedAlready.size() == 1 && this.underlyingHasNext()) {
            final var second = this.underlyingNext();
            this.addLast(second);
            if (second.getType().isAnyOf(anyOf)) {
                result = second;
            }
        } else if (this.underlyingHasNext()) { // && this.peekedAlready.size == 0 (inferred)
            final var first = this.underlyingNext();
            if (this.underlyingHasNext()) {
                final var second = this.underlyingNext();
                this.addLast(first);
                this.addLast(second);
                if (second.getType().isAnyOf(anyOf)) {
                    result = second;
                }
            } else {
                this.addLast(first);
            }
        }
        logger.trace(exit, "result: {}", result);
        return result;
    }

    @Override
    public final @Nullable Token peekInfinite(Collection<Token.Type> anyOf) {
        logger.trace(enter, "anyOf: {}", anyOf);
        final Queue<Token> tempQueue = new LinkedList<>();
        Token result = null;
        while (this.hasNext()) {
            final var peeked = this.next();
            tempQueue.add(peeked);
            if (peeked.getType().isAnyOf(anyOf)) {
                result = peeked;
                break;
            }
        }
        tempQueue.forEach(this::addLast);
        logger.trace(exit, "result: {}", result);
        return result;
    }

    @Override
    public String toString() {
        return String.format("AbstractTokenIterator(peekedAlready: %s)", this.peekedAlready);
    }

}
