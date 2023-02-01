package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.ast.Document;
import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenIterator;

import java.util.List;

public interface Parser {
    Document parseDocument(TokenIterator tokens);
}
