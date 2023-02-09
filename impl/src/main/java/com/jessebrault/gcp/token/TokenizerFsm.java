package com.jessebrault.gcp.token;

import com.jessebrault.fsm.function.FunctionFsm;
import com.jessebrault.fsm.function.FunctionFsmBuilder;
import com.jessebrault.fsm.function.FunctionFsmBuilderImpl;
import com.jessebrault.gcp.token.Tokenizer.State;

import static com.jessebrault.gcp.token.Token.Type.*;

import java.util.regex.Pattern;

final class TokenizerFsm {

    /**
     * Text
     */
    private static final FsmFunction text = new PatternMatcher(
            Pattern.compile("^(?:[\\w\\W&&[^<$]]|<(?!%|/?\\p{Lu}|/?[\\p{L}0-9_$]+(?:\\.[\\p{L}0-9_$]+)+)|\\$(?![\\w$]+(?:\\.[\\w$]+)*))+")
    );

    /**
     * Gsp dollar reference and scriptlets, also used as component values
     */
    private static final FsmFunction dollarReference = new PatternMatcher(
            Pattern.compile("^(\\$)([\\w$]+(?:\\.[\\w$]+)*)")
    );
    private static final FsmFunction dollarScriptlet = new DollarScriptletMatcher();
    private static final FsmFunction blockScriptlet = new PatternMatcher(
            Pattern.compile("^(<%)(.*?)(%>)")
    );
    private static final FsmFunction expressionScriptlet = new PatternMatcher(
            Pattern.compile("^(<%=)(.*?)(%>)")
    );

    /**
     * Component starts
     */
    private static final FsmFunction openingComponentStart = new PatternMatcher(
            Pattern.compile("^<(?=\\p{Lu}|[\\p{L}0-9_$]+(?:\\.[\\p{L}0-9_$]+)+)")
    );
    private static final FsmFunction closingComponentStart = new PatternMatcher(
            Pattern.compile("^<(?=/\\p{Lu}|[\\p{L}0-9_$]+(?:\\.[\\p{L}0-9_$]+)+)")
    );

    /**
     * Component names
     */
    private static final FsmFunction className = new PatternMatcher(
            Pattern.compile("^\\p{Lu}[\\p{L}0-9_$]*")
    );
    private static final FsmFunction packageName = new PatternMatcher(
            Pattern.compile("^[\\p{L}0-9_$]+(?=\\.)")
    );
    private static final FsmFunction dot = new PatternMatcher(
            Pattern.compile("^\\.")
    );

    /**
     * Whitespace
     */
    private static final FsmFunction whitespace = new PatternMatcher(Pattern.compile("^\\s+"));

    /**
     * Keys and values
     */
    private static final FsmFunction key = new PatternMatcher(
            Pattern.compile("^[\\p{L}0-9_$]+")
    );
    private static final FsmFunction equals = new PatternMatcher(Pattern.compile("^="));
    private static final FsmFunction singleQuoteString = new PatternMatcher(
            Pattern.compile("^(')((?:[\\w\\W&&[^\\\\'\\n\\r]]|\\\\['nrbfst\\\\u])*)(')")
    );
    private static final FsmFunction gString = new GStringMatcher();

    /**
     * Component ends
     */
    private static final FsmFunction forwardSlash = new PatternMatcher(Pattern.compile("^/"));
    private static final FsmFunction componentEnd = new PatternMatcher(Pattern.compile("^>"));

    private static final class InvalidTokenOutput implements FsmOutput {

        private final CharSequence invalidToken;

        public InvalidTokenOutput(CharSequence invalidToken) {
            this.invalidToken = invalidToken;
        }

        @Override
        public CharSequence entire() {
            return this.invalidToken;
        }

        @Override
        public CharSequence part(int index) {
            throw new UnsupportedOperationException();
        }

    }

    private static FunctionFsmBuilder<CharSequence, State, FsmOutput> getFsmBuilder() {
        return new FunctionFsmBuilderImpl<>();
    }

    public static FunctionFsm<CharSequence, State, FsmOutput> get(Accumulator acc, State state) {
        return getFsmBuilder()
                .setInitialState(state)
                .whileIn(State.TEXT, sc -> {
                    sc.on(text).exec(o -> {
                        acc.accumulate(TEXT, o.entire(), State.TEXT);
                    });
                    sc.on(dollarReference).exec(o -> {
                        acc.accumulate(DOLLAR, o.part(1), State.TEXT);
                        acc.accumulate(GROOVY_REFERENCE, o.part(2), State.TEXT);
                    });
                    sc.on(dollarScriptlet).exec(o -> {
                        acc.accumulate(DOLLAR, o.part(1), State.TEXT);
                        acc.accumulate(CURLY_OPEN, o.part(2), State.TEXT);
                        acc.accumulate(SCRIPTLET, o.part(3), State.TEXT);
                        acc.accumulate(CURLY_CLOSE, o.part(4), State.TEXT);
                    });
                    sc.on(blockScriptlet).exec(o -> {
                        acc.accumulate(BLOCK_SCRIPTLET_OPEN, o.part(1), State.TEXT);
                        acc.accumulate(SCRIPTLET, o.part(2), State.TEXT);
                        acc.accumulate(SCRIPTLET_CLOSE, o.part(3), State.TEXT);
                    });
                    sc.on(expressionScriptlet).exec(o -> {
                        acc.accumulate(EXPRESSION_SCRIPTLET_OPEN, o.part(1), State.TEXT);
                        acc.accumulate(SCRIPTLET, o.part(2), State.TEXT);
                        acc.accumulate(SCRIPTLET_CLOSE, o.part(3), State.TEXT);
                    });
                    sc.on(openingComponentStart).shiftTo(State.COMPONENT_NAME).exec(o ->
                        acc.accumulate(COMPONENT_START, o.entire(), State.TEXT)
                    );
                    sc.on(closingComponentStart).shiftTo(State.COMPONENT_NAME).exec(o -> {
                        acc.accumulate(COMPONENT_START, o.entire(), State.TEXT);
                    });
                    sc.onNoMatch().exec(input -> {
                        throw new IllegalArgumentException("Ideally should not get here.");
                    });
                })
                .whileIn(State.COMPONENT_NAME, sc -> {
                    sc.on(packageName).exec(o -> {
                       acc.accumulate(PACKAGE_NAME, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.on(dot).exec(o -> {
                        acc.accumulate(DOT, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.on(className).exec(o -> {
                        acc.accumulate(CLASS_NAME, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.on(forwardSlash).exec(o -> {
                        acc.accumulate(FORWARD_SLASH, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.on(componentEnd).shiftTo(State.TEXT).exec(o -> {
                       acc.accumulate(COMPONENT_END, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.on(whitespace).shiftTo(State.COMPONENT_KEYS_AND_VALUES).exec(o -> {
                        acc.accumulate(WHITESPACE, o.entire(), State.COMPONENT_NAME);
                    });
                    sc.onNoMatch()
                            .shiftTo(State.TEXT)
                            .instead(input -> new InvalidTokenOutput(input.subSequence(0, 1)))
                            .exec(input -> {
                                acc.accumulate(INVALID, input.subSequence(0, 1), State.COMPONENT_NAME);
                            });
                })
                .whileIn(State.COMPONENT_KEYS_AND_VALUES, sc -> {
                    sc.on(componentEnd).shiftTo(State.TEXT).exec(o -> {
                        acc.accumulate(COMPONENT_END, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(whitespace).exec(o -> {
                        acc.accumulate(WHITESPACE, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(key).exec(o -> {
                        acc.accumulate(KEY, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(equals).exec(o -> {
                       acc.accumulate(EQUALS, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(gString).exec(o -> {
                        acc.accumulate(DOUBLE_QUOTE, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(STRING, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(DOUBLE_QUOTE, o.part(3), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(singleQuoteString).exec(o -> {
                       acc.accumulate(SINGLE_QUOTE, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                       acc.accumulate(STRING, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                       acc.accumulate(SINGLE_QUOTE, o.part(3), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(dollarReference).exec(o -> {
                        acc.accumulate(DOLLAR, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(GROOVY_REFERENCE, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(dollarScriptlet).exec(o -> {
                        acc.accumulate(DOLLAR, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(CURLY_OPEN, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(SCRIPTLET, o.part(3), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(CURLY_CLOSE, o.part(4), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(blockScriptlet).exec(o -> {
                        acc.accumulate(BLOCK_SCRIPTLET_OPEN, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(SCRIPTLET, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(SCRIPTLET_CLOSE, o.part(3), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(expressionScriptlet).exec(o -> {
                        acc.accumulate(EXPRESSION_SCRIPTLET_OPEN, o.part(1), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(SCRIPTLET, o.part(2), State.COMPONENT_KEYS_AND_VALUES);
                        acc.accumulate(SCRIPTLET_CLOSE, o.part(3), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(forwardSlash).exec(o -> {
                        acc.accumulate(FORWARD_SLASH, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.on(componentEnd).shiftTo(State.TEXT).exec(o -> {
                        acc.accumulate(COMPONENT_END, o.entire(), State.COMPONENT_KEYS_AND_VALUES);
                    });
                    sc.onNoMatch()
                            .shiftTo(State.TEXT)
                            .instead(input -> new InvalidTokenOutput(input.subSequence(0, 1)))
                            .exec(input -> {
                                acc.accumulate(INVALID, input.subSequence(0, 1), State.COMPONENT_KEYS_AND_VALUES);
                            });
                })
                .build();
    }

}
