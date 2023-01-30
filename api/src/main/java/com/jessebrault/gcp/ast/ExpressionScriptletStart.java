package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: <%=
 */
public final class ExpressionScriptletStart extends AbstractAstNode {

    public ExpressionScriptletStart(List<Token> tokens) {
        super("ExpressionScriptletStart", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
