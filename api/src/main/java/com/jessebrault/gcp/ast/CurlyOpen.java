package com.jessebrault.gcp.ast;

/**
 * Leaf: {
 */
public final class CurlyOpen extends AbstractAstNode {

    public CurlyOpen() {
        super("CurlyOpen");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
