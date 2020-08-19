package com.github.rafaelsilvestri.filterdsl;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SqlSyntaxBuilder test class.
 *
 * @author Rafael Silvestri
 */
class SqlSyntaxBuilderTest {

    @Test
    void shouldGenerateSqlFragmentFromSimpleExpression() {
        String dsl = "price gt 100";
        String expectedSql = "price > 100";

        Filter filter = new Filter();
        filter.setValue(dsl);

        FilterBuilder builder = new SqlSyntaxBuilder();
        String actualSql = builder.build(filter);

        assertEquals(expectedSql, actualSql);
    }

    @Test
    void shouldGenerateSqlFragmentWithPrecedenceGrouping() {
        String dsl = "(priority eq 1 or city eq 'Redmond') and price gt 100";
        String expectedSql = "(priority = 1 OR city = 'Redmond') AND price > 100";

        Filter filter = new Filter();
        filter.setValue(dsl);

        FilterBuilder builder = new SqlSyntaxBuilder();
        String actualSql = builder.build(filter);

        assertEquals(expectedSql, actualSql);
    }

    @Test
    void shouldGenerateSqlFragmentFromFilterDsl() {
        String dsl =
                "(current_job_title contains 'software developer'" +
                        " or current_job_title contains 'software engineer'" +
                        " or (current_job_title eq 'a' and past_job_tile eq 'b')" +
                        ")" +
                        " and experience_years gt 5" +
                        " and salary lt 90000";

        String expectedSql =
                "(current_job_title LIKE '%software developer%'" +
                        " OR current_job_title LIKE '%software engineer%'" +
                        " OR (current_job_title = 'a' AND past_job_tile = 'b')" +
                        ")" +
                        " AND experience_years > 5" +
                        " AND salary < 90000";

        Filter filter = new Filter();
        filter.setValue(dsl);

        FilterBuilder builder = new SqlSyntaxBuilder();
        String actualSql = builder.build(filter);

        assertEquals(expectedSql, actualSql);
    }

    /**
     * The grammar validates a property is the first operand.
     */
    @Test
    void shouldPreventSqlInjection() {
        String dsl = "customer eq 'foo' or 'admin' eq 'admin'";

        Filter filter = new Filter();
        filter.setValue(dsl);
        FilterBuilder builder = new SqlSyntaxBuilder();
        try {
            builder.build(filter);
            fail("Should throws an exception.");
        } catch (RuntimeException e) {
            assertEquals("First operand must be a property", e.getMessage());
        }
    }

    /**
     * Using a dictionary, we can define a whitelist and hide the real field names while validate to make sure only
     * allowed properties can be used in the filter dsl.
     */
    @Test
    void shouldPreventFromWhitelistDictionary() {
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("priority", "priority");
        dictionary.put("city", "city_name");
        dictionary.put("price", "price_tag");

        String dsl = "(priority eq 1 or city eq 'Redmond') and price gt 100";
        String expectedSql = "(priority = 1 OR city_name = 'Redmond') AND price_tag > 100";

        Filter filter = new Filter();
        filter.setValue(dsl);

        FilterBuilder builder = new SqlSyntaxBuilder();
        String actualSql = builder.build(filter, dictionary);

        assertEquals(expectedSql, actualSql);
    }


}