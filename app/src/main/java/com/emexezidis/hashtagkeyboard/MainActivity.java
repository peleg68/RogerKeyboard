package com.emexezidis.hashtagkeyboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    public static String HASHTAG_KEYBOARD_PREF_NAME = "hashtagkeyboard";
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static Boolean activityVisible = false;
    private static MainActivity activityReference;
    private final int TUTORIAL_RESULT_CODE = 1;

    private PopupFragment popupFragment;
    private SharedPreferences sharedPreferences;
    private String defaultHashtagList = "#you #can #edit #these #hashtags";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReference = this;
        sharedPreferences = getSharedPreferences(HASHTAG_KEYBOARD_PREF_NAME, 0);

        handleIntent();
        checkIfFirstRun();
        showTemplateEditorFragment();
    }

    private void handleIntent() {

        // Used when the Activity is not already visible:
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.getBoolean("changeImeAndClose", false)) {
                System.out.println("Change IME and close");
                changeIme(true);
            } else if (extras.getBoolean("showTutorial", false)) {
                showTutorial();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.change_ime) {
            changeIme(false);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTemplateEditorFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        //Check if fragment already exists:
        popupFragment = (PopupFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        if (popupFragment == null) {

            // If the Fragment doesn't already exist:
            popupFragment = new PopupFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, popupFragment, TAG_RETAINED_FRAGMENT).commit();

        } else {

            // If the Fragment already exists:
            fragmentManager.beginTransaction().replace(android.R.id.content, popupFragment).commit();
        }
    }

    private void checkIfFirstRun() {

        if (getSharedPreferenceInt("firstRun") == 0) {
            saveSharedPreference("hashtags", defaultHashtagList);
            showTutorial();
        }
    }

    private void showTutorial() {
        Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
        // TODO: Check this
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, TUTORIAL_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TUTORIAL_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                saveSharedPreference("firstRun", 1);
            } else {
                finish();
            }
        }
    }

    protected String getSharedPreferenceString(String tag) {
        return sharedPreferences.getString(tag, "");
    }

    protected int getSharedPreferenceInt(String tag) {

        SharedPreferences sharedPref = getSharedPreferences(HASHTAG_KEYBOARD_PREF_NAME, 0);
        return (sharedPref.getInt(tag, 0));
    }

    protected void saveSharedPreference(String tag, int value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(tag, value);
        editor.apply();
    }

    protected void saveSharedPreference(String tag, String text) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, text);
        editor.apply();
    }

    public void changeIme(boolean finishActivity) {

        InputMethodManager imeManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        }

        if (finishActivity) {
            finish();
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static MainActivity getActivityInstance() {
        return activityReference;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.activityPaused();
    }
}
