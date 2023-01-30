package com.jessebrault.gcp.ast;

import com.jessebrault.gcp.tokenizer.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class ComponentIdentifier extends AbstractAstNode {

    private final List<ComponentPackageName> packageNames = new ArrayList<>();
    private final List<ComponentDot> dots = new ArrayList<>();
    private final List<ComponentClassName> classNames = new ArrayList<>();

    public ComponentIdentifier(List<Token> tokens) {
        super("ComponentIdentifier", tokens);
    }

    @Override
    public List<AstNode> getChildren() {
        if (this.dots.size() != this.packageNames.size() + this.classNames.size() - 1) {
            throw new IllegalStateException();
        }
        final List<AstNode> children = new ArrayList<>();
        final Queue<ComponentDot> dotQueue = new LinkedList<>(this.dots);
        this.packageNames.forEach(packageName -> {
            children.add(packageName);
            if (dotQueue.peek() != null) {
                children.add(dotQueue.remove());
            } else {
                throw new IllegalStateException();
            }
        });
        this.classNames.forEach(className -> {
            children.add(className);
            if (dotQueue.peek() != null) {
                children.add(dotQueue.remove());
            }
        });
        if (!dotQueue.isEmpty()) {
            throw new IllegalStateException();
        }
        return children;
    }

    @Override
    public void accept(AstNodeVisitor visitor) {
        visitor.visit(this);
    }

    public void appendPackage(ComponentPackageName packageName, ComponentDot dot) {
        this.packageNames.add(packageName);
        this.dots.add(dot);
    }

    public void appendClass(ComponentClassName className, ComponentDot dot) {
        this.classNames.add(className);
        this.dots.add(dot);
    }

    public void appendClass(ComponentClassName className) {
        this.classNames.add(className);
    }

}
