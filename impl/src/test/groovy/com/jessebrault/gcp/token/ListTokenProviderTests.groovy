package com.jessebrault.gcp.token

class ListTokenProviderTests extends AbstractTokenProviderTests {

    @Override
    protected TokenProvider getTokenProvider(List<Token> tokens) {
        new ListTokenProvider(tokens)
    }

}
