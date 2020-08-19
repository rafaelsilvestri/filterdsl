package com.github.rafaelsilvestri.filterdsl;

import com.github.rafaelsilvestri.filterdsl.tree.*;

import java.util.Collections;
import java.util.Map;


/**
 * Implementation of {@link com.github.rafaelsilvestri.filterdsl.FilterBuilder} to build SQL (Structured Query Language)
 * fragment from the filter-dsl.
 *
 * @author Rafael Silvestri
 */
public class SqlSyntaxBuilder implements FilterBuilder {

    private Map<String, String> dictionary;

    @Override
    public String build(final Filter filter) {
        return this.build(filter, Collections.emptyMap());
    }

    @Override
    public String build(final Filter filter, final Map<String, String> dictionary) {
        this.dictionary = dictionary;
        return this.visit(filter.getRootNode());
    }

    private String visit(FilterTree node) {
        if (node.getType() == SymbolType.LogicalOperator)
            return VisitBooleanOperator(node);
        if (node.getType() == SymbolType.Operator)
            return VisitOperator(node);
        if (node.isOpenParen())
            return visitParenthesis(node);

        throw new RuntimeException("Unable to create from search query from tree");
    }

    private String visitParenthesis(FilterTree node) {
        return "(" + visit(node.getChildren().get(0)) + ")";
    }

    private String VisitBooleanOperator(FilterTree node) {
        LogicalOperator value = (LogicalOperator) node.getValue();

        FilterTree attr1 = node.getChildren().get(0);
        FilterTree attr2 = node.getChildren().get(1);

        if (value == LogicalOperator.And)
            return visit(attr1) + " AND " + visit(attr2);

        return visit(attr1) + " OR " + visit(attr2);
    }


    private String VisitOperator(FilterTree node) {
        Operator value = (Operator) node.getValue();
        Operand op1 = (Operand) node.getChildren().get(0).getValue();
        Operand op2 = (Operand) node.getChildren().get(1).getValue();

        if (op1.getType() != OperandType.Property) {
            throw new RuntimeException("First operand must be a property");
        }

        if (op2.getType() == OperandType.Property) {
            throw new RuntimeException("Second operand cannot be a property");
        }

        final String property = dictionary.isEmpty() ? (String) op1.getValue() : dictionary.get(op1.getValue());
        if (property == null) {
            throw new RuntimeException("Property " + op1.getValue() + " does not exist");
        }

        switch (value) {
            case Contains:
                if (op2.getType() != OperandType.String) {
                    throw new RuntimeException("Second operand must be string");
                }
                return property + " LIKE " + "'%" + ((String) op2.getValue()).replace("'", "") + "%'";
            case Equal:
                // can be a string or number
                return property + " = " + op2.getValue();
            case NotEqual:
                return property + " <> " + op2.getValue();
            case LessThan:
                return property + " < " + op2.getValue();
            case LessThanEqual:
                return property + " <= " + op2.getValue();
            case GreaterThan:
                return property + " > " + op2.getValue();
            case GreaterThanEqual:
                return property + " >= " + op2.getValue();
            default:
                throw new RuntimeException("Unknown Operator " + node.getValue());
        }
    }
}
