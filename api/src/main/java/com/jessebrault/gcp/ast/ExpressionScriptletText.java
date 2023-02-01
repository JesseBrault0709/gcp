package com.jessebrault.gcp.ast;

import java.util.Objects;

public final class ExpressionScriptletText extends AbstractAstNode {

    private String text;

    public ExpressionScriptletText() {
        super("ExpressionScriptletText");
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
