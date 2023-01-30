package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GString extends AbstractAstNode {

    private GStringDoubleQuote startDoubleQuote;
    private GStringText text;
    private GStringDoubleQuote endDoubleQuote;

    public GString(List<Token> tokens) {
        super("GStringValue", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.startDoubleQuote);
        children.add(this.text);
        children.add(this.endDoubleQuote);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public GStringDoubleQuote getStartDoubleQuote() {
        return Objects.requireNonNull(this.startDoubleQuote);
    }

    public void setStartDoubleQuote(GStringDoubleQuote startDoubleQuote) {
        this.startDoubleQuote = Objects.requireNonNull(startDoubleQuote);
    }

    public GStringText getText() {
        return Objects.requireNonNull(this.text);
    }

    public void setText(GStringText text) {
        this.text = Objects.requireNonNull(text);
    }

    public GStringDoubleQuote getEndDoubleQuote() {
        return Objects.requireNonNull(this.endDoubleQuote);
    }

    public void setEndDoubleQuote(GStringDoubleQuote endDoubleQuote) {
        this.endDoubleQuote = Objects.requireNonNull(endDoubleQuote);
    }

}
