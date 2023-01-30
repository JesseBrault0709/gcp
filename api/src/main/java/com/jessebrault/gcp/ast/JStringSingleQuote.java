package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: '
 */
public final class JStringSingleQuote extends AbstractAstNode {

    public JStringSingleQuote(List<Token> tokens) {
        super("JStringSingleQuote", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
