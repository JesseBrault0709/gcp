package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.token.Token;
import com.jessebrault.gcp.token.TokenProvider;

import java.util.List;

public final class ParserImpl implements Parser {

    @Override
    public void parse(TokenProvider tokenProvider, ParserAccumulator parserAccumulator) {
        document(tokenProvider, parserAccumulator);
    }

    private static void document(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        accumulator.startRoot(AstNode.Type.DOCUMENT);
        while (tokenProvider.getCurrent() != null) {
            if (tokenProvider.peekCurrent(Token.Type.TEXT)) {
                text(tokenProvider, accumulator);
            } else {
                accumulator.unexpectedToken(List.of(Token.Type.TEXT));
            }
        }
        accumulator.doneRoot();
    }

    private static void text(TokenProvider tokenProvider, ParserAccumulator accumulator) {
        if (tokenProvider.peekCurrent(Token.Type.TEXT)) {
            accumulator.leaf(AstNode.Type.TEXT);
        } else {
            accumulator.unexpectedToken(List.of(Token.Type.TEXT));
        }
    }

}
