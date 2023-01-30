package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: $
 */
public final class Dollar extends AbstractAstNode {

    public Dollar(List<Token> tokens) {
        super("Dollar", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
