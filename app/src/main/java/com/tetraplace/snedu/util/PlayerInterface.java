package com.tetraplace.snedu.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

public class PlayerInterface {

    Context mContext;
    Activity activity;

    public PlayerInterface(Activity activity) {
        this.activity = activity;
        mContext = activity;
    }

    @JavascriptInterface
    public void onPlaying(float currentTime) {
        // 재생 시작 시점 기록 및 시간 정보 활용
        Log.d("YouTubePlayer", "Playing - Current Time: " + currentTime);
        // 예: TextView에 현재 시간 표시
    }

    @JavascriptInterface
    public void onPaused(float currentTime) {
        // 일시 정지 시점 기록 및 시간 정보 활용
        Log.d("YouTubePlayer", "Paused - Current Time: " + currentTime);
        // 예: TextView에 현재 시간 표시
    }

    @JavascriptInterface
    public void onEnded() {
        // 재생 종료 시점 기록
        Log.d("YouTubePlayer", "Ended");
    }

    @JavascriptInterface
    public void onFullScreen(boolean isFullScreen) {
        setFullScreen(isFullScreen);
        Log.d("YouTubePlayer", "Playing - Full Screen ");
    }


    public void setFullScreen(boolean fullscreen) {
        if (fullscreen) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}