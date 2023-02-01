package com.jessebrault.gcp.ast;

/**
 * Leaf: /
 */
public final class ForwardSlash extends AbstractAstNode {

    public ForwardSlash() {
        super("ForwardSlash");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
