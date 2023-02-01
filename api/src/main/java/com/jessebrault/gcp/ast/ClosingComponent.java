package com.jessebrault.gcp.ast;

public final class ClosingComponent extends AbstractAstNode implements ComponentNode {

    public ClosingComponent() {
        super("ClosingComponent");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
