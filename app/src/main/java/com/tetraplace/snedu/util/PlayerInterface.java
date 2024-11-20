package com.tetraplace.snedu.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
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
        Log.d("YouTubePlayer", "Playing - Full Screen ");
        setFullScreen(isFullScreen);
    }


    public void setFullScreen(boolean fullscreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                if (fullscreen) {
                    insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                } else {
                    insetsController.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                }
            }
        } else {
            // Android 11 미만 버전에서는 이전 방식 사용
            if (fullscreen) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

}