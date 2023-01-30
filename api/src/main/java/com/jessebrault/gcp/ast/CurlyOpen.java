package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: {
 */
public final class CurlyOpen extends AbstractAstNode {

    public CurlyOpen(List<Token> tokens) {
        super("CurlyOpen", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
