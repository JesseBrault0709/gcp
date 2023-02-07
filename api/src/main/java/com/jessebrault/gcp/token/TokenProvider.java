package com.jessebrault.gcp.token;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface TokenProvider {

    @Nullable Token getCurrent();

    boolean peekCurrent(Token.Type type);

    boolean peekSecond(Token.Type type);

    boolean peekInfinite(Token.Type type, Collection<Token.Type> failOn);

    default boolean peekInfinite(Token.Type type, Token.Type failOn) {
        return this.peekInfinite(type, List.of(failOn));
    }

    void advance();

}
