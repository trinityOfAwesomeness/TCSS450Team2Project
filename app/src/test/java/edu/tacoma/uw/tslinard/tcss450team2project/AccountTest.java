package edu.tacoma.uw.tslinard.tcss450team2project;
import org.junit.Before;
import org.junit.Test;

import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.Account;

import static org.junit.Assert.*;

/**
 * Test Class for Account class.
 * @author Tatiana Linardopoulou
 * @author Seoungdeok Jeon
 */
public class AccountTest {

    /**
     * Account object for testing.
     */
    private Account mTestAccount;

    /**
     * Set up before each test to avoid redundancy.
     */
    @Before
    public void setUp() {
        mTestAccount = new Account("Tatiana", "Linardopoulou", "tslinard", "tslinard@uw.edu", "test123");
    }

    @Test
    public void testAccountConstructor() {
        assertNotNull(new Account("Tatiana", "Linardopoulou", "tslinard", "tslinard@uw.edu", "test123"));
    }

    @Test
    public void testAccountConstructorBadEmail() {
        try {
            new Account("Tatiana", "Linardopoulou", "tslinard", "tslinarduw.edu", "test123");
            fail("Account created with invalid email");
        } catch(IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Tatiana", mTestAccount.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Linardopoulou", mTestAccount.getLastName());
    }

    @Test
    public void testGetUserName() {
        assertEquals("tslinard", mTestAccount.getUserName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("tslinard@uw.edu", mTestAccount.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals("test123", mTestAccount.getPassword());
    }

}
