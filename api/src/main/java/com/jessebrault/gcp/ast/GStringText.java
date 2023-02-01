package com.jessebrault.gcp.ast;

import java.util.Objects;

public final class GStringText extends AbstractAstNode {

    private String text;

    public GStringText() {
        super("GStringText");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return Objects.requireNonNull(this.text);
    }

    public void setText(String text) {
        this.text = Objects.requireNonNull(text);
    }

}
