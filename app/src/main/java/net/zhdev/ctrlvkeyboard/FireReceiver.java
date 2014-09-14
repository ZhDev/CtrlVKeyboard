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
import android.support.v4.content.LocalBroadcastManager;

/**
 * {@code FireReceiver} listens for actions fired from Tasker and redirects them to the keyboard.
 */
public class FireReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        Intent realFireIntent = new Intent(Constants.ACTION_PASTE_TEXT_TASKER);
        realFireIntent.putExtras(intent.getExtras());
        boolean isSynchronous = isOrderedBroadcast();
        realFireIntent.putExtra(Constants.EXTRA_IS_SYNCHRONOUS, isSynchronous);
        boolean broadcastSent = lbm.sendBroadcast(realFireIntent);
        if (isSynchronous) {
            if (broadcastSent) {
                setResultCode(TaskerPlugin.Setting.RESULT_CODE_PENDING);
            } else {
                setResultCode(TaskerPlugin.Setting.RESULT_CODE_FAILED);
            }
        }
    }

}
