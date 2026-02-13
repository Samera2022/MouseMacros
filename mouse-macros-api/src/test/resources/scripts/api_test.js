const registry_name = 'dependencyA';
const display_name = 'dependencyA';
const version = '1.1';
const author = 'Samera2022';
const description = 'A comprehensive script to test the MouseMacros v1.0 Scripting API.';
const available_version = '2.0.0';
const requireNativeAccess = true;
const requireNativeAccessDescription = "...";
// api_test.js
// A comprehensive script to test the MouseMacros v1.0 Scripting API.
// This version uses modern ES6+ syntax compatible with GraalVM.

mm.log('API Test Script v1.1 (ES6+) Loaded Successfully!');

// --- 1. Test Recording Events ---

// Listen for the 'BeforeRecordStart' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent', (event) => {
    mm.log('Event Triggered: BeforeRecordStart');
    mm.log(' > Start Time: ${new Date(event.getStartTime())}');
    mm.log(' > Initial Mouse: (${event.getInitialMousePos().x}, ${event.getInitialMousePos().y})');

    // Example of cancelling an event: Prevent recording before 9 AM.
    const currentHour = new Date().getHours();
    if (currentHour < 9) {
        mm.log(' > ACTION: Cancelling recording. It is before 9 AM!');
        event.setCancelled(true);
    }
});

// Listen for the 'OnInputCaptured' event to filter inputs.
mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    // Example: Block the ''' (tilde/backquote) key from being recorded.
    // The keycode for ''' is 192 in JNativeHook.
    if (event.getInputType() === 10 && event.getKeyCode() === 192) { // 10 = KeyPress
        mm.log('Event Triggered: OnInputCaptured - Blocking backquote key!');
        event.setCancelled(true);
    }
});

// Listen for the 'AfterRecordStop' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent', (event) => {
    mm.log('Event Triggered: AfterRecordStop');
    mm.log(' > Recording finished with ${event.getTotalActions()} actions.');

    // Example of post-processing: Log if the macro is very short.
    if (event.getTotalActions() < 5) {
        mm.log(' > INFO: This is a very short macro.');
    }
});


// --- 2. Test Execution Events ---

// Listen for the 'BeforePlaybackStart' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    mm.log('Event Triggered: BeforePlaybackStart');
    mm.log(' > Macro contains ${event.getMacroData().size()} steps.');
    mm.log(' > It will repeat ${event.getRepeatCount()} time(s).');

    // Example: Cancel playback if the macro is excessively long.
    if (event.getMacroData().size() > 1000) {
        mm.log(' > ACTION: Cancelling playback. Macro is too long (> 1000 steps).');
        event.setCancelled(true);
    }
});

// Listen for the 'BeforeStepExecute' event to add conditional logic.
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    const action = event.getAction();

    // [PERFORMANCE FIX]
    // Only check pixel color for Mouse Press (1) or Key Press (10) events.
    // Skipping this check for Mouse Move (0) events ensures smooth cursor movement.
    if (action.type !== 1 && action.type !== 10) {
        return;
    }

    // Example: Use the PluginContext to check a pixel color.
    // If the pixel at (10, 10) is not pure white, skip the current step.
    const context = mm.getContext();
    const color = context.getPixelColor(10, 10);

    if (color.getRed() < 255 || color.getGreen() < 255 || color.getBlue() < 255) {
//        mm.log('Event Triggered: BeforeStepExecute on step ${event.getStepIndex()}');
//        mm.log(' > ACTION: Pixel at (10,10) is not white. Skipping this step.');
//        event.setCancelled(true);

        // Add custom metadata to the action
//        action.metadata.put('skippedBy', 'api_test.js');
    }
});

// Listen for the 'OnLoopComplete' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    mm.log('Event Triggered: OnLoopComplete');
    mm.log(' > Loop ${event.getCompletedIterations()} finished.');
    mm.log(' > ${event.getRemainingIterations()} loops remaining.');
});

// Listen for the 'OnPlaybackAborted' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('Event Triggered: OnPlaybackAborted');
    mm.log(' > Playback aborted at step ${event.getAtStepIndex()}');
    mm.log(' > Reason: ${event.getAbortReason()}');
});


// --- 3. Test System Events ---

// Listen for the 'OnAppLaunched' event.
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('Event Triggered: OnAppLaunched');
    mm.log(' > Welcome to MouseMacros v${event.getAppVersion()}');
    mm.log(' > Running on Java ${event.getRuntimeEnv()}');
});
