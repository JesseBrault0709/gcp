package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SelfClosingComponent extends AbstractAstNode {

    private ComponentStart componentStart;
    private ComponentIdentifier componentIdentifier;
    private ComponentKeysAndValues componentKeysAndValues;
    private ForwardSlash forwardSlash;
    private ComponentEnd componentEnd;

    public SelfClosingComponent(List<Token> tokens) {
        super("SelfClosingComponent", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.componentStart);
        children.add(this.componentIdentifier);
        children.add(this.componentKeysAndValues);
        children.add(this.forwardSlash);
        children.add(this.componentEnd);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public ComponentStart getComponentStart() {
        return Objects.requireNonNull(this.componentStart);
    }

    public void setComponentStart(ComponentStart componentStart) {
        this.componentStart = Objects.requireNonNull(componentStart);
    }

    public ComponentIdentifier getComponentIdentifier() {
        return Objects.requireNonNull(this.componentIdentifier);
    }

    public void setComponentIdentifier(ComponentIdentifier componentIdentifier) {
        this.componentIdentifier = Objects.requireNonNull(componentIdentifier);
    }

    public ComponentKeysAndValues getComponentKeysAndValues() {
        return Objects.requireNonNull(this.componentKeysAndValues);
    }

    public void setComponentKeysAndValues(ComponentKeysAndValues componentKeysAndValues) {
        this.componentKeysAndValues = Objects.requireNonNull(componentKeysAndValues);
    }

    public ForwardSlash getForwardSlash() {
        return Objects.requireNonNull(this.forwardSlash);
    }

    public void setForwardSlash(ForwardSlash forwardSlash) {
        this.forwardSlash = Objects.requireNonNull(forwardSlash);
    }

    public ComponentEnd getComponentEnd() {
        return Objects.requireNonNull(this.componentEnd);
    }

    public void setComponentEnd(ComponentEnd componentEnd) {
        this.componentEnd = Objects.requireNonNull(componentEnd);
    }

}
