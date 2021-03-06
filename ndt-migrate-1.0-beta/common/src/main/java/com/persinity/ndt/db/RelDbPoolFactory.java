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

package com.persinity.ndt.db;

import org.apache.log4j.Logger;

import com.persinity.common.Config;
import com.persinity.common.collection.DirectedEdge;
import com.persinity.common.collection.Pool;
import com.persinity.common.db.Closeable;
import com.persinity.common.db.ConnectionSource;
import com.persinity.common.db.DbConfig;
import com.persinity.common.db.HikariConnectionSource;
import com.persinity.common.db.RelDb;
import com.persinity.common.db.RelDbPool;
import com.persinity.common.logging.Log4jLogger;

/**
 * Singleton source of NDT {@link com.persinity.common.db.RelDbPool}s
 *
 * @author dyordanov
 */
public class RelDbPoolFactory implements Closeable {

    public DirectedEdge<Pool<RelDb>, Pool<RelDb>> ndtBridge() {
        return ndtDbPoolBridge;
    }

    public DirectedEdge<Pool<RelDb>, Pool<RelDb>> appBridge() {
        return appDbPoolBridge;
    }

    /**
     * @param fileName
     *         of the DbConfig property file. Must be in the classpath.
     */
    public RelDbPoolFactory(final String fileName) {
        ndtDbPoolBridge = new DirectedEdge<>(newRelDbPool(fileName, "src.ndt."), newRelDbPool(fileName, "dst.ndt."));
        appDbPoolBridge = new DirectedEdge<>(newRelDbPool(fileName, "src.app."), newRelDbPool(fileName, "dst.app."));
    }

    @Override
    public void close() throws RuntimeException {
        appDbPoolBridge.src().close();
        ndtDbPoolBridge.src().close();
        ndtDbPoolBridge.dst().close();
        appDbPoolBridge.dst().close();
    }

    private static Pool<RelDb> newRelDbPool(final String fileName, final String keysPrefix) {
        final DbConfig dbConfig = new DbConfig(Config.loadPropsFrom(fileName), fileName, keysPrefix);
        final ConnectionSource connSource = new HikariConnectionSource(dbConfig, "cp-hikari.properties");
        final Pool<RelDb> result = new RelDbPool(connSource, dbConfig);
        log.info("Using {} for {} ({})", result, keysPrefix, dbConfig);
        return result;
    }

    private static final Log4jLogger log = new Log4jLogger(Logger.getLogger(RelDbPoolFactory.class));
    private final DirectedEdge<Pool<RelDb>, Pool<RelDb>> ndtDbPoolBridge;
    private final DirectedEdge<Pool<RelDb>, Pool<RelDb>> appDbPoolBridge;
}
