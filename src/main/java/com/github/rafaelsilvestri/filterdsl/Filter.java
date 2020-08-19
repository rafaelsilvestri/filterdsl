package com.github.rafaelsilvestri.filterdsl;

import com.github.rafaelsilvestri.filterdsl.tree.FilterTree;
import com.github.rafaelsilvestri.filterdsl.tree.FilterTreeVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Objects;

/**
 * Wrapper class that holds the dsl and its tokens.
 *
 * @author Rafael Silvestri
 */
public class Filter {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FilterTree getRootNode() {
        Objects.requireNonNull(this.value, "Filter value cannot be null.");

        //Loading the DSL script into the ANTLR stream.
        CharStream cs = CharStreams.fromString(this.getValue());
        FilterLexer lexer = new FilterLexer(cs);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        FilterParser parser = new FilterParser(commonTokenStream);

        FilterTreeVisitor visitor = new FilterTreeVisitor();
        return visitor.visit(parser.expr());
    }

}
