package com.jessebrault.gcp.ast.util;

import com.jessebrault.gcp.ast.AbstractAstWalker;
import com.jessebrault.gcp.ast.AstNode;

public final class AstPrettyPrinter extends AbstractAstWalker {

    private final int indent;
    private final boolean printTokens;
    private final StringBuilder b = new StringBuilder();

    private int currentIndent;

    public AstPrettyPrinter(int indent, boolean printTokens) {
        this.indent = indent;
        this.printTokens = printTokens;
        this.currentIndent = -this.indent;
    }

    public String getResult() {
        return this.b.toString();
    }

    @Override
    protected void defaultWalk(AstNode node) {
        this.currentIndent += this.indent;
        this.b.append(" ".repeat(this.currentIndent));
        this.b.append(node.getType());
        if (this.printTokens) {
            this.b.append(node.getTokens());
        }
        this.b.append("\n");
        super.defaultWalk(node);
        this.currentIndent -= this.indent;
    }

}
