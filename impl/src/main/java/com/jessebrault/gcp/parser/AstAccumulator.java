package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.ast.DiagnosticNodeImpl;
import com.jessebrault.gcp.ast.SimpleAstNode;
import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenProvider;

import java.util.*;

public final class AstAccumulator implements ParserAccumulator {

    private final TokenProvider tokenProvider;
    private AstNode root;
    private final Deque<AstNode> nodeStack = new LinkedList<>();

    public AstAccumulator(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

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
    public void leaf(AstNode.Type type) {
        this.start(type);
        final var node = this.nodeStack.peek();
        if (node == null) {
            throw new IllegalStateException("node is null");
        }
        final var token = this.tokenProvider.getCurrent();
        if (token == null) {
            throw new IllegalStateException("token is null");
        }
        node.addToken(token);
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
        this.tokenProvider.advance();
    }

    @Override
    public void doneRoot() {
        if (this.nodeStack.size() != 1 || !this.nodeStack.pop().equals(this.root)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void unexpectedToken(Collection<Token.Type> expectedTypes) {
        final var diagnosticNode = new DiagnosticNodeImpl();
        final var token = this.tokenProvider.getCurrent();
        if (token == null) {
            throw new IllegalStateException("token is null");
        }
        diagnosticNode.addDiagnostic(new UnexpectedTokenDiagnostic(token, expectedTypes));
        diagnosticNode.addToken(token);

        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.addChild(diagnosticNode);

        this.tokenProvider.advance();
    }

}
