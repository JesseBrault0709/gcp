package com.jessebrault.gcp.ast;

/**
 * Leaf: <%
 */
public final class BlockScriptletStart extends AbstractAstNode {

    public BlockScriptletStart() {
        super("BlockScriptletStart");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
