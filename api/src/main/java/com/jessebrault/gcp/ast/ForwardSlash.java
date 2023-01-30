package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf
 */
public final class ForwardSlash extends AbstractAstNode {

    public ForwardSlash(List<Token> tokens) {
        super("ForwardSlash", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
