package com.github.rafaelsilvestri.filterdsl.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the filter tree elements.
 *
 * @author Rafael Silvestri
 */
public class FilterTree {
    private boolean openParen;
    private boolean closeParen;
    private SymbolType type;
    private Object value;
    private List<FilterTree> children;

    public FilterTree() {
        this.openParen = false;
        this.closeParen = false;
        this.children = new ArrayList<>();
    }

    public FilterTree(SymbolType type, Object value) {
        this();
        this.type = type;
        this.value = value;
    }

    public boolean isOpenParen() {
        return openParen;
    }

    public void setOpenParen(boolean openParen) {
        this.openParen = openParen;
    }

    public boolean isCloseParen() {
        return closeParen;
    }

    public void setCloseParen(boolean closeParen) {
        this.closeParen = closeParen;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<FilterTree> getChildren() {
        return children;
    }

    public void setChildren(List<FilterTree> children) {
        this.children = children;
    }
}
