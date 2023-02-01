package com.jessebrault.gcp.ast;

public final class ComponentIdentifier extends AbstractAstNode {

    public ComponentIdentifier() {
        super("ComponentIdentifier");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
