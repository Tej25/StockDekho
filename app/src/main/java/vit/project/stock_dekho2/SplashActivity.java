package vit.project.stock_dekho2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    /*----------Variables----------*/
    private ImageView img_logo;
    private TextView app_nametxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        /*----------Hooks----------*/
        img_logo = findViewById(R.id.img_logo);
        app_nametxt = findViewById(R.id.app_nametxt);

        /*----------Animation Loader----------*/
        img_logo.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in));
        app_nametxt.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in));

        /*----------Handler----------*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        },2500);
    }
}