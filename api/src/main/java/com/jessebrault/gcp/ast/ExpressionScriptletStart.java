package com.jessebrault.gcp.ast;

/**
 * Leaf: <%=
 */
public final class ExpressionScriptletStart extends AbstractAstNode {

    public ExpressionScriptletStart() {
        super("ExpressionScriptletStart");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
