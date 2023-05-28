package com.residencia.guardiantrackitt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private Button websiteButton;
    private Button aboutButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userTypeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        userTypeRef = FirebaseDatabase.getInstance().getReference().child("users").child("userType");

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        websiteButton = findViewById(R.id.websiteButton);
        aboutButton = findViewById(R.id.aboutButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir página web en el navegador
                String url = "https://www.example.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse(url));
                startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // El usuario ya ha iniciado sesión, obtener el tipo de usuario desde Realtime Database
            userTypeRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.getValue(String.class);
                    if (userType != null) {
                        // Redirigir a la actividad Home correspondiente según el tipo de usuario
                        if (userType.equals("Familiar")) {
                            Intent intent = new Intent(MainActivity.this, Home_Familiar.class);
                            startActivity(intent);
                        } else if (userType.equals("Profesional")) {
                            Intent intent = new Intent(MainActivity.this, Home_Profesional.class);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        // El tipo de usuario no está definido en la base de datos
                        // Aquí puedes manejar el caso en que el tipo de usuario no esté configurado correctamente
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error al leer el tipo de usuario desde Realtime Database
                    // Aquí puedes manejar el caso de error de lectura de datos
                }
            });
        }
    }
}
