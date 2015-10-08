/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.transform;

import static com.persinity.common.StringUtils.format;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.db.RelDb;
import com.persinity.common.db.metainfo.Col;
import com.persinity.common.db.metainfo.SqlFormatter;
import com.persinity.common.invariant.Invariant;

/**
 * Represents parameterized relational query function.
 * 
 * @author Doichin Yordanov
 */
public class ParamQryFunc extends BaseRelFunc<DirectedEdge<RelDb, List<?>>, Iterator<Map<String, Object>>> implements
        RelExtractFunc {

    private final List<Col> cols;
    private String toString;

    /**
     * @param cols
     *            Meta-info about the result set.
     * @param sql
     */
    public ParamQryFunc(final List<Col> cols, final String sql) {
        super(sql);
        Invariant.assertArg(cols != null && !cols.isEmpty());
        this.cols = ImmutableList.copyOf(cols);
    }

    public List<Col> getCols() {
        return cols;
    }

    @Override
    public Iterator<Map<String, Object>> apply(final DirectedEdge<RelDb, List<?>> input) {
        return input.src().executePreparedQuery(getSql(), input.dst());
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = format("{}(SELECT ... FROM {}...)", this.getClass().getSimpleName(),
                    SqlFormatter.getTableClause(getSql()));
        }
        return toString;
    }

}
