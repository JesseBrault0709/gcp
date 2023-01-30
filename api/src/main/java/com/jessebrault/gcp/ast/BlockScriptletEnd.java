package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public final class BlockScriptletEnd extends AbstractAstNode {

    public BlockScriptletEnd(List<Token> tokens) {
        super("BlockScriptletEnd", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
