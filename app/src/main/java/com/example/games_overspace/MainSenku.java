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

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isCellSelected = false;
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

        if (!isCellSelected) {
            if (textViews[clickedRow][clickedCol].getText().toString().equals("1")) {
                selectedRow = clickedRow;
                selectedCol = clickedCol;
                textViews[selectedRow][selectedCol].setBackgroundResource(R.drawable.border_selected);
                isCellSelected = true;
            }
        } else {
            if (textViews[clickedRow][clickedCol].getText().toString().equals("0") &&
                    ((Math.abs(clickedRow - selectedRow) == 2 && clickedCol == selectedCol) ||
                            (Math.abs(clickedCol - selectedCol) == 2 && clickedRow == selectedRow))) {
                textViews[selectedRow][selectedCol].setText("0");
                textViews[(clickedRow + selectedRow) / 2][(clickedCol + selectedCol) / 2].setText("0");
                textViews[clickedRow][clickedCol].setText("1");

                textViews[selectedRow][selectedCol].setBackgroundResource(R.drawable.border_selected);

                isCellSelected = false;
            } else {
                textViews[selectedRow][selectedCol].setBackgroundResource(0);
                selectedRow = clickedRow;
                selectedCol = clickedCol;
                textViews[selectedRow][selectedCol].setBackgroundResource(R.drawable.border_selected);
            }
        }

        if (!isCellSelected) {
            textViews[selectedRow][selectedCol].setBackgroundResource(0);
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                textViews[i][j].setClickable(true);
            }
        }
    }


    public void goToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
