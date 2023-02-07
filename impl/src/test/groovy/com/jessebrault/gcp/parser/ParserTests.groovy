package com.jessebrault.gcp.parser

import com.jessebrault.gcp.ast.AstNode
import com.jessebrault.gcp.token.ListTokenProvider
import com.jessebrault.gcp.token.Token
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.jessebrault.gcp.token.Token.Type.TEXT
import static org.junit.jupiter.api.Assertions.*

class ParserTests {

    private static final Logger logger = LoggerFactory.getLogger(ParserTests)

    private static class NodeSpec {

        AstNode.Type expectedType
        Closure<Void> tests

        NodeSpec(
                AstNode.Type expectedType,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
                Closure<Void> tests = null
        ) {
            this.expectedType = expectedType
            this.tests = tests
        }

        void test(AstNode actual) {
            logger.debug('actual: {}', actual)
            assertEquals(this.expectedType, actual.type)
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
            "NodeSpec(${ this.expectedType })"
        }

    }

    private static class NodeTester {

        List<NodeSpec> childSpecs = []

        void expect(
                AstNode.Type expectedType,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
                Closure<Void> furtherTests
        ) {
            this.childSpecs << new NodeSpec(expectedType, furtherTests)
        }

        void expect(AstNode.Type expectedType) {
            this.childSpecs << new NodeSpec(expectedType)
        }

    }

    private final Parser parser = new ParserImpl()

    private void parseDocument(
            List<Token> tokens,
            @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
            @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
            Closure<Void> documentTests
    ) {
        def tokenProvider = new ListTokenProvider(tokens)
        def acc = new AstAccumulator(tokenProvider)
        this.parser.parse(tokenProvider, acc)

//        def prettyPrinter = new AstNodePrettyPrinter(2, true)
//        actualDocument.accept(prettyPrinter)
//        logger.debug('actualDocument:\n{}', prettyPrinter.result)

        def documentSpec = new NodeSpec(AstNode.Type.DOCUMENT, documentTests)
        logger.debug('documentSpec: {}', documentSpec)
        documentSpec.test(acc.result)
    }


    @Test
    void doctypeHtml() {
        def t0 = new Token(TEXT, '<!DOCTYPE html>', 0, 15)
        this.parseDocument([t0]) {
            assertIterableEquals([t0], it.tokens)

            expect(AstNode.Type.TEXT) {
                assertIterableEquals([t0], it.tokens)
            }
        }
    }

//    @Test
//    void componentWithNoEndRunsOut() {
//        def t0 = new TestToken(COMPONENT_START, '<')
//        this.parseDocument([t0]) {
//            assertEquals(1, it.tokens.size())
//            assertEquals(t0, it.tokens[0])
//
//            expect(OpeningComponent) {
//                assertEquals(1, it.tokens.size())
//                assertEquals(t0, it.tokens[0])
//
//                expect(ComponentStart) {
//                    assertEquals(1, it.tokens.size())
//                    assertEquals(t0, it.tokens[0])
//                }
//
//                expect(NotEnoughTokensNode)
//                expect(NotEnoughTokensNode)
//            }
//        }
//    }
//
//    @Test
//    void componentWithNoEndRunsOutAndEachChildHasProperTokens() {
//        def t0 = new TestToken(COMPONENT_START, '<')
//        def t1 = new TestToken(CLASS_NAME, 'Test')
//
//        this.parseDocument([t0, t1]) {
//            assertIterableEquals([t0, t1], it.tokens)
//
//            expect(OpeningComponent) {
//                assertIterableEquals([t0, t1], it.tokens)
//
//                expect(ComponentStart) {
//                    assertIterableEquals([t0], it.tokens)
//                }
//                expect(ComponentIdentifier) {
//                    assertIterableEquals([t1], it.tokens)
//
//                    expect(ComponentClassName) {
//                        assertIterableEquals([t1], it.tokens)
//                    }
//                }
//                expect(NotEnoughTokensNode)
//            }
//        }
//    }
//
//    @Test
//    void componentWithUnexpectedToken() {
//        def t0 = new TestToken(COMPONENT_START, '<')
//        def t1 = new TestToken(CLASS_NAME, 'Test')
//        def t2 = new TestToken(COMPONENT_START, '<')
//
//        this.parseDocument([t0, t1, t2]) {
//            assertIterableEquals([t0, t1, t2], it.tokens)
//
//            expect(OpeningComponent) {
//                assertIterableEquals([t0, t1, t2], it.tokens)
//
//                expect(ComponentStart) {
//                    assertIterableEquals([t0], it.tokens)
//                }
//                expect(ComponentIdentifier) {
//                    assertIterableEquals([t1], it.tokens)
//
//                    expect(ComponentClassName) {
//                        assertIterableEquals([t1], it.tokens)
//                    }
//                }
//                expect(UnexpectedTokenNode) {
//                    assertIterableEquals([t2], it.tokens)
//                }
//            }
//        }
//    }
//
//    @Test
//    void componentWithUnexpectedTokenFollowedByExtra() {
//        def t0 = new TestToken(COMPONENT_START, '<')
//        def t1 = new TestToken(CLASS_NAME, 'Test')
//        def t2 = new TestToken(COMPONENT_START, '<')
//        def t3 = new TestToken(CLASS_NAME, 'Test')
//
//        this.parseDocument([t0, t1, t2, t3]) {
//            assertIterableEquals([t0, t1, t2, t3], it.tokens)
//
//            expect(OpeningComponent) {
//                assertIterableEquals([t0, t1, t2], it.tokens)
//
//                expect(ComponentStart) {
//                    assertIterableEquals([t0], it.tokens)
//                }
//                expect(ComponentIdentifier) {
//                    assertIterableEquals([t1], it.tokens)
//
//                    expect(ComponentClassName) {
//                        assertIterableEquals([t1], it.tokens)
//                    }
//                }
//                expect(UnexpectedTokenNode) {
//                    assertIterableEquals([t2], it.tokens)
//                }
//            }
//            expect(UnexpectedTokenNode) {
//                assertIterableEquals([t3], it.tokens)
//            }
//        }
//    }
//
//    @Test
//    void closingComponentCorrect() {
//        def t0 = new TestToken(COMPONENT_START, '<')
//        def t1 = new TestToken(FORWARD_SLASH, '/')
//        def t2 = new TestToken(CLASS_NAME, 'Test')
//        def t3 = new TestToken(COMPONENT_END, '>')
//
//        this.parseDocument([t0, t1, t2, t3]) {
//            assertIterableEquals([t0, t1, t2, t3], it.tokens)
//
//            expect(ClosingComponent) {
//                assertIterableEquals([t0, t1, t2, t3], it.tokens)
//
//                expect(ComponentStart) {
//                    assertIterableEquals([t0], it.tokens)
//                }
//
//                expect(ForwardSlash) {
//                    assertIterableEquals([t1], it.tokens)
//                }
//
//                expect(ComponentIdentifier) {
//                    assertIterableEquals([t2], it.tokens)
//
//                    expect(ComponentClassName) {
//                        assertIterableEquals([t2], it.tokens)
//                    }
//                }
//
//                expect(ComponentEnd) {
//                    assertIterableEquals([t3], it.tokens)
//                }
//            }
//        }
//    }

}
