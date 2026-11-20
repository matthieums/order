package com.switchfully.order;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring (Boot) will set up a Test Application Context.
 * For Test Application Context for which Transactions are enabled,
 * The transaction will automatically be rolled back.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public abstract class ControllerIntegrationTest {

    @LocalServerPort
    private int port;

    public int getPort() {
        return port;
    }

    @BeforeEach
    public void clearAndFlushDatabase() {
        clearDatabase();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    public abstract void clearDatabase();
}
