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

package com.persinity.ndt.datamutator.reldb;

import java.util.Properties;

import com.persinity.common.Config;
import com.persinity.common.db.DbConfig;

/**
 * @author Ivo Yanakiev
 */
public class RelDbConfig {

    public RelDbConfig(final Properties props, final String propsSource) {
        config = new Config(props, propsSource);
        dbConfig = new DbConfig(props, propsSource, "");
    }

    /**
     * @return {@link DbConfig} for connections details
     */
    public DbConfig getDbConfig() {
        return dbConfig;
    }

    /**
     * @return Schema init script's name
     */
    public String getSchemaInitSql() {
        return config.getString(SCHEMA_INIT_SQL);
    }

    /**
     * @return Schema drop script's name
     */
    public String getSchemaDropSql() {
        return config.getString(SCHEMA_DROP_SQL);
    }

    static final String SCHEMA_INIT_SQL = "schema.init.sql";
    static final String SCHEMA_DROP_SQL = "schema.drop.sql";

    private final Config config;
    private final DbConfig dbConfig;
}
