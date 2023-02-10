package com.jessebrault.gcp.token;

import com.jessebrault.fsm.stackfunction.StackFunctionFsm;
import com.jessebrault.fsm.stackfunction.StackFunctionFsmBuilder;
import com.jessebrault.fsm.stackfunction.StackFunctionFsmBuilderImpl;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

final class DollarScriptletMatcher implements FsmFunction {

    public sealed interface Output extends FsmOutput permits Failure, Success {}

    public static final class Failure implements Output {

        private final CharSequence entire;
        private final CharSequence dollar;
        private final CharSequence startCurly;
        private final CharSequence scriptlet;

        public Failure(CharSequence entire, CharSequence dollar, CharSequence startCurly, CharSequence scriptlet) {
            this.entire = entire;
            this.dollar = dollar;
            this.startCurly = startCurly;
            this.scriptlet = scriptlet;
        }

        @Override
        public CharSequence entire() {
            return this.entire;
        }

        @Override
        public CharSequence part(int index) {
            return switch (index) {
                case 1 -> this.dollar;
                case 2 -> this.startCurly;
                case 3 -> this.scriptlet;
                default -> throw new IllegalArgumentException();
            };
        }

        @Override
        public String toString() {
            return "Failure(" + this.entire + ")";
        }

    }

    public static final class Success implements Output {

        private final CharSequence entire;
        private final CharSequence dollar;
        private final CharSequence startCurly;
        private final CharSequence scriptlet;
        private final CharSequence endCurly;

        public Success(
                CharSequence entire,
                CharSequence dollar,
                CharSequence startCurly,
                CharSequence scriptlet,
                CharSequence endCurly
        ) {
            this.entire = entire;
            this.dollar = dollar;
            this.startCurly = startCurly;
            this.scriptlet = scriptlet;
            this.endCurly = endCurly;
        }

        @Override
        public CharSequence entire() {
            return this.entire;
        }

        @Override
        public CharSequence part(int index) {
            return switch (index) {
                case 1 -> this.dollar;
                case 2 -> this.startCurly;
                case 3 -> this.scriptlet;
                case 4 -> this.endCurly;
                default -> throw new IllegalArgumentException();
            };
        }

        @Override
        public String toString() {
            return "Success(" + this.entire + ")";
        }

    }

    private static final PatternMatcher dollar = new PatternMatcher("\\$");
    private static final PatternMatcher curlyOpen = new PatternMatcher("\\{");
    private static final PatternMatcher curlyClose = new PatternMatcher("}");
    private static final PatternMatcher doubleQuote = new PatternMatcher("\"");
    private static final PatternMatcher singleQuote = new PatternMatcher("'");
    private static final PatternMatcher escape = new PatternMatcher("\\\\");

    private enum State {
        START_DOLLAR,
        START_CURLY,

        NO_STRING,
        G_STRING,
        SINGLE_QUOTE_STRING,

        ESCAPE,
        CHECK_CURLY,

        DONE,
        DONE_NO_MATCH
    }

    private static StackFunctionFsmBuilder<CharSequence, State, FsmOutput> getFsmBuilder() {
        return new StackFunctionFsmBuilderImpl<>();
    }

    private static StackFunctionFsm<CharSequence, State, FsmOutput> getFsm() {
        final Deque<Counter> counterStack = new LinkedList<>();

        final Supplier<Counter> currentCounterSupplier = () -> {
            final var currentCounter = counterStack.peek();
            if (currentCounter == null) {
                throw new IllegalStateException("currentCounter is null");
            }
            return currentCounter;
        };

        final var nonFinalCurlyClose = curlyClose.andThen(o -> {
            if (o == null) {
                return null;
            } else if (currentCounterSupplier.get().getCount() > 1) {
                return o;
            } else {
                return null;
            }
        });

        final var finalCurlyClose = curlyClose.andThen(o -> {
            if (o == null) {
                return null;
            } else if (currentCounterSupplier.get().getCount() == 1) {
                return o;
            } else if (currentCounterSupplier.get().getCount() < 1) {
                throw new IllegalStateException();
            } else {
                return null;
            }
        });

        return getFsmBuilder()
                .setInitialState(State.START_DOLLAR)

                .whileIn(State.START_DOLLAR, sc -> {
                    sc.on(dollar).shiftTo(State.START_CURLY);
                    sc.onNoMatch().shiftTo(State.DONE_NO_MATCH);
                })

                .whileIn(State.START_CURLY, sc -> {
                    sc.on(curlyOpen).pushStates(List.of(State.DONE, State.NO_STRING)).exec(o -> {
                        counterStack.push(new Counter()); // initial
                        currentCounterSupplier.get().increment(); // == 1
                    });
                    sc.onNoMatch().shiftTo(State.DONE_NO_MATCH);
                })

                .whileIn(State.NO_STRING, sc -> {
                    sc.on(curlyOpen).exec(o -> currentCounterSupplier.get().increment());
                    sc.on(nonFinalCurlyClose).exec(o -> currentCounterSupplier.get().decrement());
                    sc.on(finalCurlyClose).popState().exec(o -> counterStack.pop());
                    sc.on(doubleQuote).pushState(State.G_STRING);
                    sc.on(singleQuote).pushState(State.SINGLE_QUOTE_STRING);
                    sc.onNoMatch().instead(InputFsmOutput::new);
                })

                .whileIn(State.G_STRING, sc -> {
                    sc.on(escape).pushState(State.ESCAPE);
                    sc.on(dollar).pushState(State.CHECK_CURLY);
                    sc.on(doubleQuote).popState();
                    sc.onNoMatch().instead(InputFsmOutput::new);
                })

                .whileIn(State.ESCAPE, sc -> {
                    sc.onNoMatch().popState().instead(InputFsmOutput::new);
                })

                .whileIn(State.CHECK_CURLY, sc -> {
                    sc.on(curlyOpen).shiftTo(State.NO_STRING).exec(o -> {
                        counterStack.push(new Counter());
                        currentCounterSupplier.get().increment();
                    });
                    sc.onNoMatch()
                            .popState()
                            .instead(InputFsmOutput::new);
                })

                .whileIn(State.SINGLE_QUOTE_STRING, sc -> {
                    sc.on(escape).pushState(State.ESCAPE);
                    sc.on(singleQuote).popState();
                    sc.onNoMatch().instead(InputFsmOutput::new);
                })

                .build();
    }

    private static final class CharSequenceIterator implements Iterator<String> {

        private final CharSequence input;
        private int cur;

        public CharSequenceIterator(CharSequence input) {
            this.input = input;
        }

        @Override
        public boolean hasNext() {
            return this.cur < input.length();
        }

        @Override
        public String next() {
            final var c = String.valueOf(input.charAt(this.cur));
            this.cur++;
            return c;
        }

    }

    @Override
    public FsmOutput apply(CharSequence s) {
        final var fsm = getFsm();
        final var iterator = new CharSequenceIterator(s);
        final var acc = new StringBuilder();

        while (iterator.hasNext()) {
            final var c = iterator.next();
            final var o = fsm.apply(c);
            if (o != null) {
                acc.append(o.entire());
            }

            switch (fsm.getCurrentState()) {
                case DONE -> {
                    final var entire = acc.toString();
                    return new Success(
                            entire,
                            entire.substring(0, 1),
                            entire.substring(1, 2),
                            entire.substring(2, entire.length() - 1),
                            entire.substring(entire.length() - 1)
                    );
                }
                case DONE_NO_MATCH -> {
                    return null;
                }
            }
        }

        // ran out of chars
        final var entire = acc.toString();
        if (entire.length() >= 2) {
            return new Failure(
                    entire,
                    entire.substring(0, 1),
                    entire.substring(1, 2),
                    entire.substring(2)
            );
        } else if (entire.length() == 0){
            return null;
        } else {
            throw new IllegalStateException("Should not get here.");
        }
    }

}
