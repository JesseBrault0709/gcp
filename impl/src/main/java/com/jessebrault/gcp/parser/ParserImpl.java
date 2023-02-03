package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.*;
import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Collection;
import java.util.List;

import static com.jessebrault.gcp.tokenizer.Token.Type.*;

public final class ParserImpl implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(ParserImpl.class);
    private static final Marker enter = MarkerFactory.getMarker("ENTER");
    private static final Marker exit = MarkerFactory.getMarker("EXIT");

    @Override
    public Document parse(TokenIterator tokenIterator) {
        return document(tokenIterator);
    }

    private static Document document(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        final var document = new Document();

        while (tokenIterator.hasNext()) {
            if (tokenIterator.isFirst(TEXT)) {
                document.appendChild(text(tokenIterator));
            } else if (tokenIterator.isFirst(COMPONENT_START)) {
                document.appendChild(componentNode(tokenIterator));
            } else if (tokenIterator.isFirst(DOLLAR)) {
                // TODO
            } else if (tokenIterator.isFirst(EXPRESSION_SCRIPTLET_OPEN)) {
                // TODO
            } else if (tokenIterator.isFirst(BLOCK_SCRIPTLET_OPEN)) {
                // TODO
            } else {
                document.appendChild(
                        unexpectedToken(
                                tokenIterator,
                                List.of(
                                        TEXT, COMPONENT_START, DOLLAR,
                                        EXPRESSION_SCRIPTLET_OPEN, BLOCK_SCRIPTLET_OPEN
                                ),
                                List.of()
                        )
                );
            }
        }

        logger.trace(exit, "document: {}", document);
        return document;
    }

    /**
     * Peek: TEXT
     */
    private static Text text(TokenIterator tokenIterator) {
        logger.trace("tokenIterator: {}", tokenIterator);

        if (!tokenIterator.isFirst(TEXT)) {
            throw new IllegalArgumentException("did not peek for TEXT");
        }
        final var textToken = tokenIterator.next();
        logger.debug("textToken: {}", textToken);

        final var text = new Text();
        text.setText(textToken.getText().toString());
        text.addToken(textToken);

        logger.trace(exit, "text: {}", text);
        return text;
    }

    /**
     * Peek: COMPONENT_START
     */
    private static AstNode componentNode(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(COMPONENT_START)) {

            // if peekSecond is FORWARD_SLASH, we have ClosingComponent
            // if not, peek until either we hit FORWARD_SLASH or COMPONENT_END or fail

            if (tokenIterator.isSecond(FORWARD_SLASH)) {
                result = closingComponent(tokenIterator);
            } else {
                final var peekInfiniteResult = tokenIterator.peekInfinite(List.of(FORWARD_SLASH, COMPONENT_END));
                if (peekInfiniteResult == null) {
                    // we don't know what it is; assume OpeningComponent until we inevitably hit an error
                    result = openingComponent(tokenIterator);
                } else if (peekInfiniteResult.getType() == FORWARD_SLASH) {
                    result = selfClosingComponent(tokenIterator);
                } else if (peekInfiniteResult.getType() == COMPONENT_END) {
                    result = openingComponent(tokenIterator);
                } else {
                    throw new RuntimeException("should not have gotten here");
                }
            }
        } else {
            throw new IllegalArgumentException("did not peek for COMPONENT_START");
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    /**
     * Peek: COMPONENT_START ... COMPONENT_END
     */
    private static OpeningComponent openingComponent(TokenIterator tokenIterator) {
        final var openingComponent = new OpeningComponent();
        openingComponent.appendChild(componentStart(tokenIterator));
        openingComponent.appendChild(componentIdentifier(tokenIterator));

        // TODO: keys and values

        openingComponent.appendChild(componentEnd(tokenIterator));
        return openingComponent;
    }

    /**
     * Peek: COMPONENT_START ... FORWARD_SLASH
     */
    private static SelfClosingComponent selfClosingComponent(TokenIterator tokenIterator) {
        final var selfClosingComponent = new SelfClosingComponent();
        selfClosingComponent.appendChild(componentStart(tokenIterator));
        selfClosingComponent.appendChild(componentIdentifier(tokenIterator));

        // TODO: keys and values

        selfClosingComponent.appendChild(forwardSlash(tokenIterator));
        selfClosingComponent.appendChild(componentEnd(tokenIterator));
        return selfClosingComponent;
    }

    /**
     * Peek: COMPONENT_START FORWARD_SLASH
     */
    private static ClosingComponent closingComponent(TokenIterator tokenIterator) {
        final var closingComponent = new ClosingComponent();
        closingComponent.appendChild(componentStart(tokenIterator));
        closingComponent.appendChild(forwardSlash(tokenIterator));
        closingComponent.appendChild(componentIdentifier(tokenIterator));
        closingComponent.appendChild(componentEnd(tokenIterator));
        return closingComponent;
    }

    /**
     * Peek: COMPONENT_START
     */
    private static AstNode componentStart(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(COMPONENT_START)) {
            result = new ComponentStart();
            result.addToken(tokenIterator.next());
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(COMPONENT_START), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(COMPONENT_START), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode componentIdentifier(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(List.of(CLASS_NAME, PACKAGE_NAME))) {
            final var componentIdentifier = new ComponentIdentifier();

            if (tokenIterator.isFirst(CLASS_NAME)) {
                componentIdentifier.appendChild(componentClassName(tokenIterator));
            } else if (tokenIterator.isFirst(PACKAGE_NAME)) {
                componentIdentifier.appendChild(componentPackageName(tokenIterator));
            } else {
                throw new RuntimeException("should not have gotten here");
            }

            while (tokenIterator.hasNext()) {
                if (tokenIterator.isFirst(DOT)) {
                    componentIdentifier.appendChild(componentIdentifierIndex(tokenIterator));
                } else {
                    break;
                }
            }

            result = componentIdentifier;
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(CLASS_NAME, PACKAGE_NAME), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(CLASS_NAME, PACKAGE_NAME), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode componentIdentifierIndex(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        final var dot = componentDot(tokenIterator);

        AstNode index;
        if (tokenIterator.isFirst(PACKAGE_NAME)) {
            index = componentPackageName(tokenIterator);
        } else if (tokenIterator.isFirst(CLASS_NAME)) {
            index = componentClassName(tokenIterator);
        } else if (tokenIterator.hasNext()) {
            index = unexpectedToken(tokenIterator, List.of(PACKAGE_NAME, CLASS_NAME), List.of(dot));
        } else {
            index = notEnoughTokens(tokenIterator, List.of(PACKAGE_NAME, CLASS_NAME), List.of(dot));
        }

        final var componentIdentifierIndex = new ComponentIdentifierIndex();
        componentIdentifierIndex.appendChild(dot);
        componentIdentifierIndex.appendChild(index);

        logger.trace(exit, "componentIdentifierIndex: {}", componentIdentifierIndex);
        return componentIdentifierIndex;
    }

    private static AstNode componentDot(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(DOT)) {
            final var t = tokenIterator.next();
            final var componentDot = new ComponentDot();
            componentDot.addToken(t);
            result = componentDot;
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(DOT), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(DOT), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode componentClassName(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(CLASS_NAME)) {
            final var t = tokenIterator.next();
            final var componentClassName = new ComponentClassName();
            componentClassName.setClassName(t.getText().toString());
            componentClassName.addToken(t);
            result = componentClassName;
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(CLASS_NAME), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(CLASS_NAME), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode componentPackageName(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(PACKAGE_NAME)) {
            final var t = tokenIterator.next();
            final var componentPackageName = new ComponentPackageName();
            componentPackageName.setPackageName(t.getText().toString());
            componentPackageName.addToken(t);
            result = componentPackageName;
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(PACKAGE_NAME), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(PACKAGE_NAME), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode forwardSlash(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(FORWARD_SLASH)) {
            result = new ForwardSlash();
            result.addToken(tokenIterator.next());
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(FORWARD_SLASH), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(FORWARD_SLASH), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    private static AstNode componentEnd(TokenIterator tokenIterator) {
        logger.trace(enter, "tokenIterator: {}", tokenIterator);

        AstNode result;

        if (tokenIterator.isFirst(COMPONENT_END)) {
            result = new ComponentEnd();
            result.addToken(tokenIterator.next());
        } else if (tokenIterator.hasNext()) {
            result = unexpectedToken(tokenIterator, List.of(COMPONENT_END), List.of());
        } else {
            result = notEnoughTokens(tokenIterator, List.of(COMPONENT_END), List.of());
        }

        logger.trace(exit, "result: {}", result);
        return result;
    }

    /**
     * Consumes: ANY*
     * TODO: reconsider consuming ANY*
     */
    private static NotEnoughTokensNode notEnoughTokens(
            TokenIterator tokenIterator,
            Collection<Token.Type> expectedTypes,
            List<AstNode> children
    ) {
        logger.trace(enter, "tokenIterator: {}, expectedTypes: {}", tokenIterator, expectedTypes);

        final var notEnoughTokensNode = new NotEnoughTokensNode();

        notEnoughTokensNode.addDiagnostic(new NotEnoughTokensDiagnostic(expectedTypes));

        children.forEach(notEnoughTokensNode::appendChild);

        while (tokenIterator.hasNext()) {
            notEnoughTokensNode.addToken(tokenIterator.next());
        }

        logger.trace(exit, "notEnoughTokensNode: {}", notEnoughTokensNode);
        return notEnoughTokensNode;
    }

    /**
     * Consumes: ANY
     */
    private static UnexpectedTokenNode unexpectedToken(
            TokenIterator tokenIterator,
            Collection<Token.Type> expectedTypes,
            List<AstNode> children
    ) {
        logger.trace(
                enter,
                "tokenIterator: {}, expectedTypes: {}, children: {}",
                tokenIterator, expectedTypes, children
        );

        if (!tokenIterator.hasNext()) {
            throw new RuntimeException("did not peek for a next token");
        }
        final var unexpectedToken = tokenIterator.next();

        final var unexpectedTokenNode = new UnexpectedTokenNode();
        unexpectedTokenNode.addDiagnostic(new UnexpectedTokenDiagnostic(unexpectedToken, expectedTypes));

        children.forEach(unexpectedTokenNode::appendChild);

        unexpectedTokenNode.addToken(unexpectedToken);

        logger.trace(exit, "unexpectedTokenNode: {}", unexpectedTokenNode);
        return unexpectedTokenNode;
    }

}
