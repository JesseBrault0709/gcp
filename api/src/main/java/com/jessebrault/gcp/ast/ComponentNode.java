package com.jessebrault.gcp.ast;

public sealed interface ComponentNode extends AstNode
        permits OpeningComponent, SelfClosingComponent, ClosingComponent {}
