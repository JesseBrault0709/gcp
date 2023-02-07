package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.token.Token;

import java.util.Collection;

public interface ParserAccumulator {

    void startRoot(AstNode.Type type);

    void start(AstNode.Type type);

    void leaf(AstNode.Type type);

    void done();

    void doneRoot();

    void unexpectedToken(Collection<Token.Type> expectedTypes);

}
