package com.jessebrault.gcp.ast;

public abstract class AbstractAstWalker implements AstWalker {

    protected void defaultWalk(AstNode node) {
        this.walkChildren(node);
    }

    protected void walkChildren(AstNode node) {
        node.getChildren().forEach(child -> {
            switch (child.getType()) {
                case DOCUMENT -> this.document(child);
                case TEXT -> this.text(child);
                case OPENING_COMPONENT -> this.openingComponent(child);
                case SELF_CLOSING_COMPONENT -> this.selfClosingComponent(child);
                case CLOSING_COMPONENT -> this.closingComponent(child);
                case COMPONENT_START -> this.componentStart(child);
                case COMPONENT_IDENTIFIER -> this.componentIdentifier(child);
                case INDEX -> this.index(child);
                case DOT -> this.dot(child);
                case PACKAGE_NAME -> this.packageName(child);
                case CLASS_NAME -> this.className(child);
                case KEYS_AND_VALUES -> this.keysAndValues(child);
                case KEY_AND_VALUE -> this.keyAndValue(child);
                case KEY -> this.key(child);
                case EQUALS -> this.equalsNode(child);
                case G_STRING -> this.gString(child);
                case G_STRING_DOUBLE_QUOTE -> this.gStringDoubleQuote(child);
                case G_STRING_TEXT -> this.gStringText(child);
                case J_STRING -> this.jString(child);
                case J_STRING_SINGLE_QUOTE -> this.jStringSingleQuote(child);
                case J_STRING_TEXT -> this.jStringText(child);
                case DOLLAR_REFERENCE -> this.dollarReference(child);
                case DOLLAR -> this.dollar(child);
                case DOLLAR_REFERENCE_IDENTIFIER -> this.dollarReferenceIdentifier(child);
                case DOLLAR_SCRIPTLET -> this.dollarScriptlet(child);
                case CURLY_OPEN -> this.curlyOpen(child);
                case DOLLAR_SCRIPTLET_TEXT -> this.dollarScriptletText(child);
                case CURLY_CLOSE -> this.curlyClose(child);
                case EXPRESSION_SCRIPTLET -> this.expressionScriptlet(child);
                case EXPRESSION_SCRIPTLET_START -> this.expressionScriptletStart(child);
                case EXPRESSION_SCRIPTLET_TEXT -> this.expressionScriptletText(child);
                case EXPRESSION_SCRIPTLET_END -> this.expressionScriptletEnd(child);
                case BLOCK_SCRIPTLET -> this.blockScriptlet(child);
                case BLOCK_SCRIPTLET_START -> this.blockScriptletStart(child);
                case BLOCK_SCRIPTLET_TEXT -> this.blockScriptletText(child);
                case BLOCK_SCRIPTLET_END -> this.blockScriptletEnd(child);
                case FORWARD_SLASH -> this.forwardSlash(child);
                case COMPONENT_END -> this.componentEnd(child);
                case UNEXPECTED_TOKEN -> this.unexpectedToken(child);
            }
        });
    }

    @Override
    public void document(AstNode document) {
        this.defaultWalk(document);
    }

    @Override
    public void text(AstNode text) {
        this.defaultWalk(text);
    }

    @Override
    public void openingComponent(AstNode openingComponent) {
        this.defaultWalk(openingComponent);
    }

    @Override
    public void selfClosingComponent(AstNode selfClosingComponent) {
        this.defaultWalk(selfClosingComponent);
    }

    @Override
    public void closingComponent(AstNode closingComponent) {
        this.defaultWalk(closingComponent);
    }

    @Override
    public void componentStart(AstNode componentStart) {
        this.defaultWalk(componentStart);
    }

    @Override
    public void componentIdentifier(AstNode componentIdentifier) {
        this.defaultWalk(componentIdentifier);
    }

    @Override
    public void index(AstNode index) {
        this.defaultWalk(index);
    }

    @Override
    public void dot(AstNode dot) {
        this.defaultWalk(dot);
    }

    @Override
    public void packageName(AstNode packageName) {
        this.defaultWalk(packageName);
    }

    @Override
    public void className(AstNode className) {
        this.defaultWalk(className);
    }

    @Override
    public void keysAndValues(AstNode keysAndValues) {
        this.defaultWalk(keysAndValues);
    }

    @Override
    public void keyAndValue(AstNode keyAndValue) {
        this.defaultWalk(keyAndValue);
    }

    @Override
    public void key(AstNode key) {
        this.defaultWalk(key);
    }

    @Override
    public void equalsNode(AstNode equalsNode) {
        this.defaultWalk(equalsNode);
    }

    @Override
    public void gString(AstNode gString) {
        this.defaultWalk(gString);
    }

    @Override
    public void gStringDoubleQuote(AstNode gStringDoubleQuote) {
        this.defaultWalk(gStringDoubleQuote);
    }

    @Override
    public void gStringText(AstNode gStringText) {
        this.defaultWalk(gStringText);
    }

    @Override
    public void jString(AstNode jString) {
        this.defaultWalk(jString);
    }

    @Override
    public void jStringSingleQuote(AstNode jStringSingleQuote) {
        this.defaultWalk(jStringSingleQuote);
    }

    @Override
    public void jStringText(AstNode jStringText) {
        this.defaultWalk(jStringText);
    }

    @Override
    public void dollarReference(AstNode dollarReference) {
        this.defaultWalk(dollarReference);
    }

    @Override
    public void dollar(AstNode dollar) {
        this.defaultWalk(dollar);
    }

    @Override
    public void dollarReferenceIdentifier(AstNode dollarReferenceIdentifier) {
        this.defaultWalk(dollarReferenceIdentifier);
    }

    @Override
    public void dollarScriptlet(AstNode dollarScriptlet) {
        this.defaultWalk(dollarScriptlet);
    }

    @Override
    public void curlyOpen(AstNode curlyOpen) {
        this.defaultWalk(curlyOpen);
    }

    @Override
    public void dollarScriptletText(AstNode dollarScriptletText) {
        this.defaultWalk(dollarScriptletText);
    }

    @Override
    public void curlyClose(AstNode curlyClose) {
        this.defaultWalk(curlyClose);
    }

    @Override
    public void expressionScriptlet(AstNode expressionScriptlet) {
        this.defaultWalk(expressionScriptlet);
    }

    @Override
    public void expressionScriptletStart(AstNode expressionScriptletStart) {
        this.defaultWalk(expressionScriptletStart);
    }

    @Override
    public void expressionScriptletText(AstNode expressionScriptletText) {
        this.defaultWalk(expressionScriptletText);
    }

    @Override
    public void expressionScriptletEnd(AstNode expressionScriptletEnd) {
        this.defaultWalk(expressionScriptletEnd);
    }

    @Override
    public void blockScriptlet(AstNode blockScriptlet) {
        this.defaultWalk(blockScriptlet);
    }

    @Override
    public void blockScriptletStart(AstNode blockScriptletStart) {
        this.defaultWalk(blockScriptletStart);
    }

    @Override
    public void blockScriptletText(AstNode blockScriptletText) {
        this.defaultWalk(blockScriptletText);
    }

    @Override
    public void blockScriptletEnd(AstNode blockScriptletEnd) {
        this.defaultWalk(blockScriptletEnd);
    }

    @Override
    public void forwardSlash(AstNode forwardSlash) {
        this.defaultWalk(forwardSlash);
    }

    @Override
    public void componentEnd(AstNode componentEnd) {
        this.defaultWalk(componentEnd);
    }

    @Override
    public void unexpectedToken(AstNode unexpectedToken) {
        this.defaultWalk(unexpectedToken);
    }
}
