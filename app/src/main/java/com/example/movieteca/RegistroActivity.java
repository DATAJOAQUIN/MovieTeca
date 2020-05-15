package com.example.movieteca;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText emailField, usernameField, passwordField;
    private FirebaseAuth mAuth;

    private TextView loginTxtView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        loginTxtView = (TextView)findViewById(R.id.loginTxtView);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        emailField = (EditText)findViewById(R.id.emailField);
        usernameField = (EditText)findViewById(R.id.usernameField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        progressBar=findViewById(R.id.regProgressBar);

        mAuth = FirebaseAuth.getInstance();

        loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(RegistroActivity.this, "CARGANDO...", Toast.LENGTH_LONG).show();
                final String username = usernameField.getText().toString().trim();
                final String email = emailField.getText().toString().trim();
                final String password = passwordField.getText().toString().trim();


                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                mostrarMensaje("Cuenta creada");
                                actualizarUserInfo(username, mAuth.getCurrentUser());
                            }else {
                                mostrarMensaje("Fallo en la creación de cuenta "+task.getException().getMessage());
                                Log.d("Fallo",task.getException().getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {

                    Toast.makeText(RegistroActivity.this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void  actualizarUserInfo(final String name, final FirebaseUser currentUser){
        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mostrarMensaje("Registro completo");
                            Intent intent=new Intent(RegistroActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
    }
}
