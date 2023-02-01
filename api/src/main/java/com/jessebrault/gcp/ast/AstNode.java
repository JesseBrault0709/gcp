package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.Collection;
import java.util.List;

public interface AstNode {
    void appendChild(AstNode child);
    List<AstNode> getChildren();

    void addToken(Token token);
    void addTokens(Collection<? extends Token> tokens);
    List<Token> getTokens();

    void accept(AstNodeVisitor visitor);

    String getNodeTypeName();
}
