/*
 * Copyright 2015 Persinity Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.persinity.common.collection;

import static com.persinity.common.StringUtils.format;
import static com.persinity.common.invariant.Invariant.assertArg;

import com.persinity.common.PairBase;

/**
 * Connects two nodes (S, D) in one-way relation: S -> D.
 * If S or D mutate during edge existence,
 * the outcome of edge operations is undefined.
 *
 * @author Doichin Yordanov
 */
public class DirectedEdge<S, D> extends PairBase<S, D> {

    /**
     * @param src
     *         source
     * @param dst
     *         destination
     */
    public DirectedEdge(final S src, final D dst) {
        super(src, dst);
        assertArg(src != null || dst != null);
    }

    /**
     * @return source
     */
    public S src() {
        return first;
    }

    /**
     * @return destination
     */
    public D dst() {
        return second;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = format("{}->{}", src(), dst());
        }
        return toString;
    }

    private String toString;
}
