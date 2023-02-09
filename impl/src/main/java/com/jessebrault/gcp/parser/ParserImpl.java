package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.token.Token;
import com.jessebrault.gcp.token.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;

public final class ParserImpl implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(ParserImpl.class);
    private static final Marker enter = MarkerFactory.getMarker("ENTER");
    private static final Marker exit = MarkerFactory.getMarker("EXIT");

    @Override
    public void parse(TokenProvider tokenProvider, ParserAccumulator parserAccumulator) {
        logger.trace(enter, "");
        document(tokenProvider, parserAccumulator);
        logger.trace(exit, "");
    }

    private static void document(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        accumulator.startRoot(AstNode.Type.DOCUMENT);
        while (tokenProvider.getCurrent() != null) {
            if (tokenProvider.peekCurrent(Token.Type.TEXT)) {
                text(tokenProvider, accumulator);
            } else if (tokenProvider.peekCurrent(Token.Type.COMPONENT_START)) {
                component(tokenProvider, accumulator);
            } else {
                accumulator.unexpectedToken(List.of(Token.Type.TEXT));
                tokenProvider.advance();
            }
        }
        accumulator.doneRoot();

        logger.trace(exit, "");
    }

    private static void consumeCurrent(
            Token.Type tokenType,
            AstNode.Type astNodeType,
            TokenProvider tokenProvider,
            ParserAccumulator accumulator
    ) {
        logger.trace(enter, "tokenType: {}, astNodeType: {}", tokenType, astNodeType);

        if (tokenProvider.peekCurrent(tokenType)) {
            accumulator.leaf(astNodeType);
            tokenProvider.advance();
        } else if (tokenProvider.getCurrent() != null) {
            accumulator.unexpectedToken(List.of(tokenType));
            tokenProvider.advance();
        } else {
            accumulator.unexpectedEndOfInput(List.of(tokenType));
        }

        logger.trace(exit, "");
    }

    private static void text(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.TEXT, AstNode.Type.TEXT, tokenProvider, accumulator);
    }

    private static void ignoreWhitespace(TokenProvider tokenProvider) {
        logger.trace(enter, "");

        while (tokenProvider.peekCurrent(Token.Type.WHITESPACE)) {
            tokenProvider.advance();
        }

        logger.trace(exit, "");
    }

    private static void component(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        if (tokenProvider.peekSecond(Token.Type.FORWARD_SLASH)) {
            closingComponent(tokenProvider, accumulator);
        } else if (tokenProvider.peekInfinite(Token.Type.FORWARD_SLASH, Token.Type.COMPONENT_END)) {
            selfClosingComponent(tokenProvider, accumulator);
        } else {
            openingComponent(tokenProvider, accumulator);
        }

        logger.trace(exit, "");
    }

    private static void openingComponent(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        accumulator.start(AstNode.Type.OPENING_COMPONENT);

        componentStart(tokenProvider, accumulator);
        componentIdentifier(tokenProvider, accumulator);
        ignoreWhitespace(tokenProvider);
        // keys and values
        ignoreWhitespace(tokenProvider);
        componentEnd(tokenProvider, accumulator);

        accumulator.done();

        logger.trace(exit, "");
    }

    private static void selfClosingComponent(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        accumulator.start(AstNode.Type.SELF_CLOSING_COMPONENT);

        componentStart(tokenProvider, accumulator);
        componentIdentifier(tokenProvider, accumulator);
        ignoreWhitespace(tokenProvider);
        // keys and values
        forwardSlash(tokenProvider, accumulator);
        ignoreWhitespace(tokenProvider);
        componentEnd(tokenProvider, accumulator);

        accumulator.done();

        logger.trace(exit, "");
    }

    private static void closingComponent(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        accumulator.start(AstNode.Type.CLOSING_COMPONENT);

        componentStart(tokenProvider, accumulator);
        forwardSlash(tokenProvider, accumulator);
        componentIdentifier(tokenProvider, accumulator);
        ignoreWhitespace(tokenProvider);
        componentEnd(tokenProvider, accumulator);

        accumulator.done();

        logger.trace(exit, "");
    }

    private static void componentStart(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.COMPONENT_START, AstNode.Type.COMPONENT_START, tokenProvider, accumulator);
    }

    private static void componentIdentifier(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        logger.trace(enter, "");

        accumulator.start(AstNode.Type.COMPONENT_IDENTIFIER);

        if (tokenProvider.peekCurrent(Token.Type.PACKAGE_NAME)) {
            packageName(tokenProvider, accumulator);
        } else if (tokenProvider.peekCurrent(Token.Type.CLASS_NAME)) {
            className(tokenProvider, accumulator);
        } else {
            accumulator.unexpectedToken(List.of(Token.Type.PACKAGE_NAME, Token.Type.CLASS_NAME));
            tokenProvider.advance();
        }

        while (tokenProvider.peekCurrent(Token.Type.DOT)) {
            accumulator.start(AstNode.Type.INDEX);

            dot(tokenProvider, accumulator);

            if (tokenProvider.peekCurrent(Token.Type.PACKAGE_NAME)) {
                packageName(tokenProvider, accumulator);
            } else if (tokenProvider.peekCurrent(Token.Type.CLASS_NAME)) {
                className(tokenProvider, accumulator);
            } else if (tokenProvider.peekCurrent(Token.Type.WHITESPACE)) {
                tokenProvider.advance();
            } else {
                accumulator.unexpectedToken(List.of(Token.Type.PACKAGE_NAME, Token.Type.CLASS_NAME));
                tokenProvider.advance();
            }

            accumulator.done();
        }

        accumulator.done();

        logger.trace(exit, "");
    }

    private static void packageName(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.PACKAGE_NAME, AstNode.Type.PACKAGE_NAME, tokenProvider, accumulator);
    }

    private static void className(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.CLASS_NAME, AstNode.Type.CLASS_NAME, tokenProvider, accumulator);
    }

    private static void dot(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.DOT, AstNode.Type.DOT, tokenProvider, accumulator);
    }

    private static void forwardSlash(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.FORWARD_SLASH, AstNode.Type.FORWARD_SLASH, tokenProvider, accumulator);
    }

    private static void componentEnd(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        consumeCurrent(Token.Type.COMPONENT_END, AstNode.Type.COMPONENT_END, tokenProvider, accumulator);
    }

}
