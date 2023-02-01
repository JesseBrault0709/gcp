package com.jessebrault.gcp.ast;

/**
 * Leaf: "
 */
public final class GStringDoubleQuote extends AbstractAstNode {

    public GStringDoubleQuote() {
        super("GStringDoubleQuote");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
