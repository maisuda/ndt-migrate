/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.haka.impl.actor;

import static com.persinity.haka.impl.actor.TestWorkerUtil.waitForProcessedMsg;
import static com.persinity.haka.impl.actor.WorkerBaseIT.TIMEOUT_WAIT_EXPECT_NO_MSG;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.persinity.common.Id;
import com.persinity.haka.impl.actor.message.NewMsg;
import com.persinity.haka.impl.actor.message.ProcessedMsg;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author Ivan Dachev
 */
public class WorkerIgnoreOldJobStatusIT {
    @Before
    public void setUp() {
        int persistenceId = (int) (System.currentTimeMillis() % 5);
        Config config = ConfigFactory.parseString(String.format("haka.watchdog-period=1 seconds\n" +
                "haka.status-update-timeout = 3 seconds\n" +
                "haka.workers = 3\n" +
                "akka.persistence.journal.leveldb.dir=./target/journal-%d\n" +
                "akka.persistence.snapshot-store.local.dir=./target/snapshots-%d", persistenceId, persistenceId))
                .withFallback(ConfigFactory.load("test-application.conf"));
        TestSharedSystem.system = ActorSystem.create("TestSystem", config);

        final String nodeId = Id.nextValue().toStringShort();

        String poolSupervisorName = "WorkersSupervisor-" + nodeId;
        supervisor = TestSharedSystem.system.actorOf(WorkersSupervisor.props(nodeId), poolSupervisorName);

        WorkersSupervisor.waitReady(supervisor, FiniteDuration.create(10, TimeUnit.SECONDS));
    }

    @After
    public void tearDownClass() {
        TestJobProducerOneChild.setDefaultChildClasses();

        if (TestSharedSystem.system != null) {
            JavaTestKit.shutdownActorSystem(TestSharedSystem.system);
            TestSharedSystem.system = null;
        }
    }

    @Test
    public void test() {
        new JavaTestKit(TestSharedSystem.system) {{
            TestJobProducerOneChild.CHILD_JOB_PRODUCER_CLASS = TestJobProducerIgnoreOldJobStatus.class;
            final TestJob job = new TestJob(TestJobProducerOneChild.class);
            supervisor.tell(new NewMsg(job), getRef());

            ProcessedMsg processedMsg = waitForProcessedMsg(this, job.getId());

            assertThat(((TestJob) processedMsg.getJob()).state, is(TestJob.STATE_DONE));

            assertThat(((TestChildJob) TestJobProducerOneChild.lastChildJob).state,
                    is(TestJobProducerIgnoreOldJobStatus.STATE_DONE_2));

            expectNoMsg(duration(TIMEOUT_WAIT_EXPECT_NO_MSG));
        }};
    }

    private ActorRef supervisor;
}
