package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JString extends AbstractAstNode {

    private JStringSingleQuote startSingleQuote;
    private JStringText text;
    private JStringSingleQuote endSingleQuote;

    public JString(List<Token> tokens) {
        super("JString", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.startSingleQuote);
        children.add(this.text);
        children.add(this.endSingleQuote);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public JStringSingleQuote getStartSingleQuote() {
        return Objects.requireNonNull(this.startSingleQuote);
    }

    public void setStartSingleQuote(JStringSingleQuote startSingleQuote) {
        this.startSingleQuote = Objects.requireNonNull(startSingleQuote);
    }

    public JStringText getText() {
        return Objects.requireNonNull(this.text);
    }

    public void setText(JStringText text) {
        this.text = Objects.requireNonNull(text);
    }

    public JStringSingleQuote getEndSingleQuote() {
        return Objects.requireNonNull(this.endSingleQuote);
    }

    public void setEndSingleQuote(JStringSingleQuote endSingleQuote) {
        this.endSingleQuote = Objects.requireNonNull(endSingleQuote);
    }

}
