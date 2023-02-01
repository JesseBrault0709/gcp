package com.jessebrault.gcp.ast;

import java.util.Objects;

/**
 * Leaf: text
 */
public final class Text extends AbstractAstNode {

    private String text;

    public Text() {
        super("Text");
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
