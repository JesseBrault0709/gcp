package com.jessebrault.gcp.ast;

import java.util.Objects;

public final class ComponentPackageName extends AbstractAstNode {

    private String packageName;

    public ComponentPackageName() {
        super("ComponentPackageName");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getPackageName() {
        return Objects.requireNonNull(this.packageName);
    }

    public void setPackageName(String packageName) {
        this.packageName = Objects.requireNonNull(packageName);
    }

}
