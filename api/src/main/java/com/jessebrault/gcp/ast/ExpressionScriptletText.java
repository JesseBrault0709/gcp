package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;
import java.util.Objects;

public final class ExpressionScriptletText extends AbstractAstNode {

    private String text;

    public ExpressionScriptletText(List<Token> tokens) {
        super("ExpressionScriptletText", tokens);
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
