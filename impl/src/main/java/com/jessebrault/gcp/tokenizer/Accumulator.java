package com.jessebrault.gcp.tokenizer;

import java.util.List;
import java.util.regex.Pattern;

final class Accumulator {

    private static final Pattern newline = Pattern.compile("([\n\r])");

    private final List<Token> tokens;
    private int currentInputIndex = 0;
    private int line = 1;
    private int col = 1;

    public Accumulator(List<Token> tokenQueue) {
        this.tokens = tokenQueue;
    }

    public void accumulate(Token.Type type, CharSequence text) {
        final var endIndex = this.currentInputIndex + text.length();
        this.tokens.add(new TokenImpl(
                type,
                text,
                this.currentInputIndex,
                endIndex,
                this.line,
                this.col
        ));
        this.currentInputIndex = endIndex;
        final var m = newline.matcher(text);
        if (m.find()) {
            this.line += m.groupCount();
            this.col = 1;
        } else {
            this.col += text.length();
        }
    }

}
