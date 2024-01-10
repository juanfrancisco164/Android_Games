package com.example.games_overspace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class MainSenku extends AppCompatActivity {

    private GridLayout gridLayoutSenku;
    private TextView[][] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senku);

        gridLayoutSenku = findViewById(R.id.gridLayoutSenku);
        initializeTextViews();

        Button btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeGameBoard();
            }
        });
    }

    private void initializeTextViews() {
        textViews = new TextView[7][7];

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                int resId = getResources().getIdentifier("textView" + (i + 1) + "_" + (j + 1), "id", getPackageName());
                textViews[i][j] = findViewById(resId);

                textViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleGameClick(view);
                    }
                });
            }
        }
    }

    private void initializeGameBoard() {

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 3 && j == 3) {
                    textViews[i][j].setText("0");
                } else {
                    textViews[i][j].setText("1");
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                textViews[i][j].setClickable(true);
            }
        }
    }


    private void handleGameClick(View view) {
        int clickedRow = 0, clickedCol = 0;
        outerloop:
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (textViews[i][j].equals(view)) {
                    clickedRow = i;
                    clickedCol = j;
                    break outerloop;
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                textViews[i][j].setClickable(false);
            }
        }

        textViews[clickedRow][clickedCol].setText("X");  // "X" representa un nuevo estado
    }


    public void goToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
