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
package com.persinity.haka.impl.actor.rootjob;

import com.persinity.haka.Job;
import com.persinity.haka.JobIdentity;
import com.persinity.haka.JobProducer;
import com.persinity.haka.impl.actor.TestJob;

/**
 * Test root job.
 *
 * @author Ivan Dachev
 */
public class TestRootJob extends TestJob {

    private static final long serialVersionUID = 6994653673450807214L;

    public TestRootJob() {
        super(new JobIdentity(), TestRootJobProducer.class, false);
    }

    public TestRootJob(JobIdentity jobId, Class<? extends JobProducer> jobProducer, boolean clone) {
        super(jobId, jobProducer, clone);
    }

    @Override
    public Job clone() {
        TestRootJob clone = new TestRootJob(jobId, jobProducer, false);
        clone.state = state;
        return clone;
    }
}
