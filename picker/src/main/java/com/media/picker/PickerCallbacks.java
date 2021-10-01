package com.media.picker;

public interface PickerCallbacks {
    void onStartListener();

    void onProgressUpdate(int progress);

    void onCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason);
}
