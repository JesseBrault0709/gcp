package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.token.Token;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractParserDiagnostic implements ParserDiagnostic {

    private final List<Token> tokens;

    public AbstractParserDiagnostic(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public final List<Token> getTokens() {
        return new ArrayList<>(this.tokens);
    }

}
