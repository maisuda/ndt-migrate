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
package com.persinity.haka;

import java.io.Serializable;

import com.persinity.common.Id;
import com.persinity.common.StringUtils;

/**
 * {@link Job} identity with id and parentId.
 *
 * @author Ivan Dachev
 */
public class JobIdentity implements Serializable, Comparable<JobIdentity> {

    private static final long serialVersionUID = -4064177694091826045L;

    public final Id parentId;
    public final Id id;

    /**
     * Creates a new JobIdentity with same random UUID for id and parentId.
     */
    public JobIdentity() {
        this.parentId = Id.nextValue();
        this.id = this.parentId;
        this.toString = makeToString();
        this.toShortString = makeToShortString();
    }

    /**
     * Creates new child JobIdentify.
     *
     * @param parent
     *         Parent to use for new JobIdentity
     */
    public JobIdentity(final JobIdentity parent) {
        if (parent == null) {
            throw new NullPointerException("parent");
        }

        this.parentId = parent.id;
        this.id = Id.nextValue();
        this.toString = makeToString();
        this.toShortString = makeToShortString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof JobIdentity)) {
            return false;
        }

        final JobIdentity jid = (JobIdentity) obj;
        return jid.parentId.equals(this.parentId) && jid.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * @return short string representation
     */
    public String toShortString() {
        return toShortString;
    }

    @Override
    public int compareTo(final JobIdentity other) {
        if (other == null) {
            return -1;
        }
        return this.id.compareTo(other.id);
    }

    private String makeToString() {
        return StringUtils.format("{}({}/{})", getClass().getSimpleName(), parentId, id);
    }

    private String makeToShortString() {
        return StringUtils.format("{}/{}", parentId.toStringShort(), id.toStringShort());
    }

    private final String toString;
    private final String toShortString;
}
