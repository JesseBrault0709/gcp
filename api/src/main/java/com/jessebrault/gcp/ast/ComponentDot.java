package com.jessebrault.gcp.ast;

/**
 * Leaf: .
 */
public final class ComponentDot extends AbstractAstNode {

    public ComponentDot() {
        super("ComponentDot");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
