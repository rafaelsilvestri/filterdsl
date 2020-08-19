package com.github.rafaelsilvestri.filterdsl;

import java.util.Map;

/**
 * Interface that define a single builder method used to build different syntax from filter dsl.
 *
 * @author Rafael Silvestri
 */
public interface FilterBuilder {

    /**
     * Builds a syntax fragment for the given implementation. Using this method, the whitelist validation will be bypassed.
     *
     * @param filter filter value
     * @return query fragment
     */
    String build(Filter filter);

    /**
     * Builds a syntax fragment for the given implementation.
     *
     * @param filter     filter value
     * @param dictionary whitelist with the allowed properties, if empty bypass the validation.
     * @return query fragment
     */
    String build(Filter filter, Map<String, String> dictionary);

}
