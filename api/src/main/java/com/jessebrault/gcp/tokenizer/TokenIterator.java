package com.jessebrault.gcp.tokenizer;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface TokenIterator extends Iterator<Token> {

    @Nullable Token peekFirst(Collection<Token.Type> anyOf);

    default @Nullable Token peekFirst(Token.Type type) {
        return this.peekFirst(List.of(type));
    }

    default boolean isFirst(Collection<Token.Type> anyOf) {
        return this.peekFirst(anyOf) != null;
    }

    default boolean isFirst(Token.Type type) {
        return this.isFirst(List.of(type));
    }

    @Nullable Token peekSecond(Collection<Token.Type> anyOf);

    default @Nullable Token peekSecond(Token.Type type) {
        return this.peekSecond(List.of(type));
    }

    default boolean isSecond(Collection<Token.Type> anyOf) {
        return this.peekSecond(anyOf) != null;
    }

    default boolean isSecond(Token.Type type) {
        return this.isSecond(List.of(type));
    }

    @Nullable Token peekInfinite(Collection<Token.Type> anyOf);

    default @Nullable Token peekInfinite(Token.Type type) {
        return this.peekInfinite(List.of(type));
    }

}
