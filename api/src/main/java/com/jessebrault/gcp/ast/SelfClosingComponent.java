package com.jessebrault.gcp.ast;

public final class SelfClosingComponent extends AbstractAstNode implements ComponentNode {

    public SelfClosingComponent() {
        super("SelfClosingComponent");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
