package com.jessebrault.gcp.token;

import java.util.Collection;
import java.util.Objects;

public final class Token {

    public enum Type {
        TEXT,

        DOLLAR,
        GROOVY_REFERENCE,
        CURLY_OPEN,
        SCRIPTLET,
        CURLY_CLOSE,
        BLOCK_SCRIPTLET_OPEN,
        EXPRESSION_SCRIPTLET_OPEN,
        SCRIPTLET_CLOSE,

        CLASS_NAME,
        PACKAGE_NAME,
        DOT,

        WHITESPACE,

        KEY,
        EQUALS,

        DOUBLE_QUOTE,
        STRING,
        SINGLE_QUOTE,

        COMPONENT_START,
        FORWARD_SLASH,
        COMPONENT_END,

        INVALID
        ;

        public boolean isAnyOf(Collection<Type> types) {
            return types.contains(this);
        }

    }

    private static boolean hasLineAndCol(Token token) {
        return token.line >= 1 && token.col >= 1;
    }

    private final Type type;
    private final CharSequence text;
    private final int startIndex;
    private final int endIndex;
    private final int line;
    private final int col;

    public Token(Type type, CharSequence text, int startIndex, int endIndex, int line, int col) {
        this.type = type;
        this.text = text;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.line = line;
        this.col = col;
    }

    public Token(Type type, CharSequence text, int startIndex, int endIndex) {
        this(type, text, startIndex, endIndex, -1, -1);
    }

    public Type getType() {
        return this.type;
    }

    public CharSequence getText() {
        return this.text;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public String toString() {
        final var b = new StringBuilder("Token(type: ")
                .append(this.type)
                .append(", text: ")
                .append(this.text)
                .append(", startIndex: ")
                .append(this.startIndex)
                .append(", endIndex: ")
                .append(this.endIndex);
        if (hasLineAndCol(this)) {
            b.append(", line: ")
                    .append(this.line)
                    .append(", col: ")
                    .append(this.col);
        }
        b.append(")");
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        } else {
            final var t = (Token) o;
            final var result = this.type == t.getType() &&
                    this.text.equals(t.getText()) &&
                    this.startIndex == t.getStartIndex() &&
                    this.endIndex == t.getEndIndex();
            if (hasLineAndCol(this) && hasLineAndCol(t)) {
                return result &&
                        this.line == t.getLine() &&
                        this.col == t.getCol();
            } else {
                return result;
            }
        }
    }

    @Override
    public int hashCode() {
        if (hasLineAndCol(this)) {
            return Objects.hash(this.type, this.text, this.startIndex, this.endIndex, this.line, this.col);
        } else {
            return Objects.hash(this.type, this.text, this.startIndex, this.endIndex);
        }
    }

}
