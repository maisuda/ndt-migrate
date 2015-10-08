/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.ndt.etlmodule.relational.common;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMockSupport;
import org.junit.Test;

import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.db.RelDb;
import com.persinity.ndt.dbagent.relational.AgentSqlStrategy;
import com.persinity.ndt.dbagent.relational.SchemaInfo;
import com.persinity.ndt.dbagent.relational.StringTid;
import com.persinity.ndt.transform.TransferFunc;
import com.persinity.ndt.transform.TransferWindow;

/**
 * @author Ivan Dachev
 */
public class NoOpsRelTransferFunctorTest extends EasyMockSupport {

    @SuppressWarnings("unchecked")
    @Test
    public void testApply() throws Exception {
        final TransferWindow<RelDb, RelDb> tWindow = createMock(TransferWindow.class);
        final DirectedEdge<SchemaInfo, SchemaInfo> schemas = createMock(DirectedEdge.class);
        final AgentSqlStrategy sqlStrategy = createMock(AgentSqlStrategy.class);

        final List<StringTid> tids = Collections.singletonList(new StringTid("t"));
        expect((List<StringTid>) tWindow.getSrcTids()).andStubReturn(tids);

        replayAll();

        final NoOpsRelTransferFunctor noOpsRelTransferFunctor = new NoOpsRelTransferFunctor("test", tWindow, schemas,
                sqlStrategy);

        final Set<TransferFunc<RelDb, RelDb>> res = noOpsRelTransferFunctor.apply(null);

        verifyAll();

        assertThat(res, notNullValue());
        assertThat(res.size(), is(1));
        assertThat(res.iterator().next(), instanceOf(NoOpsRelTransferFunc.class));
    }
}
