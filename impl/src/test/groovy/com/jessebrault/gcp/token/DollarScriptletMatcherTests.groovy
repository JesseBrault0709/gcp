package com.jessebrault.gcp.token

import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertTrue

class DollarScriptletMatcherTests {

    private static final Logger logger = LoggerFactory.getLogger(DollarScriptletMatcherTests)

    private final DollarScriptletMatcher matcher = new DollarScriptletMatcher();

    private void expectNull(String input) {
        assertNull(this.matcher.apply(input))
    }

    private void expectSuccess(String expectedEntire, String input) {
        def r = this.matcher.apply(input)
        logger.debug('r: {}', r)
        assertTrue(r instanceof DollarScriptletMatcher.Success)
        assertEquals(expectedEntire, r.entire())
        assertEquals('$', r.part(1))
        assertEquals('{', r.part(2))
        assertEquals(expectedEntire.substring(2, expectedEntire.length() - 1), r.part(3))
        assertEquals('}', r.part(4))
    }

    private void expectFailure(String expectedEntire, String input) {
        def r = this.matcher.apply(input)
        logger.debug('r: {}', r)
        assertTrue(r instanceof DollarScriptletMatcher.Failure)
        assertEquals(expectedEntire, r.entire())
        assertEquals('$', r.part(1))
        assertEquals('{', r.part(2))
        assertEquals(expectedEntire.substring(2), r.part(3))
    }

    @Test
    void returnsNullIfNoDollarAndCurlyEmptyString() {
        expectNull ''
    }

    @Test
    void returnsNullIfNoDollarAndCurly() {
        expectNull 'abc'
    }

    @Test
    void empty() {
        expectSuccess '${}', '${}'
    }

    @Test
    void simple() {
        expectSuccess '${ 1 + 2 }', '${ 1 + 2 }'
    }

    @Test
    void nestedString() {
        expectSuccess '${ "myString" }', '${ "myString" }'
    }

    @Test
    void nestedCurlyBraces() {
        expectSuccess '${ [1, 2, 3].collect { it + 1 }.size() }', '${ [1, 2, 3].collect { it + 1 }.size() }'
    }

    @Test
    void nestedSingleQuoteString() {
        expectSuccess '${ \'abc\' }', '${ \'abc\' }'
    }

    @Test
    void nestedGString() {
        expectSuccess '${ "abc" }', '${ "abc" }'
    }

    @Test
    void nestedGStringWithClosure() {
        expectSuccess '${ "abc${ it }" }', '${ "abc${ it }" }'
    }

    @Test
    void takesOnlyAsNeeded() {
        expectSuccess '${ 1 + 2 }', '${ 1 + 2 } someOther=${ 3 + 4 }'
    }

    @Test
    void failsUponRunningOutOfInputNoClosing() {
        expectFailure '${', '${'
    }

    @Test
    void failsUponRunningOutOfInput() {
        expectFailure '${ a.b.c ', '${ a.b.c '
    }

}
