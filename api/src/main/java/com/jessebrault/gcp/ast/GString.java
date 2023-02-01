package com.jessebrault.gcp.ast;

public final class GString extends AbstractAstNode {

    public GString() {
        super("GStringValue");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
