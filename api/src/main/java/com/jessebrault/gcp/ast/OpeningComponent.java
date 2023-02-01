package com.jessebrault.gcp.ast;

public final class OpeningComponent extends AbstractAstNode implements ComponentNode {

    public OpeningComponent() {
        super("OpeningComponent");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
