package com.jessebrault.gcp.ast;

import java.util.Objects;

public final class DollarReferenceIdentifier extends AbstractAstNode {

    private String identifier;

    public DollarReferenceIdentifier() {
        super("DollarReferenceIdentifier");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getIdentifier() {
        return Objects.requireNonNull(this.identifier);
    }

    public void setIdentifier(String identifier) {
        this.identifier = Objects.requireNonNull(identifier);
    }

}
