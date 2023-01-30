package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DollarReference extends AbstractAstNode {

    private Dollar dollar;
    private DollarReferenceIdentifier dollarReferenceIdentifier;

    public DollarReference(List<Token> tokens) {
        super("DollarReference", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.dollar);
        children.add(this.dollarReferenceIdentifier);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public Dollar getDollarReferenceDollar() {
        return Objects.requireNonNull(this.dollar);
    }

    public void setDollarReferenceDollar(Dollar dollar) {
        this.dollar = Objects.requireNonNull(dollar);
    }

    public DollarReferenceIdentifier getDollarReferenceIdentifier() {
        return Objects.requireNonNull(this.dollarReferenceIdentifier);
    }

    public void setDollarReferenceIdentifier(DollarReferenceIdentifier dollarReferenceIdentifier) {
        this.dollarReferenceIdentifier = Objects.requireNonNull(dollarReferenceIdentifier);
    }

}
