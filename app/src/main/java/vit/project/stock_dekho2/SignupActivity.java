package vit.project.stock_dekho2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    /*----------Variables----------*/
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoredatabase;
    private TextInputEditText name_txt,phone_txt,age_txt,mail_txt,password_txt,confpassword_txt;
    private Button signup_btn,login_btn;
    private ProgressBar p_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        /*----------Hooks----------*/
        name_txt = findViewById(R.id.name_txt);
        phone_txt = findViewById(R.id.phone_txt);
        age_txt = findViewById(R.id.age_txt);
        mail_txt = findViewById(R.id.mail_txt);
        password_txt = findViewById(R.id.password_txt);
        confpassword_txt = findViewById(R.id.confpassword_txt);
        signup_btn = findViewById(R.id.sigup_btn);
        login_btn = findViewById(R.id.login_btn);
        p_bar = findViewById(R.id.p_bar);
        mAuth = FirebaseAuth.getInstance();
        firestoredatabase = FirebaseFirestore.getInstance();


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void signup() {
        if(validateinfo()){
            p_bar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(
                    mail_txt.getText().toString().trim(),password_txt.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> data_task) {
                            if(data_task.isSuccessful()){
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                Map<String,Object> userdata = new HashMap<>();
                                assert userdata != null;

                                userdata.put("FullName",name_txt.getText().toString().trim());
                                userdata.put("PhoneNo.",phone_txt.getText().toString().trim());
                                userdata.put("Age",age_txt.getText().toString().trim());
                                userdata.put("Email",mail_txt.getText().toString().trim());

                                firestoredatabase.collection("UserData")
                                        .document(firebaseUser.getUid()).set(userdata)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> datasaving_task) {
                                                if(datasaving_task.isSuccessful()){
                                                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                    finish();
                                                    Toast.makeText(SignupActivity.this, "Sign Up Done!", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(SignupActivity.this, "Error TryAgain", Toast.LENGTH_SHORT).show();
                                                }
                                                p_bar.setVisibility(View.GONE);
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(SignupActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateinfo() {
        int numValidateCount=0;

        if(name_txt.getText().toString().trim().isEmpty()){
            name_txt.setError("Full name Required");
            name_txt.requestFocus();
            numValidateCount++;
        }
        if(phone_txt.getText().toString().trim().isEmpty()){
            phone_txt.setError("Phone No. Required");
            phone_txt.requestFocus();
            numValidateCount++;
        }
        else if(phone_txt.getText().toString().trim().length() != 10 ){
            phone_txt.setError("Please provide valid Phone No.");
            phone_txt.requestFocus();
            numValidateCount++;
        }
        if(age_txt.getText().toString().trim().isEmpty()){
            age_txt.setError("Please Enter your Age");
            age_txt.requestFocus();
            numValidateCount++;
        }
        if(mail_txt.getText().toString().trim().isEmpty()){
            mail_txt.setError("Email id required");
            mail_txt.requestFocus();
            numValidateCount++;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail_txt.getText().toString().trim()).matches()){
            mail_txt.setError("Please provide valid Email Address");
            mail_txt.requestFocus();
            numValidateCount++;
        }

        if(password_txt.getText().toString().trim().isEmpty()){
            password_txt.setError("Password is required");
            password_txt.requestFocus();
            numValidateCount++;
        }

        else if(password_txt.getText().toString().trim().length()<6){
            password_txt.setError("Min. 6 character is required");
            password_txt.requestFocus();
            numValidateCount++;
        }
        else if(! confpassword_txt.getText().toString().trim().equals(password_txt.getText().toString().trim())){
            confpassword_txt.setError("Password Don't Match");
            confpassword_txt.requestFocus();
            numValidateCount++;
        }

        return numValidateCount==0;
    }
}