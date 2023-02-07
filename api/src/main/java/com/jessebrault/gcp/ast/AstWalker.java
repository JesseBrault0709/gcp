package com.jessebrault.gcp.ast;

public interface AstWalker {
    void document(AstNode document);
    void text(AstNode text);

    void openingComponent(AstNode openingComponent);
    void selfClosingComponent(AstNode selfClosingComponent);
    void closingComponent(AstNode closingComponent);

    void componentStart(AstNode componentStart);

    void componentIdentifier(AstNode componentIdentifier);
    void index(AstNode index);
    void dot(AstNode dot);
    void packageName(AstNode packageName);
    void className(AstNode className);

    void keysAndValues(AstNode keysAndValues);

    void keyAndValue(AstNode keyAndValue);
    void key(AstNode key);
    void equalsNode(AstNode equalsNode);

    void gString(AstNode gString);
    void gStringDoubleQuote(AstNode gStringDoubleQuote);
    void gStringText(AstNode gStringText);

    void jString(AstNode jString);
    void jStringSingleQuote(AstNode jStringSingleQuote);
    void jStringText(AstNode jStringText);

    void dollarReference(AstNode dollarReference);
    void dollar(AstNode dollar);
    void dollarReferenceIdentifier(AstNode dollarReferenceIdentifier);

    void dollarScriptlet(AstNode dollarScriptlet);
    void curlyOpen(AstNode curlyOpen);
    void dollarScriptletText(AstNode dollarScriptletText);
    void curlyClose(AstNode curlyClose);

    void expressionScriptlet(AstNode expressionScriptlet);
    void expressionScriptletStart(AstNode expressionScriptletStart);
    void expressionScriptletText(AstNode expressionScriptletText);
    void expressionScriptletEnd(AstNode expressionScriptletEnd);

    void blockScriptlet(AstNode blockScriptlet);
    void blockScriptletStart(AstNode blockScriptletStart);
    void blockScriptletText(AstNode blockScriptletText);
    void blockScriptletEnd(AstNode blockScriptletEnd);

    void forwardSlash(AstNode forwardSlash);
    void componentEnd(AstNode componentEnd);

    void unexpectedToken(AstNode unexpectedToken);
}
