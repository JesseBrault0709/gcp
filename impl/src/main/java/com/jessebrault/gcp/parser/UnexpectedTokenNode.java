package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AbstractDiagnosticNode;

public final class UnexpectedTokenNode extends AbstractDiagnosticNode {

    public UnexpectedTokenNode() {
        super("UnexpectedTokenNode");
    }

}
