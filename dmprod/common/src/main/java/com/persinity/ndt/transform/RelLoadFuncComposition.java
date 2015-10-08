/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.transform;

import static com.persinity.common.StringUtils.format;
import static com.persinity.common.StringUtils.formatObj;

import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Function;
import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.db.RelDb;

/**
 * Composition function of Function<Integer, Integer> and RelLoadFunc.
 *
 * @author Ivan Dachev
 */
public class RelLoadFuncComposition implements RelLoadFunc {

    public RelLoadFuncComposition(final Function<Integer, Integer> fa, final RelLoadFunc fb) {
        this.fa = fa;
        this.fb = fb;
    }

    @Override
    public Integer apply(final DirectedEdge<RelDb, Iterator<Map<String, Object>>> dbIteratorPair) {
        return fa.apply(fb.apply(dbIteratorPair));
    }

    /**
     * @return Fa
     */
    public Function<Integer, Integer> getFa() {
        return fa;
    }

    /**
     * @return Fb function
     */
    public RelLoadFunc getFb() {
        return fb;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = format("{}({}, {})", formatObj(this), fa, fb);
        }
        return toString;
    }

    private final Function<Integer, Integer> fa;
    private final RelLoadFunc fb;

    private String toString;
}
