package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractAstNode implements AstNode {

    private final String nodeTypeName;
    private final List<Token> tokens;

    public AbstractAstNode(String nodeTypeName, List<Token> tokens) {
        this.nodeTypeName = nodeTypeName;
        this.tokens = tokens;
    }

    @Override
    public List<AstNode> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public final List<Token> getTokens() {
        return new ArrayList<>(this.tokens);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.nodeTypeName, this.getChildren());
    }

}
