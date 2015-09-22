/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.haka.impl.actor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.persinity.haka.impl.actor.message.NewMsg;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Testing of Worker when config is set for 1 job per worker.
 *
 * @author Ivan Dachev
 */
public class WorkerConfigSingleJobPerWorkerIT extends WorkerBaseIT {

    @BeforeClass
    public static void setUpClass() {
        Config config = ConfigFactory.parseString("haka.max-jobs-per-worker=1")
                .withFallback(ConfigFactory.load("test-application.conf"));

        TestSharedSystem.system = ActorSystem.create("TestSystem", config);
    }

    @Test
    public void testSingleJobPerWorkerInProgress() {
        new JavaTestKit(TestSharedSystem.system) {{
            TestJob job = new TestJob(TestJobProducerOneChild.class);
            elMediator.tell(new NewMsg(job), getRef());

            NewMsg newMsgChild = testPool.testKit.expectMsgClass(duration(TIMEOUT_WAIT_EXPECT_MSG), NewMsg.class);
            assertThat(newMsgChild.getJobId().parentId, is(job.getId().parentId));

            // resend new child msg to same mediator should resend it as it can handle only one
            elMediator.tell(newMsgChild, testPool.testKit.getRef());

            // timeout here is bigger as it should be at least 2x the msg-resend-delay = 3 seconds config value
            NewMsg newMsgChildResend = testPool.testKit
                    .expectMsgClass(duration(TIMEOUT_WAIT_EXPECT_2X_MSG), NewMsg.class);
            assertThat(newMsgChildResend.getJobId().parentId, is(job.getId().parentId));
            assertThat(newMsgChildResend.getJobId().id, is(newMsgChild.getJobId().id));

            expectNoMsg(duration(TIMEOUT_WAIT_EXPECT_NO_MSG));
            testPool.testKit.expectNoMsg(duration(TIMEOUT_WAIT_EXPECT_NO_MSG));
        }};
    }

}