package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.ast.Document;
import com.jessebrault.gcp.ast.Text;
import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenIterator;

import java.util.*;

public final class AstBuildingParser extends AbstractParser {

    private Deque<AstNode> nodeStack;
    private Document document;

    public Document getDocument() {
        return Objects.requireNonNull(this.document);
    }

    @Override
    public void parse(TokenIterator tokenIterator) {
        this.nodeStack = new LinkedList<>();
        super.parse(tokenIterator);
    }

    @Override
    protected void beforeDocument() {
        this.nodeStack.push(new Document());
    }

    @Override
    protected void documentUnexpected(Collection<Token.Type> expectedTypes) {
        final var document = this.nodeStack.peek();
        if (document == null) {
            throw new IllegalStateException();
        }
        this.unexpectedTokenNode(expectedTypes);
    }

    @Override
    protected void afterDocument() {
        if (this.nodeStack.size() != 1 || !(this.nodeStack.peek() instanceof Document)) {
            throw new IllegalStateException();
        }
        this.document = (Document) this.nodeStack.pop();
    }

    @Override
    protected void beforeText() {
        this.nodeStack.push(new Text());
    }

    @Override
    protected void afterText(Token textToken) {
        if (!(this.nodeStack.peek() instanceof Text)) {
            throw new IllegalStateException();
        }
        final var text = (Text) this.nodeStack.pop();
        text.setText(textToken.getText().toString());
        text.addToken(textToken);

        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.appendChild(text);
    }

    private void unexpectedTokenNode(Collection<Token.Type> expectedTypes) {
        this.unexpectedTokenNode(expectedTypes, List.of());
    }

    private void unexpectedTokenNode(Collection<Token.Type> expectedTypes, List<AstNode> children) {
        if (!this.tokenIterator.hasNext()) {
            throw new RuntimeException("ran out of tokens");
        }
        final var unexpectedToken = tokenIterator.next();

        final var unexpectedTokenNode = new UnexpectedTokenNode();
        unexpectedTokenNode.addDiagnostic(new UnexpectedTokenDiagnostic(unexpectedToken, expectedTypes));

        children.forEach(unexpectedTokenNode::appendChild);

        unexpectedTokenNode.addToken(unexpectedToken);

        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.appendChild(unexpectedTokenNode);
    }

}
