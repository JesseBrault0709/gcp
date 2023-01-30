package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

/**
 * Leaf: }
 */
public final class CurlyClose extends AbstractAstNode {

    public CurlyClose(List<Token> tokens) {
        super("CurlyClose", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
