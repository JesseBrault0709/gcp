package com.jessebrault.gcp.tokenizer;

import com.jessebrault.fsm.function.FunctionFsm;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class TokenizerImpl implements Tokenizer {

    private CharSequence input;
    private int currentOffset;
    private int endOffset;

    private int currentTokenIndex;
    private List<Token> tokens;
    private FunctionFsm<CharSequence, State, FsmOutput> fsm;

    private void pullTokens() {
        if (this.currentOffset < this.endOffset) {
            final var output = this.fsm.apply(this.input.subSequence(this.currentOffset, this.endOffset));
            if (output == null) {
                throw new IllegalStateException();
            }
            this.currentOffset += output.entire().length();
        }
    }

    @Override
    public void start(CharSequence input, int startOffset, int endOffset, State initialState) {
        this.input = input;
        this.currentOffset = startOffset;
        this.endOffset = endOffset;
        this.currentTokenIndex = 0;
        this.tokens = new ArrayList<>();
        this.fsm = TokenizerFsm.get(new Accumulator(this.tokens), initialState);
        this.pullTokens();
    }

    @Override
    public CharSequence getCurrentInput() {
        return Objects.requireNonNull(this.input);
    }

    @Override
    public State getCurrentState() {
        return this.fsm.getCurrentState();
    }

    @Override
    public void advance() {
        this.currentTokenIndex++;
        this.pullTokens();
    }

    @Override
    public @Nullable Token getCurrent() {
        if (this.currentTokenIndex < this.tokens.size()) {
            return this.tokens.get(this.currentTokenIndex);
        } else {
            return null;
        }
    }

    @Override
    public boolean peekCurrent(Token.Type type) {
        if (this.currentTokenIndex < this.tokens.size()) {
            return this.tokens.get(this.currentTokenIndex).getType() == type;
        } else {
            return false;
        }
    }

    @Override
    public boolean peekSecond(Token.Type type) {
        final var secondIndex = this.currentTokenIndex + 1;
        if (secondIndex < this.tokens.size()) {
            return this.tokens.get(secondIndex).getType() == type;
        } else {
            this.pullTokens();
            if (secondIndex < this.tokens.size()) {
                return this.tokens.get(secondIndex).getType() == type;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean peekInfinite(Token.Type type, Collection<Token.Type> failOn) {
        int peekIndex = this.currentTokenIndex;
        while (true) {
            if (peekIndex < this.tokens.size()) {
                final var peeked = this.tokens.get(peekIndex);
                if (peeked.getType() == type) {
                    return true;
                } else if (peeked.getType().isAnyOf(failOn)) {
                    return false;
                }
                peekIndex++;
            } else {
                this.pullTokens();
                if (peekIndex >= this.tokens.size()) {
                    break;
                }
            }
        }
        return false;
    }

}
