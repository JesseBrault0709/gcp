package com.jessebrault.gcp.parser;

public interface ParserAccumulator<T> {
    T getResult();
    void start();
    void done();
    void unexpectedToken();
    void notEnoughTokens();
}
