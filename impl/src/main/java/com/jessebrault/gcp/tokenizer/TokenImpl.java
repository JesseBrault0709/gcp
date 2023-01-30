package com.jessebrault.gcp.tokenizer;

public final class TokenImpl implements Token {

    private final Type type;
    private final CharSequence text;
    private final int startIndex;
    private final int endIndex;
    private final int line;
    private final int col;

    public TokenImpl(Type type, CharSequence text, int startIndex, int endIndex, int line, int col) {
        this.type = type;
        this.text = text;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.line = line;
        this.col = col;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public CharSequence getText() {
        return text;
    }

    @Override
    public int getStartIndex() {
        return this.startIndex;
    }

    @Override
    public int getEndIndex() {
        return this.endIndex;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return String.format(
                "Token(type: %s, text: %s, startIndex: %d, endIndex: %d, line: %d, col: %d)",
                this.type,
                this.text,
                this.startIndex,
                this.endIndex,
                this.line,
                this.col
        );
    }

}
