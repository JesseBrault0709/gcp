package com.jessebrault.gcp.ast;

/**
 * Leaf: %>
 */
public final class ExpressionScriptletEnd extends AbstractAstNode {

    public ExpressionScriptletEnd() {
        super("ExpressionScriptletEnd");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
