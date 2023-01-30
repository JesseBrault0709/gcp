package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ComponentValue extends AbstractAstNode {

    private AstNode value;

    public ComponentValue(List<Token> tokens) {
        super("ComponentValue", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.value);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public AstNode getValue() {
        return Objects.requireNonNull(this.value);
    }

    public void setValue(GString gString) {
        this.value = Objects.requireNonNull(gString);
    }

    public void setValue(JString jString) {
        this.value = Objects.requireNonNull(jString);
    }

    public void setValue(DollarReference dollarReference) {
        this.value = Objects.requireNonNull(dollarReference);
    }

    public void setValue(DollarScriptlet dollarScriptlet) {
        this.value = Objects.requireNonNull(dollarScriptlet);
    }

    public void setValue(ExpressionScriptlet expressionScriptlet) {
        this.value = Objects.requireNonNull(expressionScriptlet);
    }

    public void setValue(BlockScriptlet blockScriptlet) {
        this.value = Objects.requireNonNull(blockScriptlet);
    }

}
