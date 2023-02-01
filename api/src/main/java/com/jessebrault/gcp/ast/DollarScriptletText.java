package com.jessebrault.gcp.ast;

import java.util.Objects;

public final class DollarScriptletText extends AbstractAstNode {

    private String text;

    public DollarScriptletText() {
        super("DollarScriptletText");
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
