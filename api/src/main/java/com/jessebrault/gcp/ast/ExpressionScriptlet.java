package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionScriptlet extends AbstractAstNode {

    private ExpressionScriptletStart expressionScriptletStart;
    private ExpressionScriptletText expressionScriptletText;
    private ExpressionScriptletEnd expressionScriptletEnd;

    public ExpressionScriptlet(List<Token> tokens) {
        super("ExpressionScriptlet", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.expressionScriptletStart);
        children.add(this.expressionScriptletText);
        children.add(this.expressionScriptletEnd);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public ExpressionScriptletStart getExpressionScriptletStart() {
        return Objects.requireNonNull(this.expressionScriptletStart);
    }

    public void setExpressionScriptletStart(ExpressionScriptletStart expressionScriptletStart) {
        this.expressionScriptletStart = Objects.requireNonNull(expressionScriptletStart);
    }

    public ExpressionScriptletText getExpressionScriptletText() {
        return Objects.requireNonNull(this.expressionScriptletText);
    }

    public void setExpressionScriptletText(ExpressionScriptletText expressionScriptletText) {
        this.expressionScriptletText = Objects.requireNonNull(expressionScriptletText);
    }

    public ExpressionScriptletEnd getExpressionScriptletEnd() {
        return Objects.requireNonNull(this.expressionScriptletEnd);
    }

    public void setExpressionScriptletEnd(ExpressionScriptletEnd expressionScriptletEnd) {
        this.expressionScriptletEnd = Objects.requireNonNull(expressionScriptletEnd);
    }

}
