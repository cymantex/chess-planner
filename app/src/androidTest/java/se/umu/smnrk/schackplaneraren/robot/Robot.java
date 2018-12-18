package se.umu.smnrk.schackplaneraren.robot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Simon Eriksson
 * @version 1.1
 */
public class Robot<T extends Robot> {
    private ActivityTestRule<?> activityRule;
    private boolean rotateEnabled;
    private boolean isPortrait;

    public Robot(ActivityTestRule activityRule){
        this.activityRule = activityRule;
    }

    Robot(ActivityTestRule activityRule, boolean enableRotate){
        this.activityRule = activityRule;
        rotateEnabled = enableRotate;
    }

    public T launch(){
        activityRule.launchActivity(null);

        activityRule.getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isPortrait = true;

        return cast(this);
    }

    public T launch(Intent intent){
        activityRule.launchActivity(intent);

        activityRule.getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isPortrait = true;

        return cast(this);
    }

    public T checkIfViewMatches(@IdRes int viewId, Matcher<View> matcher){
        onView(withId(viewId)).check(matches(matcher));

        return cast(this);
    }

    public T clickView(@IdRes int viewId){
        onView(withId(viewId)).perform(click());

        return cast(this);
    }

    public T checkIfHidden(@IdRes int... viewIds){
        for(int viewId : viewIds){
            onView(withId(viewId)).check(matches(not(isDisplayed())));
        }

        return cast(this);
    }

    public T checkIfShowing(Matcher<View> matcher){
        onView(matcher).check(matches(isDisplayed()));

        return cast(this);
    }

    public T checkIfShowing(@IdRes int... viewIds){
        for(int viewId : viewIds){
            onView(withId(viewId)).check(matches(isDisplayed()));
        }

        return cast(this);
    }

    public T clickBackButton(){
        pressBack();

        return cast(this);
    }

    public T rotateIfEnabled(){
        if(!rotateEnabled){
            return cast(this);
        }

        activityRule.getActivity().setRequestedOrientation(isPortrait
                ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        isPortrait = !isPortrait;

        return cast(this);
    }

    ActivityTestRule getActivityRule(){
        return activityRule;
    }

    @SuppressWarnings("unchecked")
    private T cast(Robot robot){
        return (T)robot;
    }
}
