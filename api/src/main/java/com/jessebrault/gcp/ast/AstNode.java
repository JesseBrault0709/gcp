package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public interface AstNode {
    List<AstNode> getChildren();

    List<Token> getTokens();

    void accept(AstNodeVisitor visitor);
}
