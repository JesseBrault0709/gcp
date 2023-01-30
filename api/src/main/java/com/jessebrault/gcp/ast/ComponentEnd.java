package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: >
 */
public final class ComponentEnd extends AbstractAstNode {

    public ComponentEnd(List<Token> tokens) {
        super("ComponentEnd", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
         visitor.visit(this);
    }

}
