package com.github.rafaelsilvestri.filterdsl;

import java.util.Map;

/**
 * Implementation of {@link com.github.rafaelsilvestri.filterdsl.FilterBuilder} to build
 * Elastic Search Syntax from filter-dsl.
 *
 * @author Rafael Silvestri
 */
public class ElasticSearchSyntaxBuilder implements FilterBuilder {

    @Override
    public String build(Filter filter) {
        throw new IllegalStateException("Not implemented yet.");
    }

    @Override
    public String build(Filter filter, Map<String, String> dictionary) {
        throw new IllegalStateException("Not implemented yet.");
    }
}
