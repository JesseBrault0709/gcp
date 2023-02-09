package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.Diagnostic;
import com.jessebrault.gcp.token.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class AstNode {

    public enum Type {
        DOCUMENT,
        TEXT,

        OPENING_COMPONENT,
        SELF_CLOSING_COMPONENT,
        CLOSING_COMPONENT,

        COMPONENT_START,

        COMPONENT_IDENTIFIER,
        INDEX,
        DOT,
        PACKAGE_NAME,
        CLASS_NAME,

        KEYS_AND_VALUES,

        KEY_AND_VALUE,
        KEY,
        EQUALS,

        G_STRING,
        G_STRING_DOUBLE_QUOTE,
        G_STRING_TEXT,

        J_STRING,
        J_STRING_SINGLE_QUOTE,
        J_STRING_TEXT,

        DOLLAR_REFERENCE,
        DOLLAR,
        DOLLAR_REFERENCE_IDENTIFIER,

        DOLLAR_SCRIPTLET,
        CURLY_OPEN,
        DOLLAR_SCRIPTLET_TEXT,
        CURLY_CLOSE,

        EXPRESSION_SCRIPTLET,
        EXPRESSION_SCRIPTLET_START,
        EXPRESSION_SCRIPTLET_TEXT,
        EXPRESSION_SCRIPTLET_END,

        BLOCK_SCRIPTLET,
        BLOCK_SCRIPTLET_START,
        BLOCK_SCRIPTLET_TEXT,
        BLOCK_SCRIPTLET_END,

        FORWARD_SLASH,
        COMPONENT_END,

        UNEXPECTED_TOKEN,
        UNEXPECTED_END_OF_INPUT
    }

    private final Type type;
    private final List<AstNode> children = new ArrayList<>();
    private final List<Token> tokens = new ArrayList<>();
    private final Collection<Diagnostic> diagnostics = new ArrayList<>();

    public AstNode(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void addChild(AstNode child) {
        this.children.add(child);
        this.tokens.addAll(child.getTokens());
    }

    public void addChildren(List<? extends AstNode> children) {
        children.forEach(this::addChild);
    }

    public List<AstNode> getChildren() {
        return new ArrayList<>(this.children);
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public void addTokens(List<? extends Token> tokens) {
        this.tokens.addAll(tokens);
    }

    public List<Token> getTokens() {
        return new ArrayList<>(this.tokens);
    }

    public void addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
    }

    public void addDiagnostics(Collection<? extends Diagnostic> diagnostics) {
        this.diagnostics.addAll(diagnostics);
    }

    public Collection<Diagnostic> getDiagnostics() {
        return new ArrayList<>(this.diagnostics);
    }

    @Override
    public String toString() {
        return String.format("AstNode(%s, %s)", this.type.name(), this.children);
    }

}
