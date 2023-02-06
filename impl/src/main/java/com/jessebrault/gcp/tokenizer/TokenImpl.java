package com.jessebrault.gcp.tokenizer;

import java.util.Objects;

final class TokenImpl implements Token {

    private final Type type;
    private final CharSequence text;
    private final int startIndex;
    private final int endIndex;
    private final int line;
    private final int col;

    public TokenImpl(Type type, CharSequence text, int startIndex, int endIndex, int line, int col) {
        this.type = Objects.requireNonNull(type);
        this.text = Objects.requireNonNull(text);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !Token.class.isAssignableFrom(o.getClass())) {
            return false;
        } else {
            final var t = (Token) o;
            return this.type == t.getType() &&
                    this.text.equals(t.getText()) &&
                    this.startIndex == t.getStartIndex() &&
                    this.endIndex == t.getEndIndex();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.text, this.startIndex, this.endIndex);
    }

}
