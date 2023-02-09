package com.jessebrault.gcp.token

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNull

class GStringMatcherTests {

    private final GStringMatcher matcher = new GStringMatcher()

    private void test(String expectedEntire, String input) {
        def output = this.matcher.apply(input)
        assertEquals(expectedEntire, output.entire())
        assertEquals('"', output.part(1))
        assertEquals(expectedEntire.substring(1, expectedEntire.length() - 1), output.part(2))
        assertEquals('"', output.part(3))
    }

    @Test
    void empty() {
        test '""', '""'
    }

    @Test
    void simple() {
        test '"abc"', '"abc"'
    }

    @Test
    void nestedDollarClosureWithGString() {
        test '"abc ${ \'def\'.each { "$it " }.join() }"', '"abc ${ \'def\'.each { "$it " }.join() }"'
    }

    @Test
    void nestedDollarClosureWithGStringTakesOnlyAsNeeded() {
        test '"abc ${ \'def\'.each { "$it " }.join() }"', '"abc ${ \'def\'.each { "$it " }.join() }" test="rest"'
    }

    @Test
    void takesOnlyAsNeeded() {
        test '"abc"', '"abc" test="def"'
    }

    @Test
    void incompleteNoContents() {
        assertNull(this.matcher.apply('"'))
    }

    @Test
    void incompleteWithContents() {
        assertNull(this.matcher.apply('"abc'))
    }

}
