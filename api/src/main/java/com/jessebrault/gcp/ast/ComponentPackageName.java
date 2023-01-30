package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public final class ComponentPackageName extends AbstractAstNode {

    public ComponentPackageName(List<Token> tokens) {
        super("ComponentPackageName", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
