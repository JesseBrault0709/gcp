package com.jessebrault.gcp.ast;

/**
 * Leaf: }
 */
public final class CurlyClose extends AbstractAstNode {

    public CurlyClose() {
        super("CurlyClose");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
