package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public final class ComponentKeysAndValues extends AbstractAstNode {

    private final List<ComponentKeyAndValue> keysAndValues = new ArrayList<>();

    public ComponentKeysAndValues(List<Token> tokens) {
        super("ComponentKeysAndValues", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        return new ArrayList<>(this.keysAndValues);
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public void append(ComponentKeyAndValue keyAndValue) {
        this.keysAndValues.add(keyAndValue);
    }

}
