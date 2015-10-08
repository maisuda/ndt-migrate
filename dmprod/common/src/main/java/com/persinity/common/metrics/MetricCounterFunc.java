/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.common.metrics;

import static com.persinity.common.StringUtils.format;
import static com.persinity.common.StringUtils.formatObj;
import static com.persinity.common.invariant.Invariant.notEmpty;

import com.codahale.metrics.Counter;
import com.google.common.base.Function;

/**
 * @author Ivan Dachev
 */
public class MetricCounterFunc implements Function<Integer, Integer> {

    public MetricCounterFunc(final String counterName) {
        notEmpty(counterName);

        this.counterName = counterName;
        counter = Metrics.getMetrics().getMetricRegistry().counter(counterName);
    }

    @Override
    public Integer apply(final Integer res) {
        counter.inc(res);
        return res;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = format("{}({})", formatObj(this), counterName);
        }
        return toString;
    }

    private final String counterName;
    private final Counter counter;

    private String toString;
}
