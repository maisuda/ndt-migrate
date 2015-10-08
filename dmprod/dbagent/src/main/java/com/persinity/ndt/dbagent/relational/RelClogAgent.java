/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.dbagent.relational;

import static com.persinity.common.invariant.Invariant.notNull;

import java.util.Arrays;

import com.google.common.base.Function;
import com.persinity.common.collection.CollectionUtils;
import com.persinity.common.collection.Tree;
import com.persinity.common.db.RelDb;
import com.persinity.ndt.dbagent.ClogAgent;
import com.persinity.ndt.dbagent.relational.impl.ClogGcFunctor;
import com.persinity.ndt.dbagent.relational.impl.ClogMountFunctor;
import com.persinity.ndt.dbagent.relational.impl.ClogUmountFunctor;
import com.persinity.ndt.dbagent.relational.impl.PlanBuilder;
import com.persinity.ndt.transform.RelFunc;

/**
 * {@link ClogAgent} implementation for relational databases.<BR>
 *
 * @author Doichin Yordanov
 */
public class RelClogAgent implements ClogAgent<Function<RelDb, RelDb>> {

    /**
     * @param db
     * @param schema
     *         Meta information for the application database
     * @param strategy
     *         Database specific SQL strategy
     */
    public RelClogAgent(final RelDb db, final SchemaInfo schema, final AgentSqlStrategy strategy) {
        notNull(schema);
        notNull(strategy);
        notNull(db);

        this.schema = schema;
        this.strategy = strategy;
        pb = new PlanBuilder();
        this.db = db;
    }

    @Override
    public Tree<Function<RelDb, RelDb>> clogMount() {
        return pb.build(schema.getTableNames(), new ClogMountFunctor(schema, strategy));
    }

    @Override
    public Tree<Function<RelDb, RelDb>> clogUmount() {
        return pb.build(schema.getTableNames(), new ClogUmountFunctor(schema, strategy));
    }

    @Override
    public Tree<Function<RelDb, RelDb>> clogGc() {
        return pb.build(schema.getTableNames(), new ClogGcFunctor(schema, strategy));
    }

    @Override
    public Tree<Function<RelDb, RelDb>> trlogCleanup() {
        String delStatement = strategy.deleteAllStatement(SchemaInfo.TAB_TRLOG);
        Function<RelDb, RelDb> delFunc = new RelFunc(delStatement);
        return CollectionUtils.newTree(Arrays.asList(delFunc));
    }

    @Override
    public Long getLastGid() {
        long maxGid = db.getInt(strategy.getMaxGidStatement());
        return maxGid;
    }

    private final AgentSqlStrategy strategy;
    private final SchemaInfo schema;
    private final PlanBuilder pb;
    private final RelDb db;
}
