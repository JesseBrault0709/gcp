package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.Diagnostic;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractDiagnosticNode extends AbstractAstNode implements DiagnosticNode {

    private final Collection<Diagnostic> diagnostics = new ArrayList<>();

    public AbstractDiagnosticNode(String nodeTypeName) {
        super(nodeTypeName);
    }

    @Override
    public void addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
    }

    @Override
    public void addAllDiagnostics(Collection<? extends Diagnostic> diagnostics) {
        this.diagnostics.addAll(diagnostics);
    }

    @Override
    public Collection<Diagnostic> getDiagnostics() {
        return new ArrayList<>(this.diagnostics);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

}
