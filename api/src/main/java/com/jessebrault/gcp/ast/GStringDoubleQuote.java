package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: "
 */
public final class GStringDoubleQuote extends AbstractAstNode {

    public GStringDoubleQuote(List<Token> tokens) {
        super("GStringDoubleQuote", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
