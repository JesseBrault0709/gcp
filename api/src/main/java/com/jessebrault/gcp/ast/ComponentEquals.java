package com.jessebrault.gcp.ast;

/**
 * Leaf: =
 */
public final class ComponentEquals extends AbstractAstNode {

    public ComponentEquals() {
        super("ComponentEquals");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
