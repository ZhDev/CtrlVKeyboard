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

/**
 * Common constants.
 */
public final class Constants {

    public static final int BUNDLE_VERSION = 1;

    public static final String ACTION_PASTE_TEXT = "net.zhdev.ctrlvkeyboard.ACTION_PASTE_TEXT";

    public static final String ACTION_PASTE_TEXT_TASKER
            = "net.zhdev.ctrlvkeyboard.ACTION_PASTE_TEXT_TASKER";

    public static final String EXTRA_BUNDLE_VERSION
            = "net.zhdev.ctrlvkeyboard.EXTRA_BUNDLE_VERSION";

    public static final String EXTRA_TEXT = "net.zhdev.ctrlvkeyboard.EXTRA_TEXT";

    public static final String EXTRA_CLEAR_BEFORE
            = "net.zhdev.ctrlvkeyboard.EXTRA_CLEAR_BEFORE";

    public static final String EXTRA_END_WITH_ACTION
            = "net.zhdev.ctrlvkeyboard.EXTRA_END_WITH_ACTION";

    public static final String EXTRA_IS_SYNCHRONOUS
            = "net.zhdev.ctrlvkeyboard.EXTRA_IS_SYNCHRONOUS";

    public static final String LOG_TAG = "CtrlV";

    private Constants() {
        throw new UnsupportedOperationException("Non-instantiable class");
    }

}
