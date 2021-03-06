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
package com.persinity.ndt.transform;

import static com.persinity.common.StringUtils.format;
import static com.persinity.common.invariant.Invariant.assertArg;
import static com.persinity.common.invariant.Invariant.notEmpty;

import java.util.ArrayList;
import java.util.List;

import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.db.RelDb;
import com.persinity.common.db.metainfo.Col;

/**
 * Execute insert prepared DML statement on duplicate keys failure execute updates.
 *
 * @author Ivan Dachev
 */
public class InsertOnFailureUpdateFunc extends ParamDmlFunc {

    public InsertOnFailureUpdateFunc(final String insertSql, final String updateSql, final List<Col> cols,
            final List<Col> updateIdCols) {
        super(format("{} ON FAILURE {}", insertSql, updateSql), cols);

        notEmpty(insertSql);
        notEmpty(updateSql);

        this.insertSql = insertSql;
        this.updateSql = updateSql;

        this.updateIdColIndxes = new ArrayList<>();

        for (Col updateIdCol : updateIdCols) {
            final int indx = cols.indexOf(updateIdCol);
            assertArg(indx != -1, "Failed to find index for updateIdCol: {}", updateIdCol);
            this.updateIdColIndxes.add(indx);
        }
    }

    @Override
    public Integer apply(final DirectedEdge<RelDb, List<?>> input) {
        try {
            return input.src().executePreparedDml(insertSql, input.dst());
        } catch (final RuntimeException e) {
            final Throwable cause = e.getCause();
            if (input.src().getSqlStrategy().isIntegrityConstraintViolation(cause)) {
                final ArrayList<Object> params = new ArrayList<>(input.dst().size() + 1);
                params.addAll(input.dst());
                for (int updateIdColIndex : updateIdColIndxes) {
                    params.add(input.dst().get(updateIdColIndex));
                }
                return input.src().executePreparedDml(updateSql, params);
            }
            throw e;
        }
    }

    private final String insertSql;
    private final String updateSql;
    private final List<Integer> updateIdColIndxes;
}
