package com.jessebrault.gcp.token;

final class TokenAndInputState {

    private final Token token;
    private final Tokenizer.State inputState;

    public TokenAndInputState(Token token, Tokenizer.State inputState) {
        this.token = token;
        this.inputState = inputState;
    }

    public Token getToken() {
        return this.token;
    }

    public Tokenizer.State getInputState() {
        return this.inputState;
    }

}
