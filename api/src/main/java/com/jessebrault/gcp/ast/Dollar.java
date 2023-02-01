package com.jessebrault.gcp.ast;

/**
 * Leaf: $
 */
public final class Dollar extends AbstractAstNode {

    public Dollar() {
        super("Dollar");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
