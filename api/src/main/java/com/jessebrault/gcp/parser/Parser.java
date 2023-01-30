package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.AstNode;
import com.jessebrault.gcp.tokenizer.Token;

import java.util.List;

public interface Parser {
    AstNode parse(List<Token> tokens);
}
