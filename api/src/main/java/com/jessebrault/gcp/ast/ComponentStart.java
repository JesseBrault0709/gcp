package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: <
 */
public final class ComponentStart extends AbstractAstNode {

    public ComponentStart(List<Token> tokens) {
        super("ComponentStart", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
