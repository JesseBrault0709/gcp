package com.jessebrault.gcp.tokenizer;

import java.util.LinkedList;
import java.util.Queue;

public interface Tokenizer extends TokenProvider {

    enum State {
        TEXT,
        COMPONENT_NAME,
        COMPONENT_KEYS_AND_VALUES
    }

    void start(CharSequence input, int startOffset, int endOffset, State initialState);

    CharSequence getCurrentInput();

    State getCurrentState();

    default Queue<Token> tokenizeAll(CharSequence input, State initialState) {
        this.start(input, 0, input.length(), initialState);
        final Queue<Token> tokens = new LinkedList<>();
        Token current;
        while ((current = this.getCurrent()) != null) {
            tokens.add(current);
            this.advance();
        }
        return tokens;
    }

}
