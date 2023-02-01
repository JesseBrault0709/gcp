package com.jessebrault.gcp.ast;

public final class ComponentIdentifierIndex extends AbstractAstNode {

    public ComponentIdentifierIndex() {
        super("ComponentIdentifierIndex");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
