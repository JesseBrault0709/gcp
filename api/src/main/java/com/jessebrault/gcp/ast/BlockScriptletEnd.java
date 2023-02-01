package com.jessebrault.gcp.ast;

/**
 * Leaf: %>
 */
public final class BlockScriptletEnd extends AbstractAstNode {

    public BlockScriptletEnd() {
        super("BlockScriptletEnd");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
