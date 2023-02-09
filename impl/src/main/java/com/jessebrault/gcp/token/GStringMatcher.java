package com.jessebrault.gcp.token;

import com.jessebrault.fsm.stackfunction.StackFunctionFsm;
import com.jessebrault.fsm.stackfunction.StackFunctionFsmBuilder;
import com.jessebrault.fsm.stackfunction.StackFunctionFsmBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * TODO: either make another (incomplete) matcher, or adjust this one
 */
final class GStringMatcher implements FsmFunction {

    private static final class GStringMatcherOutput implements FsmOutput {

        private final CharSequence entire;
        private final CharSequence openingDoubleQuote;
        private final CharSequence contents;
        private final CharSequence closingDoubleQuote;

        public GStringMatcherOutput(
                CharSequence entire,
                CharSequence openingDoubleQuote,
                CharSequence contents,
                CharSequence closingDoubleQuote
        ) {
            this.entire = entire;
            this.openingDoubleQuote = openingDoubleQuote;
            this.contents = contents;
            this.closingDoubleQuote = closingDoubleQuote;
        }

        @Override
        public CharSequence entire() {
            return this.entire;
        }

        @Override
        public CharSequence part(int index) {
            return switch (index) {
                case 1 -> this.openingDoubleQuote;
                case 2 -> this.contents;
                case 3 -> this.closingDoubleQuote;
                default -> throw new IllegalArgumentException();
            };
        }

    }

    private static final PatternMatcher text = new PatternMatcher(
            Pattern.compile("^(?:[\\w\\W&&[^$\\\\\"\\n\\r]]|\\\\[\"nrbfst\\\\u]|\\$(?!\\{|[\\w$]+(?:\\.[\\w$]+)*))+")
    );
    private static final DollarScriptletMatcher dollarScriptlet = new DollarScriptletMatcher();
    private static final PatternMatcher doubleQuote = new PatternMatcher(
            Pattern.compile("^\"")
    );

    private enum State {
        START, CONTENTS, DONE
    }

    private static StackFunctionFsmBuilder<CharSequence, State, FsmOutput> getFsmBuilder() {
        return new StackFunctionFsmBuilderImpl<>();
    }

    private static StackFunctionFsm<CharSequence, State, FsmOutput> getFsm(StringBuilder acc) {
        return getFsmBuilder()
                .setInitialState(State.START)
                .whileIn(State.START, sc -> {
                    sc.on(doubleQuote).shiftTo(State.CONTENTS).exec(o -> {
                        acc.append(o.entire());
                    });
                    sc.onNoMatch().exec(input -> {
                        throw new RuntimeException("Should not have gotten here.");
                    });
                })
                .whileIn(State.CONTENTS, sc -> {
                    sc.on(text).exec(o -> {
                        acc.append(o.entire());
                    });
                    sc.on(dollarScriptlet).exec(o -> {
                        acc.append(o.entire());
                    });
                    sc.on(doubleQuote).shiftTo(State.DONE).exec(o -> {
                        acc.append(o.entire());
                    });
                })
                .build();
    }

    @Override
    public FsmOutput apply(final CharSequence s) {
        final var acc = new StringBuilder();
        final var fsm = getFsm(acc);

        CharSequence remaining = s;

        // Look-ahead
        if (!String.valueOf(remaining.charAt(0)).equals("\"")) {
            return null;
        }

        while (remaining.length() > 0) {
            final var output = fsm.apply(remaining);
            if (output == null) {
                throw new IllegalStateException("output is null");
            } else if (fsm.getCurrentState() == State.DONE) {
                final var entire = acc.toString();
                return new GStringMatcherOutput(
                        entire,
                        entire.substring(0, 1),
                        entire.substring(1, entire.length() - 1),
                        entire.substring(entire.length() - 1)
                );
            } else {
                remaining = remaining.subSequence(output.entire().length(), remaining.length());
            }
        }
        // invalid or incomplete
        return null;
    }

}
