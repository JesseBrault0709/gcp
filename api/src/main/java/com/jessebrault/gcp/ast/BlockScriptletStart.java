package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: <%
 */
public final class BlockScriptletStart extends AbstractAstNode {

    public BlockScriptletStart(List<Token> tokens) {
        super("BlockScriptletStart", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
