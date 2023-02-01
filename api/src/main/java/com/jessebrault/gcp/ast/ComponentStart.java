package com.jessebrault.gcp.ast;

/**
 * Leaf: <
 */
public final class ComponentStart extends AbstractAstNode {

    public ComponentStart() {
        super("ComponentStart");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
