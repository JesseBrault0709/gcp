package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BlockScriptlet extends AbstractAstNode {

    private BlockScriptletStart blockScriptletStart;
    private BlockScriptletText blockScriptletText;
    private BlockScriptletEnd blockScriptletEnd;

    public BlockScriptlet(List<Token> tokens) {
        super("BlockScriptlet", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        final List<AstNode> children = new ArrayList<>();
        children.add(this.blockScriptletStart);
        children.add(this.blockScriptletText);
        children.add(this.blockScriptletEnd);
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public BlockScriptletStart getBlockScriptletStart() {
        return Objects.requireNonNull(this.blockScriptletStart);
    }

    public void setBlockScriptletStart(BlockScriptletStart blockScriptletStart) {
        this.blockScriptletStart = Objects.requireNonNull(blockScriptletStart);
    }

    public BlockScriptletText getBlockScriptletText() {
        return Objects.requireNonNull(this.blockScriptletText);
    }

    public void setBlockScriptletText(BlockScriptletText blockScriptletText) {
        this.blockScriptletText = Objects.requireNonNull(blockScriptletText);
    }

    public BlockScriptletEnd getBlockScriptletEnd() {
        return Objects.requireNonNull(this.blockScriptletEnd);
    }

    public void setBlockScriptletEnd(BlockScriptletEnd blockScriptletEnd) {
        this.blockScriptletEnd = Objects.requireNonNull(blockScriptletEnd);
    }

}
