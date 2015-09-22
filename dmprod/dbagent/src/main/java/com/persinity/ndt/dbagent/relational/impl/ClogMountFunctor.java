/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.dbagent.relational.impl;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.persinity.common.db.RelDb;
import com.persinity.common.fp.Functor;
import com.persinity.ndt.dbagent.relational.AgentSqlStrategy;
import com.persinity.ndt.dbagent.relational.SchemaInfo;
import com.persinity.ndt.transform.RelFunc;

/**
 * Functor that returns function, which creates CLOG data structures
 *
 * @author Doichin Yordanov
 */
public class ClogMountFunctor implements Functor<RelDb, RelDb, String, Function<RelDb, RelDb>> {

    public ClogMountFunctor(final SchemaInfo schema, final AgentSqlStrategy strategy) {
        assert schema != null && strategy != null;
        this.schema = schema;
        this.strategy = strategy;
    }

    @Override
    public Function<RelDb, RelDb> apply(final String tableName) {
        final String clogTableName = schema.getClogTableName(tableName);
        final String sqlCreate = strategy
                .createTable(clogTableName, schema.getClogTableCols(tableName), SchemaInfo.COL_GID);
        final RelFunc clogCreate = new RelFunc(sqlCreate);

        final String sqlIndex = strategy.createIndex(clogTableName, SchemaInfo.COL_TID);
        final RelFunc clogIndex = new RelFunc(sqlIndex);

        final Function<RelDb, RelDb> clogFunc = Functions.compose(clogIndex, clogCreate);
        return clogFunc;
    }

    private final SchemaInfo schema;
    private final AgentSqlStrategy strategy;
}
