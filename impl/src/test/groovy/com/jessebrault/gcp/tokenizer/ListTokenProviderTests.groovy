package com.jessebrault.gcp.tokenizer

class ListTokenProviderTests extends AbstractTokenProviderTests {

    @Override
    protected TokenProvider getTokenProvider(List<Token> tokens) {
        new ListTokenProvider(tokens)
    }

}
