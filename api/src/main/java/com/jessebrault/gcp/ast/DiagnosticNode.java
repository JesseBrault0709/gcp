package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.Diagnostic;

import java.util.Collection;

public interface DiagnosticNode extends AstNode {
    void addDiagnostic(Diagnostic diagnostic);
    void addAllDiagnostics(Collection<? extends Diagnostic> diagnostics);
    Collection<Diagnostic> getDiagnostics();
}
