package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ComponentKeyAndValue extends AbstractAstNode {

    private ComponentKey key;
    private ComponentEquals equals;
    private ComponentValue value;

    public ComponentKeyAndValue(List<Token> tokens) {
        super("ComponentKeyAndValue", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.key);
        children.add(this.equals);
        children.add(this.value);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public ComponentKey getKey() {
        return Objects.requireNonNull(this.key);
    }

    public void setKey(ComponentKey key) {
        this.key = Objects.requireNonNull(key);
    }

    public ComponentEquals getEquals() {
        return Objects.requireNonNull(this.equals);
    }

    public void setEquals(ComponentEquals equals) {
        this.equals = Objects.requireNonNull(equals);
    }

    public ComponentValue getValue() {
        return Objects.requireNonNull(this.value);
    }

    public void setValue(ComponentValue value) {
        this.value = Objects.requireNonNull(value);
    }

}
