package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.tokenizer.TokenProvider;

public interface Parser {
    void parse(TokenProvider tokenProvider, ParserAccumulator parserAccumulator);
}
