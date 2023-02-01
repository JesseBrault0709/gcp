package com.jessebrault.gcp.ast;

public class ExpressionScriptlet extends AbstractAstNode {

    public ExpressionScriptlet() {
        super("ExpressionScriptlet");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
