package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ClosingComponent extends AbstractAstNode {

    private ComponentStart componentStart;
    private ForwardSlash forwardSlash;
    private ComponentIdentifier componentIdentifier;
    private ComponentEnd componentEnd;

    public ClosingComponent(List<Token> tokens) {
        super("ClosingComponent", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.componentStart);
        children.add(this.forwardSlash);
        children.add(this.componentIdentifier);
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

    public ForwardSlash getForwardSlash() {
        return Objects.requireNonNull(this.forwardSlash);
    }

    public void setForwardSlash(ForwardSlash forwardSlash) {
        this.forwardSlash = Objects.requireNonNull(forwardSlash);
    }

    public ComponentIdentifier getComponentIdentifier() {
        return Objects.requireNonNull(this.componentIdentifier);
    }

    public void setComponentIdentifier(ComponentIdentifier componentIdentifier) {
        this.componentIdentifier = Objects.requireNonNull(componentIdentifier);
    }

    public ComponentEnd getComponentEnd() {
        return Objects.requireNonNull(this.componentEnd);
    }

    public void setComponentEnd(ComponentEnd componentEnd) {
        this.componentEnd = Objects.requireNonNull(componentEnd);
    }

}
