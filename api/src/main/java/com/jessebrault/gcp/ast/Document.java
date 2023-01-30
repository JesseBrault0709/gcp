package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Document extends AbstractAstNode {

    private final List<AstNode> children = new ArrayList<>();

    public Document(List<Token> tokens) {
        super("Document", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        return new ArrayList<>(this.children);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public void append(Text text) {
        this.children.add(Objects.requireNonNull(text));
    }

    public void append(OpeningComponent openingComponent) {
        this.children.add(Objects.requireNonNull(openingComponent));
    }

    public void append(SelfClosingComponent selfClosingComponent) {
        this.children.add(Objects.requireNonNull(selfClosingComponent));
    }

    public void append(ClosingComponent closingComponent) {
        this.children.add(Objects.requireNonNull(closingComponent));
    }

    public void append(DollarReference dollarReference) {
        this.children.add(Objects.requireNonNull(dollarReference));
    }

    public void append(DollarScriptlet dollarScriptlet) {
        this.children.add(Objects.requireNonNull(dollarScriptlet));
    }

    public void append(ExpressionScriptlet expressionScriptlet) {
        this.children.add(Objects.requireNonNull(expressionScriptlet));
    }

    public void append(BlockScriptlet blockScriptlet) {
        this.children.add(Objects.requireNonNull(blockScriptlet));
    }

}
