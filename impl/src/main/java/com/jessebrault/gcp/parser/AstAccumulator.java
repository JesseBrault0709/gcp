package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.ast.DiagnosticNodeImpl;
import com.jessebrault.gcp.ast.SimpleAstNode;
import com.jessebrault.gcp.tokenizer.Token;

import java.util.*;

public final class AstAccumulator implements ParserAccumulator {

    private AstNode root;
    private final Deque<AstNode> nodeStack = new LinkedList<>();

    public AstNode getResult() {
        return Objects.requireNonNull(this.root);
    }

    @Override
    public void startRoot(AstNode.Type type) {
        this.root = new SimpleAstNode(type);
        this.nodeStack.push(this.root);
    }

    @Override
    public void start(AstNode.Type type) {
        this.nodeStack.push(new SimpleAstNode(type));
    }

    @Override
    public void leaf(AstNode.Type type, List<Token> tokens) {
        this.start(type);
        final var node = this.nodeStack.peek();
        if (node == null) {
            throw new IllegalStateException("node is null");
        }
        node.addTokens(tokens);
        this.done();
    }

    @Override
    public void done() {
        if (this.nodeStack.size() < 2) {
            throw new IllegalStateException("nodeStack.size() is less than 2");
        }
        final var node = this.nodeStack.pop();
        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.addChild(node);
    }

    @Override
    public void doneRoot() {
        if (this.nodeStack.size() != 1 || !this.nodeStack.pop().equals(this.root)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void unexpectedToken(Token token, Collection<Token.Type> expectedTypes) {
        final var diagnosticNode = new DiagnosticNodeImpl();
        diagnosticNode.addDiagnostic(new UnexpectedTokenDiagnostic(token, expectedTypes));
        diagnosticNode.addToken(token);

        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.addChild(diagnosticNode);
    }

}
