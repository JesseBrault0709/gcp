package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAstNode implements AstNode {

    private final Type type;
    private final List<AstNode> children = new ArrayList<>();
    private final List<Token> tokens = new ArrayList<>();

    public AbstractAstNode(Type type) {
        this.type = type;
    }

    @Override
    public final Type getType() {
        return this.type;
    }

    @Override
    public final void addChild(AstNode child) {
        this.children.add(child);
        this.tokens.addAll(child.getTokens());
    }

    @Override
    public final void addChildren(List<? extends AstNode> children) {
        children.forEach(this::addChild);
    }

    @Override
    public final List<AstNode> getChildren() {
        return new ArrayList<>(this.children);
    }

    @Override
    public final void addToken(Token token) {
        this.tokens.add(token);
    }

    @Override
    public final void addTokens(List<? extends Token> tokens) {
        this.tokens.addAll(tokens);
    }

    @Override
    public final List<Token> getTokens() {
        return new ArrayList<>(this.tokens);
    }

    @Override
    public String toString() {
        return String.format("AstNode(%s, %s)", this.type.name(), this.children);
    }

}
