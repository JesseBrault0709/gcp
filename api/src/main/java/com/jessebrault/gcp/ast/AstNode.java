package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.Collection;
import java.util.List;

public interface AstNode {

    enum Type {
        DOCUMENT,
        TEXT,

        OPENING_COMPONENT,
        SELF_CLOSING_COMPONENT,
        CLOSING_COMPONENT,

        LESS_THAN,

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
        GREATER_THAN,

        DIAGNOSTIC_NODE
    }

    Type getType();

    void addChild(AstNode child);
    void addChildren(List<? extends AstNode> children);
    List<AstNode> getChildren();

    void addToken(Token token);
    void addTokens(List<? extends Token> tokens);
    List<Token> getTokens();
}
