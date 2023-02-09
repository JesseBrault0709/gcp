package com.jessebrault.gcp.parser

import com.jessebrault.gcp.ast.AstNode
import com.jessebrault.gcp.ast.util.AstPrettyPrinter
import com.jessebrault.gcp.token.ListTokenProvider
import com.jessebrault.gcp.token.Token
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.jessebrault.gcp.token.Token.Type.CLASS_NAME
import static com.jessebrault.gcp.token.Token.Type.COMPONENT_END
import static com.jessebrault.gcp.token.Token.Type.COMPONENT_START
import static com.jessebrault.gcp.token.Token.Type.FORWARD_SLASH
import static com.jessebrault.gcp.token.Token.Type.TEXT
import static com.jessebrault.gcp.token.Token.Type.WHITESPACE
import static org.junit.jupiter.api.Assertions.*

class ParserTests {

    private static final Logger logger = LoggerFactory.getLogger(ParserTests)

    private static class NodeSpec {

        AstNode.Type expectedType
        List<Token> expectedTokens
        Closure<Void> tests

        NodeSpec(
                AstNode.Type expectedType,
                List<Token> expectedTokens,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
                Closure<Void> tests = null
        ) {
            this.expectedType = expectedType
            this.expectedTokens = expectedTokens
            this.tests = tests
        }

        void test(AstNode actual) {
            logger.debug('actual: {}', actual)
            assertEquals(this.expectedType, actual.type)
            assertIterableEquals(this.expectedTokens, actual.tokens)
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
            } else {
                assertEquals(0, actual.children.size())
            }
        }

        @Override
        String toString() {
            "NodeSpec(${ this.expectedType }, ${ this.expectedTokens })"
        }

    }

    private static class NodeTester {

        List<NodeSpec> childSpecs = []

        void expect(
                AstNode.Type expectedType,
                List<Token> expectedTokens,
                @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
                Closure<Void> furtherTests
        ) {
            this.childSpecs << new NodeSpec(expectedType, expectedTokens, furtherTests)
        }

        void expect(AstNode.Type expectedType, List<Token> expectedTokens) {
            this.childSpecs << new NodeSpec(expectedType, expectedTokens)
        }

    }

    private final Parser parser = new ParserImpl()

    private void parseDocument(
            List<Token> allTokens,
            List<Token> expectedDocumentTokens,
            @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
            @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
            Closure<Void> documentTests
    ) {
        def tokenProvider = new ListTokenProvider(allTokens)
        def acc = new AstAccumulator(tokenProvider)
        this.parser.parse(tokenProvider, acc)

        def actualDocument = acc.result

        def prettyPrinter = new AstPrettyPrinter(2, false)
        prettyPrinter.document(actualDocument)
        logger.debug('actualDocument:\n{}', prettyPrinter.result)

        def documentSpec = new NodeSpec(AstNode.Type.DOCUMENT, expectedDocumentTokens, documentTests)
        logger.debug('documentSpec: {}', documentSpec)
        documentSpec.test(actualDocument)
    }

    private void parseDocument(
            List<Token> documentTokens,
            @DelegatesTo(value = NodeTester, strategy = Closure.DELEGATE_FIRST)
            @ClosureParams(value = SimpleType, options = ['com.jessebrault.gcp.ast.AstNode'])
            Closure<Void> documentTests
    ) {
        this.parseDocument(documentTokens, documentTokens, documentTests)
    }

    @Test
    void doctypeHtml() {
        def t0 = new Token(TEXT, '<!DOCTYPE html>', 0, 15)
        this.parseDocument([t0]) {
            expect(AstNode.Type.TEXT, [t0])
        }
    }

    @Test
    void simpleOpeningComponent() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(CLASS_NAME, 'Test', 1, 5)
        def t2 = new Token(COMPONENT_END, '>', 5, 6)

        this.parseDocument([t0, t1, t2]) {
            expect(AstNode.Type.OPENING_COMPONENT, [t0, t1, t2]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t1]) {
                    expect(AstNode.Type.CLASS_NAME, [t1])
                }
                expect(AstNode.Type.COMPONENT_END, [t2])
            }
        }
    }

    @Test
    void openingComponentIgnoresWhitespace() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        // in the future, we might be able to tokenize a component with a whitespace here, but for now not
        def t1 = new Token(CLASS_NAME, 'Test', 1, 5)
        def t2 = new Token(WHITESPACE, ' ', 5, 6)
        def t3 = new Token(COMPONENT_END, '>', 6, 7)

        this.parseDocument([t0, t1, t2, t3], [t0, t1, t3]) {
            expect(AstNode.Type.OPENING_COMPONENT, [t0, t1, t3]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t1]) {
                    expect(AstNode.Type.CLASS_NAME, [t1])
                }
                expect(AstNode.Type.COMPONENT_END, [t3])
            }
        }
    }

    @Test
    void openingComponentNothingAfterClassName() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(CLASS_NAME, 'Test', 1, 5)

        this.parseDocument([t0, t1]) {
            expect(AstNode.Type.OPENING_COMPONENT, [t0, t1]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t1]) {
                    expect(AstNode.Type.CLASS_NAME, [t1])
                }
                expect(AstNode.Type.UNEXPECTED_END_OF_INPUT, [])
            }
        }
    }

    @Test
    void simpleSelfClosingComponent() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(CLASS_NAME, 'Test', 1, 5)
        def t2 = new Token(FORWARD_SLASH, '/', 5, 6)
        def t3 = new Token(COMPONENT_END, '>', 6, 7)

        this.parseDocument([t0, t1, t2, t3]) {
            expect(AstNode.Type.SELF_CLOSING_COMPONENT, [t0, t1, t2, t3]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t1]) {
                    expect(AstNode.Type.CLASS_NAME, [t1])
                }
                expect(AstNode.Type.FORWARD_SLASH, [t2])
                expect(AstNode.Type.COMPONENT_END, [t3])
            }
        }
    }

    @Test
    void selfClosingComponentIgnoresWhitespace() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(CLASS_NAME, 'Test', 1, 5)
        def t2 = new Token(WHITESPACE, ' ', 5, 6)
        def t3 = new Token(FORWARD_SLASH, '/', 6, 7)
        def t4 = new Token(WHITESPACE, ' ', 7, 8)
        def t5 = new Token(COMPONENT_END, '>', 7, 8)

        this.parseDocument([t0, t1, t2, t3, t4, t5], [t0, t1, t3, t5]) {
            expect(AstNode.Type.SELF_CLOSING_COMPONENT, [t0, t1, t3, t5]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t1]) {
                    expect(AstNode.Type.CLASS_NAME, [t1])
                }
                expect(AstNode.Type.FORWARD_SLASH, [t3])
                expect(AstNode.Type.COMPONENT_END, [t5])
            }
        }
    }

    @Test
    void simpleClosingComponent() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(FORWARD_SLASH, '/', 1, 2)
        def t2 = new Token(CLASS_NAME, 'Test', 2, 6)
        def t3 = new Token(COMPONENT_END, '>', 6, 7)

        this.parseDocument([t0, t1, t2, t3]) {
            expect(AstNode.Type.CLOSING_COMPONENT, [t0, t1, t2, t3]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.FORWARD_SLASH, [t1])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t2]) {
                    expect(AstNode.Type.CLASS_NAME, [t2])
                }
                expect(AstNode.Type.COMPONENT_END, [t3])
            }
        }
    }

    @Test
    void closingComponentIgnoresWhitespace() {
        def t0 = new Token(COMPONENT_START, '<', 0, 1)
        def t1 = new Token(FORWARD_SLASH, '/', 1, 2)
        // Not yet possible to tokenize a space here
        def t2 = new Token(CLASS_NAME, 'Test', 2, 6)
        def t3 = new Token(WHITESPACE, ' ', 6, 7)
        def t4 = new Token(COMPONENT_END, '>', 7, 8)

        this.parseDocument([t0, t1, t2, t3, t4], [t0, t1, t2, t4]) {
            expect(AstNode.Type.CLOSING_COMPONENT, [t0, t1, t2, t4]) {
                expect(AstNode.Type.COMPONENT_START, [t0])
                expect(AstNode.Type.FORWARD_SLASH, [t1])
                expect(AstNode.Type.COMPONENT_IDENTIFIER, [t2]) {
                    expect(AstNode.Type.CLASS_NAME, [t2])
                }
                expect(AstNode.Type.COMPONENT_END, [t4])
            }
        }
    }

}
