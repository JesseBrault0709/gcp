package com.jessebrault.gcp.ast;

public final class DollarScriptlet extends AbstractAstNode {

    public DollarScriptlet() {
        super("DollarScriptlet");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
