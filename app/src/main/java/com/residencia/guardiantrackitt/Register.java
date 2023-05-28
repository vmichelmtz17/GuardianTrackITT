package com.residencia.guardiantrackitt;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView passwordRequirementsTextView;
    private EditText confirmPasswordEditText;
    private CheckBox showPasswordCheckbox;
    private EditText phoneEditText;
    private RadioGroup userTypeRadioGroup;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userTypeRef;
    private DatabaseReference userDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        userTypeRef = FirebaseDatabase.getInstance().getReference().child("users").child("userType");
        userDataRef = FirebaseDatabase.getInstance().getReference().child("users").child("userData");

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordRequirementsTextView = findViewById(R.id.passwordRequirementsTextView);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);
        phoneEditText = findViewById(R.id.phoneEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        registerButton = findViewById(R.id.registerButton);

        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                int selectedUserType = userTypeRadioGroup.getCheckedRadioButtonId();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                        || phone.isEmpty() || selectedUserType == -1) {
                    Toast.makeText(Register.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")) {
                    Toast.makeText(Register.this, "La contraseña debe tener al menos 6 caracteres, una mayúscula y una minúscula", Toast.LENGTH_SHORT).show();
                } else {
                    String userType = getUserType(selectedUserType);
                    registerUser(name, email, password, phone, userType);
                }
            }
        });
    }

    private String getUserType(int selectedId) {
        RadioButton radioButton = findViewById(selectedId);
        return radioButton.getText().toString();
    }

    private void registerUser(String name, String email, String password, String phone, final String userType) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso, guardar el tipo de usuario en Realtime Database
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                userTypeRef.child(currentUser.getUid()).setValue(userType)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Guardar campos adicionales en Realtime Database
                                                    DatabaseReference userRef = userDataRef.child(currentUser.getUid());
                                                    userRef.child("name").setValue(name);
                                                    userRef.child("phone").setValue(phone);

                                                    // Redirigir a la actividad Home correspondiente según el tipo de usuario
                                                    if (userType.equals("Familiar")) {
                                                        Intent intent = new Intent(Register.this, Home_Familiar.class);
                                                        startActivity(intent);
                                                    } else if (userType.equals("Profesional")) {
                                                        Intent intent = new Intent(Register.this, Home_Profesional.class);
                                                        startActivity(intent);
                                                    }
                                                    finish();
                                                } else {
                                                    // Error al guardar el tipo de usuario en la base de datos
                                                    // Aquí puedes manejar el caso de error de escritura de datos
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error en el registro de usuario
                            // Aquí puedes manejar el caso de error de registro
                        }
                    }
                });
    }
}
