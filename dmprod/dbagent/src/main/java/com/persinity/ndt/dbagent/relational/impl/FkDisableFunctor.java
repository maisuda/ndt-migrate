/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.dbagent.relational.impl;

import com.google.common.base.Function;
import com.persinity.common.db.RelDb;
import com.persinity.common.db.metainfo.constraint.FK;
import com.persinity.common.fp.Functor;
import com.persinity.ndt.dbagent.relational.AgentSqlStrategy;
import com.persinity.ndt.transform.RelFunc;

/**
 * Functor that returns function, which disables a FK constraint.
 * 
 * @author Doichin Yordanov
 */
public class FkDisableFunctor implements Functor<RelDb, RelDb, FK, Function<RelDb, RelDb>> {

    public FkDisableFunctor(final AgentSqlStrategy sqlStrategy) {
        this.sqlStrategy = sqlStrategy;
    }

    @Override
    public Function<RelDb, RelDb> apply(final FK input) {
        final String sql = sqlStrategy.disableConstraint(input);
        final Function<RelDb, RelDb> result = new RelFunc(sql);
        return result;
    }

    private final AgentSqlStrategy sqlStrategy;

}
