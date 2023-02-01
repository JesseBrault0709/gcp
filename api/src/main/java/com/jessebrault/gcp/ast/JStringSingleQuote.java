package com.jessebrault.gcp.ast;

/**
 * Leaf: '
 */
public final class JStringSingleQuote extends AbstractAstNode {

    public JStringSingleQuote() {
        super("JStringSingleQuote");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
