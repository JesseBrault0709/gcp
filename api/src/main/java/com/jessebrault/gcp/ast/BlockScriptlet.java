package com.jessebrault.gcp.ast;

public final class BlockScriptlet extends AbstractAstNode {

    public BlockScriptlet() {
        super("BlockScriptlet");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
