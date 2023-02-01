package com.jessebrault.gcp.ast;

public final class DollarReference extends AbstractAstNode {

    public DollarReference() {
        super("DollarReference");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
