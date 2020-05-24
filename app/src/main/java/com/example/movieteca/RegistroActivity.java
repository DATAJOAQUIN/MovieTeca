package com.example.movieteca;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistroActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText emailField, usernameField, passwordField;
    private FirebaseAuth mAuth;

    private TextView loginTxtView;
    private ProgressBar progressBar;
    private ImageView userPhoto;

    static int PReqCode=1;
    static int REQUEST_CODE=1;
    Uri imagenElegida;

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
        userPhoto=findViewById(R.id.user_foto);

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
                                actualizarUserInfo(username,imagenElegida, mAuth.getCurrentUser());
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

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>=22){

                    comprobaryPedirPermisos();
                }else {
                    abrirGaleria();
                }
            }
        });
    }


    private void  actualizarUserInfo(final String name,Uri imagenElegida ,final FirebaseUser currentUser){

        //cargar imagen en Firebase Storage y conseguir url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("fotos_usuarios");
        final StorageReference imageFilePath = mStorage.child(imagenElegida.getLastPathSegment());
        imageFilePath.putFile(imagenElegida).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
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
                });

            }
        });


    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
    }

    //Permisos para abrir la galería de Imágenes
    private void comprobaryPedirPermisos() {

        if (ContextCompat.checkSelfPermission(RegistroActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistroActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegistroActivity.this,"Por favor acepte el permiso requerido",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(RegistroActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            abrirGaleria();
    }

    private void abrirGaleria() {

        Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null ) {

            // el usuario ha seleccionado una imagen
            // Hay que guardar la referencia en una variable Uri
            imagenElegida = data.getData() ;
            userPhoto.setImageURI(imagenElegida);

        }
    }
}
