package com.jessebrault.gcp.ast;

public abstract class AbstractAstNodeVisitor implements AstNodeVisitor {

    protected void visitChildren(AstNode node) {
        node.getChildren().forEach(child -> child.accept(this));
    }
    
    protected void defaultVisit(AstNode node) {
        this.visitChildren(node);
    }
    
    @Override
    public void visit(Document document) {
		this.defaultVisit(document);
    }

    @Override
    public void visit(Text text) {
		this.defaultVisit(text);
    }

    @Override
    public void visit(OpeningComponent openingComponent) {
		this.defaultVisit(openingComponent);
    }

    @Override
    public void visit(SelfClosingComponent selfClosingComponent) {
		this.defaultVisit(selfClosingComponent);
    }

    @Override
    public void visit(ClosingComponent closingComponent) {
		this.defaultVisit(closingComponent);
    }

    @Override
    public void visit(ComponentStart componentStart) {
		this.defaultVisit(componentStart);
    }

    @Override
    public void visit(ComponentIdentifier componentIdentifier) {
		this.defaultVisit(componentIdentifier);
    }

    @Override
    public void visit(ComponentPackageName componentPackageName) {
		this.defaultVisit(componentPackageName);
    }

    @Override
    public void visit(ComponentIdentifierIndex componentIdentifierIndex) {
        this.defaultVisit(componentIdentifierIndex);
    }

    @Override
    public void visit(ComponentDot componentDot) {
		this.defaultVisit(componentDot);
    }

    @Override
    public void visit(ComponentClassName componentClassName) {
		this.defaultVisit(componentClassName);
    }

    @Override
    public void visit(ComponentKeysAndValues componentKeysAndValues) {
		this.defaultVisit(componentKeysAndValues);
    }

    @Override
    public void visit(ComponentKeyAndValue componentKeyAndValue) {
		this.defaultVisit(componentKeyAndValue);
    }

    @Override
    public void visit(ComponentKey componentKey) {
		this.defaultVisit(componentKey);
    }

    @Override
    public void visit(ComponentEquals componentEquals) {
		this.defaultVisit(componentEquals);
    }

    @Override
    public void visit(GString gString) {
		this.defaultVisit(gString);
    }

    @Override
    public void visit(GStringDoubleQuote gStringDoubleQuote) {
		this.defaultVisit(gStringDoubleQuote);
    }

    @Override
    public void visit(GStringText gStringText) {
		this.defaultVisit(gStringText);
    }

    @Override
    public void visit(JString jString) {
		this.defaultVisit(jString);
    }

    @Override
    public void visit(JStringSingleQuote stringSingleQuote) {
		this.defaultVisit(stringSingleQuote);
    }

    @Override
    public void visit(JStringText stringText) {
		this.defaultVisit(stringText);
    }

    @Override
    public void visit(DollarReference dollarReference) {
		this.defaultVisit(dollarReference);
    }

    @Override
    public void visit(Dollar dollar) {
		this.defaultVisit(dollar);
    }

    @Override
    public void visit(DollarReferenceIdentifier dollarReferenceIdentifier) {
		this.defaultVisit(dollarReferenceIdentifier);
    }

    @Override
    public void visit(DollarScriptlet dollarScriptlet) {
		this.defaultVisit(dollarScriptlet);
    }

    @Override
    public void visit(CurlyOpen curlyOpen) {
		this.defaultVisit(curlyOpen);
    }

    @Override
    public void visit(DollarScriptletText dollarScriptletContent) {
		this.defaultVisit(dollarScriptletContent);
    }

    @Override
    public void visit(CurlyClose curlyClose) {
		this.defaultVisit(curlyClose);
    }

    @Override
    public void visit(ExpressionScriptlet expressionScriptlet) {
		this.defaultVisit(expressionScriptlet);
    }

    @Override
    public void visit(ExpressionScriptletStart expressionScriptletStart) {
		this.defaultVisit(expressionScriptletStart);
    }

    @Override
    public void visit(ExpressionScriptletText expressionScriptletText) {
		this.defaultVisit(expressionScriptletText);
    }

    @Override
    public void visit(ExpressionScriptletEnd expressionScriptletEnd) {
		this.defaultVisit(expressionScriptletEnd);
    }

    @Override
    public void visit(BlockScriptlet blockScriptlet) {
		this.defaultVisit(blockScriptlet);
    }

    @Override
    public void visit(BlockScriptletStart blockScriptletStart) {
		this.defaultVisit(blockScriptletStart);
    }

    @Override
    public void visit(BlockScriptletText blockScriptletText) {
		this.defaultVisit(blockScriptletText);
    }

    @Override
    public void visit(BlockScriptletEnd blockScriptletEnd) {
		this.defaultVisit(blockScriptletEnd);
    }

    @Override
    public void visit(ForwardSlash forwardSlash) {
		this.defaultVisit(forwardSlash);
    }

    @Override
    public void visit(ComponentEnd componentEnd) {
		this.defaultVisit(componentEnd);
    }

    @Override
    public void visit(DiagnosticNode diagnosticNode) {
		this.defaultVisit(diagnosticNode);
    }
    
}
