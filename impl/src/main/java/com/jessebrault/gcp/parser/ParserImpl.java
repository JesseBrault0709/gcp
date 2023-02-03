package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenIterator;

import java.util.List;

public final class ParserImpl implements Parser {

    @Override
    public void parse(TokenIterator tokenIterator, ParserAccumulator parserAccumulator) {
        document(tokenIterator, parserAccumulator);
    }

    private static void document(TokenIterator iter, ParserAccumulator acc) {
        acc.startRoot(AstNode.Type.DOCUMENT);
        while (iter.hasNext()) {
            if (iter.isFirst(Token.Type.TEXT)) {
                text(iter, acc);
            } else {
                acc.unexpectedToken(iter.next(), List.of(Token.Type.TEXT));
            }
        }
        acc.doneRoot();
    }

    private static void text(TokenIterator i, ParserAccumulator acc) {
        acc.leaf(AstNode.Type.TEXT, i.next());
    }

}
