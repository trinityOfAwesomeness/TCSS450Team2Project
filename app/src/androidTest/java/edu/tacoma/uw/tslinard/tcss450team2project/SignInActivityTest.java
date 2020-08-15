package edu.tacoma.uw.tslinard.tcss450team2project;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.SignInActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);

    @Test
    public void testLoginInvalidEmail() {
        // Type text and then press the button.
        onView(withId(R.id.et_email))
                .perform(typeText("test.edu"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.et_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withText("Enter valid email address."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginInvalidAccount() {
        // Type text and then press the button.
        onView(withId(R.id.et_email))
                .perform(typeText("test@uw.edu"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.et_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withText("That account doesn't exist. Enter a different account."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLaunchSignUpFragment() {
        onView(withId(R.id.btn_create_account))
                .perform(click());

        onView(withText("Create Account"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLogin() {
        // test the valid account
        onView(ViewMatchers.withId(R.id.et_email))
                .perform(ViewActions.typeText("test@uw.edu"));
        Espresso.closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.et_password))
                .perform(ViewActions.typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.btn_login))
                .perform(ViewActions.click());

        onView(withText("Sun"))
                .check(matches(isDisplayed()));
    }
}
