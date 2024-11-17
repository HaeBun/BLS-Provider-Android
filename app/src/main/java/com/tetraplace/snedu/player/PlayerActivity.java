package com.tetraplace.snedu.player;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tetraplace.snedu.R;
import com.tetraplace.snedu.util.PlayerInterface;

public class PlayerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        webView = findViewById(R.id.youtube_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            private View customView;
            private WebChromeClient.CustomViewCallback customViewCallback;
            private int originalOrientation;
            private int originalSystemUiVisibility;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // 전체화면으로 전환
                if (customView != null) {
                    onHideCustomView();
                    return;
                }

                customView = view;
                originalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                originalOrientation = getRequestedOrientation();

                customViewCallback = callback;
                ((FrameLayout) getWindow().getDecorView()).addView(customView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            // 화면이 모두 로딩될 때 까지 변화를 감지함
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    // 페이지 로딩 완료 (영상 재생 시작 또는 재개)
                    // 필요한 동작 수행 (예: 로딩 표시 숨기기)
                } else if (newProgress < 100) {
                    // 페이지 로딩 중 (영상 버퍼링 또는 멈춤)
                    // 필요한 동작 수행 (예: 로딩 표시 보이기)
                }
            }

            @Override
            public void onHideCustomView() {
                // 전체화면에서 복귀
                ((FrameLayout) getWindow().getDecorView()).removeView(customView);
                customView = null;
                getWindow().getDecorView().setSystemUiVisibility(originalSystemUiVisibility);
                setRequestedOrientation(originalOrientation);
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;
            }
        });

        webView.loadDataWithBaseURL("https://www.youtube.com", getHtmlData("VDBBCLwE1Ww", 30), "text/html", "UTF-8", null);
        webView.addJavascriptInterface(new PlayerInterface(this), "Android");
    }

    private String getHtmlData(String videoId, int startSeconds) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>모바일 YouTube 플레이어</title>\n" +
                "<style>\n" +
                "  body { margin: 0; }\n" +
                "  .player-container {\n" +
                "    position: relative;\n" +
                "    width: 100%;\n" +
                "    padding-bottom: 56.25%; /* 16:9 비율 유지 */\n" +
                "    height: 0;\n" +
                "  }\n" +
                "  .player-container iframe {\n" +
                "    position: absolute;\n" +
                "    top: 0;\n" +
                "    left: 0;\n" +
                "    width: 100%;\n" +
                "    height: 100%;\n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"player-container\">\n" +
                "<iframe id=\"player\" \n" +
                "        src=\"https://www.youtube.com/embed/" + videoId + "?autoplay=0&controls=1&showinfo=0&modestbranding=1&rel=0&fs=1&enablejsapi=1&start=" + startSeconds + "\" \n" +
                "        frameborder=\"0\" \n" +
                "        allowfullscreen>\n" +
                "</iframe>\n" +
                "</div>\n" +
                "<script>\n" +
                "  var player; // player 객체를 전역 변수로 선언\n" +
                "\n" +
                "  function onYouTubeIframeAPIReady() {\n" +
                "    player = new YT.Player('player', {\n" +
                "      playerVars: { // playerVars 추가\n" +
                "        autoplay: 0,\n" +
                "        controls: 0,\n" +
                "        loop: 0,\n" +
                "        modestbranding: 1,\n" +
                "        rel: 0\n" +
                "      },\n" +
                "      events: {\n" +
                "        'onReady': onPlayerReady,\n" +
                "        'onStateChange': onPlayerStateChange\n" +
                "      }\n" +
                "    });\n" +
                "  }\n" +
                "\n" +
                "  function onPlayerReady(event) {\n" +
                "    console.log(\"플레이어 준비 완료\");\n" +
                "    // 플레이어가 준비되면 초기 재생 시간을 전달합니다.\n" +
                "    Android.onPlaying(player.getCurrentTime());\n" +
                "  }\n" +
                "\n" +
                "  function onPlayerStateChange(event) {\n" +
                "    console.log(\"플레이어 상태 변경:\", event.data);\n" +
                "\n" +
                "    // 플레이어 상태에 따라 Android 네이티브 코드에 이벤트를 전달합니다.\n" +
                "    switch (event.data) {\n" +
                "      case YT.PlayerState.PLAYING:\n" +
                "        Android.onPlaying(player.getCurrentTime());\n" +
                "        break;\n" +
                "      case YT.PlayerState.PAUSED:\n" +
                "        Android.onPaused(player.getCurrentTime());\n" +
                "        break;\n" +
                "      case YT.PlayerState.ENDED:\n" +
                "        Android.onEnded();\n" +
                "        break;\n" +
                "      case YT.PlayerState.FULLSCREEN:\n" +
                "        Android.onFullScreen(event.target.isFullscreen()); // 전체화면 상태 전달\n" +
                "        break;\n" +
                "    }\n" +
                "  }\n" +
                "</script>\n" +
                "<script src=\"https://www.youtube.com/iframe_api\"></script>\n" +
                "</body>\n" +
                "</html>";
    }
}