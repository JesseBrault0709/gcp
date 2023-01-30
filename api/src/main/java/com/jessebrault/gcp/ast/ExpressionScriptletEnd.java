package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: %>
 */
public final class ExpressionScriptletEnd extends AbstractAstNode {

    public ExpressionScriptletEnd(List<Token> tokens) {
        super("ExpressionScriptletEnd", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
