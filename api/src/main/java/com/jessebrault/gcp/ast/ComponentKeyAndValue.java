package com.jessebrault.gcp.ast;

public final class ComponentKeyAndValue extends AbstractAstNode {

    public ComponentKeyAndValue() {
        super("ComponentKeyAndValue");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
