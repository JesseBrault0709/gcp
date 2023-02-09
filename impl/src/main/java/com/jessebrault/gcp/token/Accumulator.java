package com.jessebrault.gcp.token;

import java.util.List;
import java.util.regex.Pattern;

final class Accumulator {

    private static final Pattern newline = Pattern.compile("([\n\r])");

    private final List<TokenAndInputState> tokensAndInputStates;

    private int currentInputIndex = 0;
    private int line = 1;
    private int col = 1;

    public Accumulator(List<TokenAndInputState> tokensAndInputStates, int startingInputIndex) {
        this.tokensAndInputStates = tokensAndInputStates;
        this.currentInputIndex = startingInputIndex;
    }

    public void accumulate(Token.Type type, CharSequence text, Tokenizer.State inputState) {
        final var endIndex = this.currentInputIndex + text.length();
        this.tokensAndInputStates.add(
                new TokenAndInputState(
                        new Token(
                                type,
                                text,
                                this.currentInputIndex,
                                endIndex,
                                this.line,
                                this.col
                        ),
                        inputState
                )
        );
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
