package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.Diagnostic;
import com.jessebrault.gcp.token.Token;

import java.util.List;

public interface ParserDiagnostic extends Diagnostic {
    String getMessage();
    List<Token> getTokens();
}
