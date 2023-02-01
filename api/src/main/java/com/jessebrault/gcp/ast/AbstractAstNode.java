package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

abstract class AbstractAstNode implements AstNode {

    private final String nodeTypeName;
    private final List<AstNode> children = new ArrayList<>();
    private final List<Token> tokens = new ArrayList<>();

    public AbstractAstNode(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    @Override
    public final void appendChild(AstNode child) {
        this.children.add(Objects.requireNonNull(child));
        this.addTokens(child.getTokens());
    }

    @Override
    public final List<AstNode> getChildren() {
        return new ArrayList<>(this.children);
    }

    @Override
    public void addToken(Token token) {
        this.tokens.add(token);
    }

    @Override
    public void addTokens(Collection<? extends Token> tokens) {
        this.tokens.addAll(tokens);
    }

    @Override
    public final List<Token> getTokens() {
        return new ArrayList<>(Objects.requireNonNull(this.tokens));
    }

    @Override
    public String getNodeTypeName() {
        return this.nodeTypeName;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.nodeTypeName, this.getChildren());
    }

}
