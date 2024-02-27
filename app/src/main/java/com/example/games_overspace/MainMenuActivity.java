package com.example.games_overspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private TextView nameUserMenu;
    private RecyclerView recyclerViewMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        nameUserMenu = findViewById(R.id.nameUserMenu);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);

        updateUsernameDisplay();

        List<String> menuItems = Arrays.asList("2048", "Senku", "Settings");
        MenuAdapter adapter = new MenuAdapter(menuItems, this::onMenuItemClick);
        recyclerViewMenu.setAdapter(adapter);
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onMenuItemClick(View view) {
        String menuItem = (String) view.getTag();
        switch (menuItem) {
            case "2048":
                start2048(null);
                break;
            case "Senku":
                startSenku(null);
                break;
            case "Settings":
                goSettings(null);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameDisplay();
    }

    private void updateUsernameDisplay() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Usuario");
        nameUserMenu.setText(username);
    }

    public void start2048(View view) {
        Intent intent = new Intent(this, Main2048.class);
        startActivity(intent);
    }

    public void startSenku(View view) {
        Intent intent = new Intent(this, MainSenku.class);
        startActivity(intent);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(this, SettingsMenu.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Deshabilitar el botón de retroceso. IGNORAR ERROR, funciona
    }
}
