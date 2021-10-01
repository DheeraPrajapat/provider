package com.media.picker;

interface CallBackTask {
    void onPreExecute();
    void onProgressUpdate(int progress);
    void onPostExecute(String path, boolean wasDriveFile, boolean wasSuccessful, String reason);
}
