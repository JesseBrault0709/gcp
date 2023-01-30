package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public final class ComponentClassName extends AbstractAstNode {

    public ComponentClassName(List<Token> tokens) {
        super("ComponentClassName", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
