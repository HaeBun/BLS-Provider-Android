package com.tetraplace.snedu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tetraplace.snedu.R;
import com.tetraplace.snedu.databinding.ActivityMainBinding;
import com.tetraplace.snedu.player.PlayerActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setDisplaySetting();

        binding.constraintlayoutMainPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            startActivity(intent);
        });

    }

    public void setDisplaySetting() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
}