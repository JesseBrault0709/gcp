package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;
import java.util.Objects;

/**
 * Leaf: text
 */
public final class Text extends AbstractAstNode {

    private String text;

    public Text(List<Token> tokens) {
        super("Text", tokens);
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
