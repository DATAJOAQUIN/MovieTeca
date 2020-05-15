package com.example.movieteca;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText email, clave;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private TextView registerText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.emailField);
        clave=findViewById(R.id.passwordField);
        btnLogin=findViewById(R.id.loginrBtn);
        registerText=findViewById(R.id.registerTxtView);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.loginProgressBar);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(register);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               final String mail=email.getText().toString();
               final String password=clave.getText().toString();

                if (mail.isEmpty()||password.isEmpty()){
                    mostrarMensaje("Por favor. Verifique todos los campos");
                }else {
                    email.setText("");
                    clave.setText("");
                    login(mail,password);
                }
            }
        });

    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
    }

    private void login(String mail, String password){

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    mostrarMensaje("Login correcto");


                }else {
                    mostrarMensaje("Login incorrecto"+task.getException().getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
