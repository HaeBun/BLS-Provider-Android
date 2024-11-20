package com.tetraplace.snedu.player;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tetraplace.snedu.R;
import com.tetraplace.snedu.databinding.ActivityPlayerBinding;
import com.tetraplace.snedu.util.PlayerInterface;

public class PlayerActivity extends AppCompatActivity {

    public WebView webView;
    public ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        setDisplaySetting();

        webView = binding.webviewPlayer;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            private View customView;
            private WebChromeClient.CustomViewCallback customViewCallback;
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

                customViewCallback = callback;
                ((FrameLayout) getWindow().getDecorView()).addView(customView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }

            @Override
            public void onHideCustomView() {
                // 전체화면에서 복귀
                ((FrameLayout) getWindow().getDecorView()).removeView(customView);
                customView = null;
                getWindow().getDecorView().setSystemUiVisibility(originalSystemUiVisibility);
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;
            }
        });

        webView.loadDataWithBaseURL("https://www.youtube.com", getHtmlData("cf8Mm1Dyi7U", 0), "text/html", "UTF-8", null);
        webView.addJavascriptInterface(new PlayerInterface(this), "Android");
    }

    public void setDisplaySetting() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                // 상태 표시줄과 네비게이션 바 숨기기
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                // 스와이프하여 네비게이션 바 올림
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
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
                "    object-fit: contain; \n" +
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
                "        controls: 1,\n" +
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
                "        Android.onFullScreen(false); // 전체화면 상태 전달\n" +
                "\n" +
                "        // 전체화면 모드일 때 화면 회전\n" +
                "        if (event.target.isFullscreen()) {\n" +
                "          // 가로 모드로 회전\n" +
                "          screen.orientation.lock('landscape-primary');\n" +
                "        } else {\n" +
                "          // 세로 모드로 회전\n" +
                "          screen.orientation.lock('portrait-primary');\n" +
                "        }\n" +
                "        break;\n" +
                "    }\n" +
                "  }\n" +
                "</script>\n" +
                "<script src=\"https://www.youtube.com/iframe_api\"></script>\n" +
                "</body>\n" +
                "</html>";
    }
}