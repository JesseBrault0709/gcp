package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;
import java.util.Objects;

public final class GStringText extends AbstractAstNode {

    private String text;

    public GStringText(List<Token> tokens) {
        super("GStringText", tokens);
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
