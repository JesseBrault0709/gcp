package com.jessebrault.gcp.ast;

public interface AstNodeVisitor {
    void visit(Document document);

    void visit(Text text);

    void visit(OpeningComponent openingComponent);
    void visit(SelfClosingComponent selfClosingComponent);
    void visit(ClosingComponent closingComponent);

    void visit(ComponentStart componentStart); // <
    void visit(ComponentIdentifier componentIdentifier); // com.jessebrault.Component
    void visit(ComponentPackageName componentPackageName); // com
    void visit(ComponentIdentifierIndex componentIdentifierIndex); // .Component
    void visit(ComponentDot componentDot); // .
    void visit(ComponentClassName componentClassName); // Component

    void visit(ComponentKeysAndValues componentKeysAndValues); // key0="Value" key1=$ref

    void visit(ComponentKeyAndValue componentKeyAndValue); // key0="Value"
    void visit(ComponentKey componentKey); // key0
    void visit(ComponentEquals componentEquals); // =

    void visit(GString gString); // "Value"
    void visit(GStringDoubleQuote gStringDoubleQuote); // "
    void visit(GStringText gStringText); // Value

    void visit(JString jString); // 'string'
    void visit(JStringSingleQuote stringSingleQuote); // '
    void visit(JStringText stringText); // string

    void visit(DollarReference dollarReference); // $ref
    void visit(Dollar dollar); // $
    void visit(DollarReferenceIdentifier dollarReferenceIdentifier); // ref.ref

    void visit(DollarScriptlet dollarScriptlet); // ${ 1 + 2 }

    void visit(CurlyOpen curlyOpen); // ${
    void visit(DollarScriptletText dollarScriptletContent); // 1 + 2
    void visit(CurlyClose curlyClose); // }

    void visit(ExpressionScriptlet expressionScriptlet); // <%= 1 + 2 %>
    void visit(ExpressionScriptletStart expressionScriptletStart); // <%=
    void visit(ExpressionScriptletText expressionScriptletText); // 1 + 2
    void visit(ExpressionScriptletEnd expressionScriptletEnd); // %>

    void visit(BlockScriptlet blockScriptlet); // <% out << 'Hello, World!' %>
    void visit(BlockScriptletStart blockScriptletStart); // <%
    void visit(BlockScriptletText blockScriptletText); // out << 'Hello, World!'
    void visit(BlockScriptletEnd blockScriptletEnd); // %>

    void visit(ForwardSlash forwardSlash); // /
    void visit(ComponentEnd componentEnd); // >

    void visit(DiagnosticNode diagnosticNode);
}
