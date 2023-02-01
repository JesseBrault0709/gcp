package com.jessebrault.gcp.ast;

public final class ComponentKeysAndValues extends AbstractAstNode {

    public ComponentKeysAndValues() {
        super("ComponentKeysAndValues");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }
}
