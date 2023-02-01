package com.jessebrault.gcp.ast;

/**
 * Leaf: >
 */
public final class ComponentEnd extends AbstractAstNode {

    public ComponentEnd() {
        super("ComponentEnd");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
         visitor.visit(this);
    }

}
