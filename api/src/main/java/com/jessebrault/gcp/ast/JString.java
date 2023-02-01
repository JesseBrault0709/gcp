package com.jessebrault.gcp.ast;

public final class JString extends AbstractAstNode {

    public JString() {
        super("JString");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
