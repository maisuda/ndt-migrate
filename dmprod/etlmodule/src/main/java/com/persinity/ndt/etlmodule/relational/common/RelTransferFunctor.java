/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.etlmodule.relational.common;

import static com.persinity.common.StringUtils.format;
import static com.persinity.common.StringUtils.formatObj;

import java.util.Objects;

import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.db.RelDb;
import com.persinity.ndt.dbagent.relational.AgentSqlStrategy;
import com.persinity.ndt.dbagent.relational.SchemaInfo;
import com.persinity.ndt.etlmodule.TransferFunctor;
import com.persinity.ndt.transform.TransferWindow;

/**
 * Relational {@link TransferFunctor} implementation.
 *
 * @author Ivan Dachev
 */
public abstract class RelTransferFunctor implements TransferFunctor<RelDb, RelDb> {
    public RelTransferFunctor(final TransferWindow<RelDb, RelDb> transferWindow,
            final DirectedEdge<SchemaInfo, SchemaInfo> schemas, final AgentSqlStrategy sqlStrategy) {

        this.transferWindow = transferWindow;
        this.schemas = schemas;
        this.sqlStrategy = sqlStrategy;
    }

    public TransferWindow<RelDb, RelDb> getTransferWindow() {
        return transferWindow;
    }

    public DirectedEdge<SchemaInfo, SchemaInfo> getSchemas() {
        return schemas;
    }

    public AgentSqlStrategy getSqlStrategy() {
        return sqlStrategy;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = format("{}({})", formatObj(this), getTransferWindow());
        }
        return toString;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RelTransferFunctor)) {
            return false;
        }
        final RelTransferFunctor that = (RelTransferFunctor) obj;
        return getClass().equals(that.getClass()) && getTransferWindow().equals(that.getTransferWindow());
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            hashCode = Objects.hash(getClass(), getTransferWindow());
        }
        return hashCode;
    }

    private final TransferWindow<RelDb, RelDb> transferWindow;
    private final DirectedEdge<SchemaInfo, SchemaInfo> schemas;
    private final AgentSqlStrategy sqlStrategy;

    private String toString;
    private Integer hashCode;
}
