package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.tokenizer.TokenIterator;

public interface Parser {
    void parse(TokenIterator tokenIterator, ParserAccumulator parserAccumulator);
}
