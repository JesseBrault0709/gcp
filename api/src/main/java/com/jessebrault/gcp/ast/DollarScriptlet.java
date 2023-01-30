package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;
import java.util.Objects;

public final class DollarScriptlet extends AbstractAstNode {

    private Dollar dollar;
    private CurlyOpen curlyOpen;
    private DollarScriptletText dollarScriptletText;
    private CurlyClose curlyClose;

    public DollarScriptlet(List<Token> tokens) {
        super("DollarScriptlet", tokens);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public Dollar getDollar() {
        return Objects.requireNonNull(this.dollar);
    }

    public void setDollar(Dollar dollar) {
        this.dollar = Objects.requireNonNull(dollar);
    }

    public CurlyOpen getCurlyOpen() {
        return Objects.requireNonNull(this.curlyOpen);
    }

    public void setCurlyOpen(CurlyOpen curlyOpen) {
        this.curlyOpen = Objects.requireNonNull(curlyOpen);
    }

    public DollarScriptletText getDollarScriptletText() {
        return Objects.requireNonNull(this.dollarScriptletText);
    }

    public void setDollarScriptletText(DollarScriptletText dollarScriptletText) {
        this.dollarScriptletText = Objects.requireNonNull(dollarScriptletText);
    }

    public CurlyClose getCurlyClose() {
        return Objects.requireNonNull(this.curlyClose);
    }

    public void setCurlyClose(CurlyClose curlyClose) {
        this.curlyClose = Objects.requireNonNull(curlyClose);
    }

}
