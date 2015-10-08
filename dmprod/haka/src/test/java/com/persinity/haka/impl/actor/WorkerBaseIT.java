/**
 * Copyright (c) 2015 Persinity Inc.
 */
package com.persinity.haka.impl.actor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.testkit.JavaTestKit;
import com.persinity.common.Id;
import com.typesafe.config.ConfigFactory;

/**
 * @author Ivan Dachev
 */
public class WorkerBaseIT {

    @BeforeClass
    public static void setUpClass() {
        TestSharedSystem.system = ActorSystem.create("TestSystem", ConfigFactory.load("test-application.conf"));
    }

    @AfterClass
    public static void tearDownClass() {
        if (TestSharedSystem.system != null) {
            JavaTestKit.shutdownActorSystem(TestSharedSystem.system);
            TestSharedSystem.system = null;
        }
    }

    @Before
    public void setUp() {
        testPool = new TestWorkerPool(TestSharedSystem.system);
        Props elMediatorProps = Worker.props(testPool);
        // on each test run use different Worker name to recover from clean snapshots
        String elMediatorName = "Worker-test-" + Id.nextValue().toStringShort();
        elMediator = TestSharedSystem.system.actorOf(elMediatorProps, elMediatorName);
        log.info("Created: {}", elMediator.path());
    }

    @After
    public void tearDown() {
        elMediator.tell(PoisonPill.getInstance(), ActorRef.noSender());
        elMediator = null;
        testPool.destroy();
        testPool = null;
    }

    final LoggingAdapter log = Logging.getLogger(TestSharedSystem.system, this);
    ActorRef elMediator;
    TestWorkerPool testPool;

    // set here to big values when debug for example 300 seconds
    public static final String TIMEOUT_WAIT_EXPECT_MSG = "6 seconds";
    public static final String TIMEOUT_WAIT_EXPECT_2X_MSG = "18 seconds";
    public static final String TIMEOUT_WAIT_EXPECT_NO_MSG = "2 seconds";
}