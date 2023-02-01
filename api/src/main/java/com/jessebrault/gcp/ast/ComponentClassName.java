package com.jessebrault.gcp.ast;

import java.util.Objects;

/**
 * Leaf: ClassName
 */
public final class ComponentClassName extends AbstractAstNode {

    private String className;

    public ComponentClassName() {
        super("ComponentClassName");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getClassName() {
        return Objects.requireNonNull(this.className);
    }

    public void setClassName(String className) {
        this.className = Objects.requireNonNull(className);
    }

}
