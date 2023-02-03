package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.tokenizer.Token;

import java.util.Collection;
import java.util.List;

public interface ParserAccumulator {
    void startRoot(AstNode.Type type);
    void start(AstNode.Type type);

    void leaf(AstNode.Type type, List<Token> tokens);

    void done();
    void doneRoot();

    void unexpectedToken(Token token, Collection<Token.Type> expectedTypes);

    default void leaf(AstNode.Type type, Token token) {
        this.leaf(type, List.of(token));
    }

}
