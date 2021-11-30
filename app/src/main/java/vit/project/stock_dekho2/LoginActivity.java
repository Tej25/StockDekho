package vit.project.stock_dekho2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    /*----------Variables----------*/
    private FirebaseAuth mAuth;
    private TextInputEditText mail_txt,password_txt;
    private Button login_btn,forgotpass_btn,signup_btn;
    private ProgressBar p_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        /*----------Hooks----------*/
        mail_txt = findViewById(R.id.mail_txt);
        password_txt = findViewById(R.id.password_txt);
        login_btn = findViewById(R.id.login_btn);
        forgotpass_btn = findViewById(R.id.forgotpassword_btn);
        signup_btn = findViewById(R.id.sigup_btn);
        p_bar = findViewById(R.id.p_bar);
        mAuth = FirebaseAuth.getInstance();


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        forgotpass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });
    }

    private void login() {
        p_bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(
                mail_txt.getText().toString().trim(),password_txt.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> login_task) {
                        if(login_task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect Mail or Password!!", Toast.LENGTH_SHORT).show();
                        }

                        p_bar.setVisibility(View.GONE);
                    }
                });
    }
}