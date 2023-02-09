package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.token.Token;
import com.jessebrault.gcp.token.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class AstAccumulator implements ParserAccumulator {

    private static final Logger logger = LoggerFactory.getLogger(AstAccumulator.class);

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
        this.root = new AstNode(type);
        this.nodeStack.push(this.root);
    }

    @Override
    public void start(AstNode.Type type) {
        this.nodeStack.push(new AstNode(type));
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
    }

    @Override
    public void doneRoot() {
        if (this.nodeStack.size() != 1) {
            throw new IllegalStateException("the nodeStack has a size other than 1");
        } else if (!this.nodeStack.pop().equals(this.root)) {
            throw new IllegalStateException("bottom of nodeStack is not the root");
        }
    }

    @Override
    public void unexpectedToken(Collection<Token.Type> expectedTypes) {
        final var diagnosticNode = new AstNode(AstNode.Type.UNEXPECTED_TOKEN);
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
    }

    @Override
    public void unexpectedEndOfInput(Collection<Token.Type> expectedTypes) {
        final var diagnosticNode = new AstNode(AstNode.Type.UNEXPECTED_END_OF_INPUT);
        diagnosticNode.addDiagnostic(new NotEnoughTokensDiagnostic(expectedTypes));

        final var parent = this.nodeStack.peek();
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.addChild(diagnosticNode);
    }

    @Override
    public String toString() {
        return "AstAccumulator(root: " + this.root + ", nodeStack: " + this.nodeStack + ")";
    }

}
