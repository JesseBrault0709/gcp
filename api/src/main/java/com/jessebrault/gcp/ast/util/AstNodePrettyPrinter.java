package com.jessebrault.gcp.ast.util;

import com.jessebrault.gcp.ast.AbstractAstNodeVisitor;
import com.jessebrault.gcp.ast.AstNode;

public final class AstNodePrettyPrinter extends AbstractAstNodeVisitor {

    private final int indent;
    private final boolean printTokens;
    private int currentIndent;

    private final StringBuilder b = new StringBuilder();

    public AstNodePrettyPrinter(int indent, boolean printTokens) {
        this.indent = indent;
        this.currentIndent = -indent;
        this.printTokens = printTokens;
    }

    public String getResult() {
        return this.b.toString();
    }

    @Override
    protected void defaultVisit(AstNode node) {
        this.currentIndent += this.indent;
        this.b.append(" ".repeat(Math.max(0, this.currentIndent)));
        this.b.append(node.getNodeTypeName());
        if (this.printTokens) {
            this.b.append("(")
                    .append(node.getTokens())
                    .append(")");
        }
        this.b.append("\n");
        super.defaultVisit(node);
        this.currentIndent -= this.indent;
    }

}
