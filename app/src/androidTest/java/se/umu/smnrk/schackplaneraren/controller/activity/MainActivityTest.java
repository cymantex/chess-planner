package se.umu.smnrk.schackplaneraren.controller.activity;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.robot.Robot;

import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    /*
    @Test
    public void shouldHaveCorrectLaunchView(){
        new Robot(activityRule)
                .launch()
                .checkIfShowing(withText(R.string.trainings))
                .checkIfShowing(R.id.training_default_actions)
                .checkIfHidden(R.id.training_selected_actions)
                .clickView(R.id.training_add)
                .checkIfShowing(R.id.input_title)
                .clickBackButton()
                .clickBackButton()
                .clickView(R.id.training_search)
                .clickBackButton()
                .clickBackButton()
                .clickView(R.id.training_filter)
                .clickBackButton();
    }
    */
}