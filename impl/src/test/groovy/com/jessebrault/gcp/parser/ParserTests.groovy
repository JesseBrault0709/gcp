package com.jessebrault.gcp.parser

import com.jessebrault.gcp.ast.*
import com.jessebrault.gcp.ast.util.AstNodePrettyPrinter
import com.jessebrault.gcp.tokenizer.ListTokenIterator
import com.jessebrault.gcp.tokenizer.Token
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SimpleType
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.jessebrault.gcp.tokenizer.Token.Type.*
import static org.junit.jupiter.api.Assertions.*

class ParserTests {

    private static final Logger logger = LoggerFactory.getLogger(ParserTests)

    private static class NodeSpec<T extends AstNode> {

        Class<T> nodeClass
        Closure<Void> tests

        NodeSpec(
                Class<T> nodeClass,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(FirstParam.FirstGenericType)
                Closure<Void> tests = null
        ) {
            this.nodeClass = nodeClass;
            this.tests = tests
        }

        void test(AstNode actual) {
            logger.debug('actual: {}', actual)
            assertTrue(this.nodeClass.isAssignableFrom(actual.class))
            if (this.tests != null) {
                def nodeTester = new NodeTester()
                this.tests.setDelegate(nodeTester)
                this.tests.setResolveStrategy(Closure.DELEGATE_FIRST)
                this.tests(actual)

                assertEquals(nodeTester.childSpecs.size(), actual.children.size())

                def childIterator = actual.children.iterator()
                nodeTester.childSpecs.each {
                    assertTrue(childIterator.hasNext())
                    def next = childIterator.next()
                    it.test(next)
                }
            }
        }

        @Override
        String toString() {
            "NodeSpec(${ this.nodeClass.simpleName })"
        }

    }

    private static class NodeTester {

        List<NodeSpec<? extends AstNode>> childSpecs = []

        def <T extends AstNode> void expect(
                Class<T> childNodeClass,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(FirstParam.FirstGenericType)
                Closure<Void> furtherTests
        ) {
            this.childSpecs << new NodeSpec<>(childNodeClass, furtherTests)
        }

        void expect(Class<? extends AstNode> childNodeClass) {
            this.childSpecs << new NodeSpec<>(childNodeClass)
        }

    }

    private static class TestToken implements Token {

        Type type
        CharSequence text
        int startIndex
        int endIndex
        int line
        int col

        TestToken(Type type) {
            this.type = type
        }

        TestToken(Type type, CharSequence text) {
            this(type)
            this.text = text
        }

        @Override
        String toString() {
            "TestToken(${ this.type }, ${ this.text })"
        }

    }

    private final Parser parser = new ParserImpl()

    private void parseDocument(
            List<Token> tokens,
            @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
            @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.Document'])
            Closure<Void> documentTests
    ) {
        def actualDocument = this.parser.parse(new ListTokenIterator(tokens))

        def prettyPrinter = new AstNodePrettyPrinter(2, true)
        actualDocument.accept(prettyPrinter)
        logger.debug('actualDocument:\n{}', prettyPrinter.result)

        def documentSpec = new NodeSpec(Document, documentTests)
        logger.debug('documentSpec: {}', documentSpec)
        documentSpec.test(actualDocument)
    }


    @Test
    void doctypeHtml() {
        def t0 = new TestToken(TEXT, '<!DOCTYPE html>')
        this.parseDocument([t0]) {
            assertEquals(1, it.tokens.size())
            assertEquals(t0, it.tokens[0])

            expect(Text) {
                assertEquals(1, it.tokens.size())
                assertEquals(t0, it.tokens[0])
            }
        }
    }

    @Test
    void componentWithNoEndRunsOut() {
        def t0 = new TestToken(COMPONENT_START, '<')
        this.parseDocument([t0]) {
            assertEquals(1, it.tokens.size())
            assertEquals(t0, it.tokens[0])

            expect(OpeningComponent) {
                assertEquals(1, it.tokens.size())
                assertEquals(t0, it.tokens[0])

                expect(ComponentStart) {
                    assertEquals(1, it.tokens.size())
                    assertEquals(t0, it.tokens[0])
                }

                expect(NotEnoughTokensNode)
                expect(NotEnoughTokensNode)
            }
        }
    }

    @Test
    void componentWithNoEndRunsOutAndEachChildHasProperTokens() {
        def t0 = new TestToken(COMPONENT_START, '<')
        def t1 = new TestToken(CLASS_NAME, 'Test')

        this.parseDocument([t0, t1]) {
            assertIterableEquals([t0, t1], it.tokens)

            expect(OpeningComponent) {
                assertIterableEquals([t0, t1], it.tokens)

                expect(ComponentStart) {
                    assertIterableEquals([t0], it.tokens)
                }
                expect(ComponentIdentifier) {
                    assertIterableEquals([t1], it.tokens)

                    expect(ComponentClassName) {
                        assertIterableEquals([t1], it.tokens)
                    }
                }
                expect(NotEnoughTokensNode)
            }
        }
    }

    @Test
    void componentWithUnexpectedToken() {
        def t0 = new TestToken(COMPONENT_START, '<')
        def t1 = new TestToken(CLASS_NAME, 'Test')
        def t2 = new TestToken(COMPONENT_START, '<')

        this.parseDocument([t0, t1, t2]) {
            assertIterableEquals([t0, t1, t2], it.tokens)

            expect(OpeningComponent) {
                assertIterableEquals([t0, t1, t2], it.tokens)

                expect(ComponentStart) {
                    assertIterableEquals([t0], it.tokens)
                }
                expect(ComponentIdentifier) {
                    assertIterableEquals([t1], it.tokens)

                    expect(ComponentClassName) {
                        assertIterableEquals([t1], it.tokens)
                    }
                }
                expect(UnexpectedTokenNode) {
                    assertIterableEquals([t2], it.tokens)
                }
            }
        }
    }

    @Test
    void componentWithUnexpectedTokenFollowedByExtra() {
        def t0 = new TestToken(COMPONENT_START, '<')
        def t1 = new TestToken(CLASS_NAME, 'Test')
        def t2 = new TestToken(COMPONENT_START, '<')
        def t3 = new TestToken(CLASS_NAME, 'Test')

        this.parseDocument([t0, t1, t2, t3]) {
            assertIterableEquals([t0, t1, t2, t3], it.tokens)

            expect(OpeningComponent) {
                assertIterableEquals([t0, t1, t2], it.tokens)

                expect(ComponentStart) {
                    assertIterableEquals([t0], it.tokens)
                }
                expect(ComponentIdentifier) {
                    assertIterableEquals([t1], it.tokens)

                    expect(ComponentClassName) {
                        assertIterableEquals([t1], it.tokens)
                    }
                }
                expect(UnexpectedTokenNode) {
                    assertIterableEquals([t2], it.tokens)
                }
            }
            expect(UnexpectedTokenNode) {
                assertIterableEquals([t3], it.tokens)
            }
        }
    }

    @Test
    void closingComponentCorrect() {
        def t0 = new TestToken(COMPONENT_START, '<')
        def t1 = new TestToken(FORWARD_SLASH, '/')
        def t2 = new TestToken(CLASS_NAME, 'Test')
        def t3 = new TestToken(COMPONENT_END, '>')

        this.parseDocument([t0, t1, t2, t3]) {
            assertIterableEquals([t0, t1, t2, t3], it.tokens)

            expect(ClosingComponent) {
                assertIterableEquals([t0, t1, t2, t3], it.tokens)

                expect(ComponentStart) {
                    assertIterableEquals([t0], it.tokens)
                }

                expect(ForwardSlash) {
                    assertIterableEquals([t1], it.tokens)
                }

                expect(ComponentIdentifier) {
                    assertIterableEquals([t2], it.tokens)

                    expect(ComponentClassName) {
                        assertIterableEquals([t2], it.tokens)
                    }
                }

                expect(ComponentEnd) {
                    assertIterableEquals([t3], it.tokens)
                }
            }
        }
    }

}
