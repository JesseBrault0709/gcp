package com.jessebrault.gcp.tokenizer

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class ListTokenIteratorTests {

    @Test
    void oneElement() {
        def t0 = new TokenImpl(Token.Type.TEXT, 'Hello, World!', 0, 13, 1, 1)
        TokenIterator iterator = new ListTokenIterator([t0])
        assertEquals(t0, iterator.peekFirst(Token.Type.TEXT))
        assertTrue(iterator.isFirst(Token.Type.TEXT))
        assertTrue(iterator.hasNext())
        assertEquals(t0, iterator.next())
        assertFalse(iterator.hasNext())
        assertThrows(NullPointerException, () -> iterator.next())
    }

    @Test
    void twoElements() {
        def t0 = new TokenImpl(Token.Type.TEXT, 'Hello, ', 0, 7, 1, 1)
        def t1 = new TokenImpl(Token.Type.TEXT, 'World!', 7, 13, 1, 7)
        TokenIterator iterator = new ListTokenIterator([t0, t1])
        assertEquals(t0, iterator.peekFirst(Token.Type.TEXT))
        assertEquals(t1, iterator.peekSecond(Token.Type.TEXT))
        assertTrue(iterator.isFirst(Token.Type.TEXT))
        assertTrue(iterator.isSecond(Token.Type.TEXT))

        assertTrue(iterator.hasNext())
        assertEquals(t0, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(t1, iterator.next())
        assertFalse(iterator.hasNext())
        assertThrows(NullPointerException, () -> iterator.next())
    }

}
