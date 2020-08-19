package com.github.rafaelsilvestri.filterdsl.tree;

import com.github.rafaelsilvestri.filterdsl.FilterBaseVisitor;
import com.github.rafaelsilvestri.filterdsl.FilterLexer;
import com.github.rafaelsilvestri.filterdsl.FilterParser;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Filter tree visitor is used to populate the {@link FilterTree} object.
 *
 * @author Rafael Silvestri
 */
public class FilterTreeVisitor extends FilterBaseVisitor<FilterTree> {

    @Override
    public FilterTree visitExpr(FilterParser.ExprContext ctx) {
        return visit(ctx.attribute());
    }

    @Override
    public FilterTree visitAttribute(FilterParser.AttributeContext context) {
        // OpenParen attribute CloseParen
        if (context.OpenParen() != null) {
            FilterTree openCloseParen = new FilterTree();
            openCloseParen.setOpenParen(true);
            openCloseParen.setCloseParen(true);
            FilterTree n = visit(context.attribute(0));
            openCloseParen.getChildren().add(n); //first
            return openCloseParen;
        }

        // attribute logicalOperator attribute
        if (context.logicalOperator() != null) {
            FilterTree booleanOperator = visit(context.logicalOperator());
            booleanOperator.getChildren().add(visit(context.attribute(0)));
            booleanOperator.getChildren().add(visit(context.attribute(1)));
            return booleanOperator;
        }

        // operand operator operand
        if (context.operator() != null) {
            FilterTree operator = visit(context.operator());
            operator.getChildren().add(visit(context.operand(0)));
            operator.getChildren().add(visit(context.operand(1)));
            return operator;
        }

        throw new RuntimeException("Unhandled Attribute " + context.getText());
    }

    @Override
    public FilterTree visitLogicalOperator(FilterParser.LogicalOperatorContext context) {
        TerminalNode terminal = (TerminalNode) context.getChild(0);
        int symbolType = terminal.getSymbol().getType();

        switch (symbolType) {
            case FilterLexer.And:
                return new FilterTree(SymbolType.LogicalOperator, LogicalOperator.And);
            case FilterLexer.Or:
                return new FilterTree(SymbolType.LogicalOperator, LogicalOperator.Or);
        }

        throw new RuntimeException("Unhandled Logical Operator");
    }

    @Override
    public FilterTree visitOperator(FilterParser.OperatorContext context) {
        TerminalNode terminal = (TerminalNode) context.getChild(0);
        int symbolType = terminal.getSymbol().getType();

        switch (symbolType) {
            case FilterLexer.GreaterThan:
                return createOperatorNode(Operator.GreaterThan);
            case FilterLexer.GreaterThanEqual:
                return createOperatorNode(Operator.GreaterThanEqual);
            case FilterLexer.LessThan:
                return createOperatorNode(Operator.LessThan);
            case FilterLexer.LessThanEqual:
                return createOperatorNode(Operator.LessThanEqual);
            case FilterLexer.Equal:
                return createOperatorNode(Operator.Equal);
            case FilterLexer.NotEqual:
                return createOperatorNode(Operator.NotEqual);
            case FilterLexer.Contains:
                return createOperatorNode(Operator.Contains);
        }

        throw new RuntimeException("Unhandled Operator");
    }

    private FilterTree createOperatorNode(Operator value) {
        return new FilterTree(SymbolType.Operator, value);
    }

    @Override
    public FilterTree visitOperand(FilterParser.OperandContext context) {
        TerminalNode terminal = (TerminalNode) context.getChild(0);
        int symbolType = terminal.getSymbol().getType();

        switch (symbolType) {
            case FilterLexer.String:
                return new FilterTree(SymbolType.Operand, new Operand(OperandType.String, terminal.getSymbol().getText().trim()));
            case FilterLexer.Number:
                // even it's a number, do not format as number (e.g.: 900 = 900.0)
                return new FilterTree(SymbolType.Operand, new Operand(OperandType.Number, terminal.getSymbol().getText()));
            case FilterLexer.Property:
                return new FilterTree(SymbolType.Operand, new Operand(OperandType.Property, terminal.getSymbol().getText().trim()));
        }

        throw new RuntimeException("Unhandled Operand");
    }
}
