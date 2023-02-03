package com.jessebrault.gcp.parser;

import com.jessebrault.gcp.tokenizer.Token;
import com.jessebrault.gcp.tokenizer.TokenIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Collection;
import java.util.List;

import static com.jessebrault.gcp.tokenizer.Token.Type.*;

public abstract class AbstractParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParser.class);
    private static final Marker enter = MarkerFactory.getMarker("ENTER");
    private static final Marker exit = MarkerFactory.getMarker("EXIT");

    protected TokenIterator tokenIterator;

    @Override
    public void parse(TokenIterator tokenIterator) {
        this.tokenIterator = tokenIterator;
        this.document();
    }

    protected abstract void beforeDocument();

    private void document() {
        this.beforeDocument();

        while (this.tokenIterator.hasNext()) {
            if (this.tokenIterator.isFirst(TEXT)) {
                this.text();
            } else if (this.tokenIterator.isFirst(COMPONENT_START)) {
                throw new UnsupportedOperationException();
            } else if (this.tokenIterator.isFirst(DOLLAR)) {
                throw new UnsupportedOperationException();
            } else if (this.tokenIterator.isFirst(EXPRESSION_SCRIPTLET_OPEN)) {
                throw new UnsupportedOperationException();
            } else if(this.tokenIterator.isFirst(BLOCK_SCRIPTLET_OPEN)) {
                throw new UnsupportedOperationException();
            } else {
                this.documentUnexpected(
                        List.of(TEXT, COMPONENT_START, DOLLAR, EXPRESSION_SCRIPTLET_OPEN, BLOCK_SCRIPTLET_OPEN)
                );
            }
        }

        this.afterDocument();
    }

    protected abstract void documentUnexpected(Collection<Token.Type> expectedTypes);

    protected abstract void afterDocument();

    protected abstract void beforeText();

    private void text() {
        this.beforeText();
        if (this.tokenIterator.isFirst(TEXT)) {
            this.afterText(this.tokenIterator.next());
        } else {
            throw new IllegalArgumentException("did not peek for TEXT");
        }
    }

    protected abstract void afterText(Token textToken);

}
