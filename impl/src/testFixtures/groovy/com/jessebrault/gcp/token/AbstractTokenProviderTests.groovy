package com.jessebrault.gcp.token

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

abstract class AbstractTokenProviderTests {

    private static List<Token> getCompleteExample() {
        [
                new Token(Token.Type.TEXT, 'Hello, World!', 0, 13, 1, 1),
                new Token(Token.Type.COMPONENT_START, '<', 13, 14, 1, 14),
                new Token(Token.Type.CLASS_NAME, 'Test', 14, 18, 1, 15),
                new Token(Token.Type.WHITESPACE, ' ', 18, 19, 1, 19),
                new Token(Token.Type.FORWARD_SLASH, '/', 19, 20, 1, 20),
                new Token(Token.Type.COMPONENT_END, '>', 20, 21, 1, 21)
        ]
    }

    /**
     * @return A TokenProvider which will produce the given tokens in the correct order.
     */
    protected abstract TokenProvider getTokenProvider(List<Token> tokens);

    @Test
    void withOneTokenCurrentNotNull() {
        def t0 = new Token(Token.Type.TEXT, 'Hello, World!', 0, 13, 1, 1)
        def tp = this.getTokenProvider([t0])
        def o0 = tp.getCurrent()
        assertEquals(t0, o0)
    }

    @Test
    void withMultipleTokensAdvanceAndCurrentIsCorrect() {
        def tokens = getCompleteExample()

        def tp = this.getTokenProvider(tokens)

        def check = { Token expected, boolean advance = true ->
            assertEquals(expected, tp.getCurrent())
            if (advance) {
                tp.advance()
            }
        }

        tokens.each(check)
        assertNull(tp.getCurrent())
    }

    @Test
    void withOneTokenPeekCurrentIsCorrect() {
        def t0 = new Token(Token.Type.TEXT, 'Hello, World!', 0, 13, 1, 1)
        def tp = this.getTokenProvider([t0])
        assertTrue(tp.peekCurrent(Token.Type.TEXT))
    }

    @Test
    void withMultipleTokensPeekSecondIsCorrect() {
        def tokens = getCompleteExample()
        def tp = this.getTokenProvider(tokens)
        assertTrue(tp.peekSecond(Token.Type.COMPONENT_START))
    }

    @Test
    void withMultipleTokensPeekSecondThenAdvance() {
        def tokens = getCompleteExample()
        def tp = this.getTokenProvider(tokens)
        assertTrue(tp.peekCurrent(Token.Type.TEXT))
        assertTrue(tp.peekSecond(Token.Type.COMPONENT_START))
        assertEquals(tokens[0], tp.getCurrent())
        tp.advance()
        assertEquals(tokens[1], tp.getCurrent())
    }

    @Test
    void peekInfiniteAndAdvancing() {
        def tokens = getCompleteExample()

        def tp = this.getTokenProvider(tokens)

        def check = { Token expectedCurrent, boolean expectedInfinite = true ->
            assertEquals(expectedInfinite, tp.peekInfinite(Token.Type.FORWARD_SLASH, [Token.Type.COMPONENT_END]))
            assertEquals(expectedCurrent, tp.getCurrent())
            tp.advance()
        }

        tokens.subList(0, 5).each check // do first five
        check(tokens[5], false) // last should be false
    }

}
