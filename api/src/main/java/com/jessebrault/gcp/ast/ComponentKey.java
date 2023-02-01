package com.jessebrault.gcp.ast;

import java.util.Objects;

/**
 * Leaf: key
 */
public final class ComponentKey extends AbstractAstNode {

    private String key;

    public ComponentKey() {
        super("ComponentKey");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getKey() {
        return Objects.requireNonNull(this.key);
    }

    public void setKey(String key) {
        this.key = Objects.requireNonNull(key);
    }

}
