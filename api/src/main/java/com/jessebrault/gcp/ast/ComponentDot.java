package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public final class ComponentDot extends AbstractAstNode {

    public ComponentDot(List<Token> tokens) {
        super("ComponentDot", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
