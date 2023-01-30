package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: =
 */
public final class ComponentEquals extends AbstractAstNode {

    public ComponentEquals(List<Token> tokens) {
        super("ComponentEquals", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
