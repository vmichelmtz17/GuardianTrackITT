package com.residencia.guardiantrackitt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_Familiar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_familiar);

        // Configurar el ActionBar y el NavigationDrawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        // Configurar el ActionBarDrawerToggle para abrir y cerrar el NavigationDrawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar el clic de los botones en el contenido principal
        Button buttonUbicacion = findViewById(R.id.buttonUbicacion);
        Button buttonPaciente = findViewById(R.id.buttonPaciente);
        Button buttonPaginaWeb = findViewById(R.id.buttonPaginaWeb);
        Button buttonContacto = findViewById(R.id.buttonContacto);

        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Familiar.this, Ubicacion.class);
                startActivity(intent);
            }
        });

        buttonPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Familiar.this, Paciente.class);
                startActivity(intent);
            }
        });

        buttonPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón de página web
                // Aún no está programado, puedes agregar el código más tarde
            }
        });

        buttonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Familiar.this, Contacto.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Maneja los clics en los elementos del menú del NavigationDrawer
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Intent profileIntent = new Intent(Home_Familiar.this, Perfil.class);
                startActivity(profileIntent);
                return true;
            case R.id.menu_logout:
                // Lógica para cerrar sesión
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(Home_Familiar.this, MainActivity.class);
                startActivity(logoutIntent);
                finish(); // Cierra la actividad actual
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Maneja los clics en el botón de menú de la ActionBar
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Cierra el NavigationDrawer si está abierto cuando se presiona el botón Atrás
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
