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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * {@code CtrlVKeyboard} is a keyless keyboard that is controlled by Intent broadcast, on their own
 * and through Tasker.
 *
 * @author Julio Garcia Munoz (ZhDev)
 */
public class CtrlVKeyboard extends InputMethodService {

    /**
     * This receiver listens for "net.zhdev.ctrlvkeyboard.ACTION_PASTE_TEXT" and
     * "net.zhdev.ctrlvkeyboard.ACTION_PASTE_TEXT_TASKER" Intent actions when the keyboard is
     * active
     * and showing.
     */
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            boolean isSynchronous = false;
            Bundle extras;
            if (intent.getAction().equals(Constants.ACTION_PASTE_TEXT_TASKER)) {
                extras = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
                isSynchronous = intent.getBooleanExtra(Constants.EXTRA_IS_SYNCHRONOUS, false);
            } else {
                extras = intent.getExtras();
            }
            String text = extras.getString(Constants.EXTRA_TEXT);
            boolean endWithAction = extras.getBoolean(Constants.EXTRA_END_WITH_ACTION, false);
            boolean clearBefore = extras.getBoolean(Constants.EXTRA_CLEAR_BEFORE, false);
            boolean typingDone = type(text, clearBefore, endWithAction);

            if (isSynchronous) {
                if (typingDone) {
                    TaskerPlugin.Setting
                            .signalFinish(context, intent, TaskerPlugin.Setting.RESULT_CODE_OK,
                                    null);
                } else {
                    TaskerPlugin.Setting
                            .signalFinish(context, intent, TaskerPlugin.Setting.RESULT_CODE_FAILED,
                                    null);
                }
            }
        }
    };

    /**
     * Inserts text in the current focused text editor if possible and if it is a valid one.
     *
     * @param text          the text to be inserted
     * @param clearBefore   whether the text editor should be cleared before inserting the text
     * @param endWithAction whether the IME action should be performed at the end
     * @return {@code true} if the text was inserted and the actions performed, {@code false}
     * otherwise
     */
    private boolean type(String text, boolean clearBefore, boolean endWithAction) {
        boolean result = false;

        if (text == null) {
            text = "";
        }

        EditorInfo info = getCurrentInputEditorInfo();

        if (info != null) {
            // If the view accepts text as input
            if ((info.inputType & EditorInfo.TYPE_MASK_CLASS) != EditorInfo.TYPE_NULL) {
                InputConnection ic = getCurrentInputConnection();

                if (ic != null) {
                    boolean enterShouldHaveAction = true;
                    ic.beginBatchEdit();
                    if (endWithAction) {
                        enterShouldHaveAction =
                                (info.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0;
                        if (!enterShouldHaveAction) {
                            text += "\n";
                        }
                    }
                    if (clearBefore) {
                        ic.deleteSurroundingText(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    }
                    ic.commitText(text, 1);
                    result = ic.endBatchEdit();

                    if (result && endWithAction && enterShouldHaveAction) {
                        result = ic
                                .performEditorAction(info.imeOptions & EditorInfo.IME_MASK_ACTION);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public View onCreateInputView() {
        return getLayoutInflater().inflate(R.layout.keyboard, null);
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the BroadcastReceiver for both actions
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.ACTION_PASTE_TEXT_TASKER));
        registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.ACTION_PASTE_TEXT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // The BroadcastReceiver is no longer needed if the keyboard is not showing
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mBroadcastReceiver);
    }
}
