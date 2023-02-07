package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.token.Token;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class UnexpectedTokenDiagnostic extends AbstractParserDiagnostic {

    private final Token unexpected;
    private final Collection<Token.Type> expectedTypes;

    public UnexpectedTokenDiagnostic(Token unexpected, Collection<Token.Type> expectedTypes) {
        super(List.of(unexpected));
        this.unexpected = unexpected;
        this.expectedTypes = expectedTypes;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Unexpected token: %s. Expected any of: %s",
                this.unexpected,
                this.expectedTypes.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ", "", "."))
        );
    }

}
