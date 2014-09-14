/*
 * Copyright 2014 Julio Garcia Munoz (ZhDev)
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

package net.zhdev.ctrlvkeyboard;

import net.dinglisch.android.tasker.TaskerPlugin;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

/**
 * Settings configuration Activity for Tasker actions.
 */
public class SettingsActivity extends ActionBarActivity {

    private EditText mEtTextToType;

    private CheckBox mCbClearText;

    private CheckBox mCbPerformAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mEtTextToType = (EditText) findViewById(R.id.et_text_to_be_typed);
        mCbClearText = (CheckBox) findViewById(R.id.cb_clear_text);
        mCbPerformAction = (CheckBox) findViewById(R.id.cb_perform_action);

        if (savedInstanceState == null) {
            Bundle localeBundle = getIntent()
                    .getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
            if (localeBundle != null) {
                String text = localeBundle.getString(Constants.EXTRA_TEXT);
                if (text != null) {
                    mEtTextToType.setText(text);
                }
                boolean clearText = localeBundle.getBoolean(Constants.EXTRA_CLEAR_BEFORE, false);
                mCbClearText.setChecked(clearText);
                boolean performAction = localeBundle
                        .getBoolean(Constants.EXTRA_END_WITH_ACTION, false);
                mCbPerformAction.setChecked(performAction);
            }
        }

        if (!TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement(this)) {
            mEtTextToType.setHint(R.string.variable_replacement_not_ok);
        }

        if (!isKeyboardEnabled()) {
            showDialogKeyboardNotEnabled();
        }
    }

    private boolean isKeyboardEnabled() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> inputMethodList = inputMethodManager.getEnabledInputMethodList();
        ComponentName ctrlV = new ComponentName(this, CtrlVKeyboard.class);
        boolean ctrlVIsEnabled = false;
        int i = 0;

        while (!ctrlVIsEnabled && i < inputMethodList.size()) {
            if (inputMethodList.get(i).getComponent().equals(ctrlV)) {
                ctrlVIsEnabled = true;
            }
            i++;
        }

        return ctrlVIsEnabled;
    }

    private void showDialogKeyboardNotEnabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle(R.string.ctrlv_not_enabled_title)
                .setMessage(R.string.ctrlv_not_enabled_message)
                .setPositiveButton(R.string.go_to_system_settings,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                packResult();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates and sets an appropriate result Intent to be return to Tasker containing the
     * necessary variables and the blurb.
     */
    private void packResult() {
        String textToType = mEtTextToType.getText().toString();
        boolean clearText = mCbClearText.isChecked();
        boolean performAction = mCbPerformAction.isChecked();
        String blurb = String.format(getString(R.string.blurb), textToType,
                (clearText ? getString(R.string.yes) : getString(R.string.no)),
                (performAction ? getString(R.string.yes) : getString(R.string.no)));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

        Bundle resultBundle = new Bundle(5);
        resultBundle.putInt(Constants.EXTRA_BUNDLE_VERSION, Constants.BUNDLE_VERSION);
        resultBundle.putString(Constants.EXTRA_TEXT, textToType);
        resultBundle.putBoolean(Constants.EXTRA_CLEAR_BEFORE, clearText);
        resultBundle.putBoolean(Constants.EXTRA_END_WITH_ACTION, performAction);

        if (TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement(this)) {
            TaskerPlugin.Setting.setVariableReplaceKeys(resultBundle, new String[]{
                    Constants.EXTRA_TEXT
            });
        }

        resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

        if (TaskerPlugin.Setting.hostSupportsSynchronousExecution(getIntent().getExtras())) {
            TaskerPlugin.Setting.requestTimeoutMS(resultIntent, 2000);
        }

        setResult(TaskerPlugin.Setting.RESULT_CODE_OK, resultIntent);
    }

}
