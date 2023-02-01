package com.jessebrault.gcp.ast;

public final class Document extends AbstractAstNode {

    public Document() {
        super("Document");
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
