package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.token.Token;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class NotEnoughTokensDiagnostic extends AbstractParserDiagnostic {

    private final Collection<Token.Type> expectedTypes;

    public NotEnoughTokensDiagnostic(Collection<Token.Type> expectedTypes) {
        super(List.of());
        this.expectedTypes = expectedTypes;
    }

    @Override
    public String getMessage() {
        return "Ran out of tokens at end of file. Expected any of: " + this.expectedTypes.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "", "."));
    }

}
