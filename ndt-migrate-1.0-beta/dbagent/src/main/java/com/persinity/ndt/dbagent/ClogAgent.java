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
package com.persinity.ndt.dbagent;

import com.google.common.base.Function;
import com.persinity.common.collection.Tree;

/**
 * Responsible for un/mounting the logic and DB objects for Change log.
 *
 * @author Doichin Yordanov
 */
public interface ClogAgent<T extends Function<?, ?>> {

    /**
     * Generates the plan for Change log structures creation.
     */
    Tree<T> clogMount();

    /**
     * Generates the plan for Change log structures removal.
     */
    Tree<T> clogUmount();

    /**
     * Generates the plan for garbage collection of clog entries.
     */
    Tree<T> clogGc();

    /**
     * Deletes the TRLOG records.
     */
    Tree<T> trlogCleanup();

    /**
     * @return The maximal GID (change ID) out of all CLOG records
     */
    Long getLastGid();
}
