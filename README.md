# Ctrl-V Keyboard

A keyless keyboard for inserting text using Tasker. The keyboard allows you to "paste" text
into apps that don't provide an open API, for example if the app doesn't have a broadcast
receiver and it doesn't accept data when an activity loads.

As long as this keyboard is active and a text box is selected you can issue commands to it
through the plugin settings or even using intents (i.e. through adb). It will make the text
appear in any open application.

When you use this app together with other plugins such as "Secure settings" or "Keyboard Swap for Tasker" (they need root access), you can make it fully automated. A small example of tasks
that could make this work would be:

1. Secure settings -> Switch keyboard to Ctrl-V.
2. Open the target app.
3. Wait one or two seconds to make sure app is fully loaded and the text box focused.
4. Ctrl-V -> Input text: "This is some sample text".
5. Secure settings -> Switch back to default keyboard.

Although Tasker is not required for this app, it's recommended. You can also control it by
delivering broadcast intents while the keyboard is active. This option could be useful while
debugging an app that requires a lot of user input, adb could be used in this case. This is the
information you'll need for the broadcast:

* Broadcast intent action: `net.zhdev.ctrlvkeyboard.ACTION_PASTE_TEXT`
* Intent extras (all of them are optional):

|Value|Type|Default|Comments|
|-----|----|:-----:|--------|
|`net.zhdev.ctrlvkeyboard.EXTRA_TEXT`|String|*empty*|The text to be inserted|
|`net.zhdev.ctrlvkeyboard.EXTRA_CLEAR_TEXT`|boolean|`false`|Clear the existing text|
|`net.zhdev.ctrlvkeyboard.EXTRA_END_WITH_ACTION`|boolean|`false`|Simulate the 'Enter' key action after inserting the text|

The app has been uploaded to Google Play. You'll find it here, along with screenshots of the app:

https://play.google.com/store/apps/details?id=net.zhdev.ctrlvkeyboard

## License
```
Copyright 2014 Julio García Muñoz (ZhDev)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
