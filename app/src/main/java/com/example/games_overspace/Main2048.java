package com.example.games_overspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Main2048 extends AppCompatActivity {
    private int[][] board;
    private TextView[][] cells;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        board = new int[4][4];
        cells = new TextView[4][4];
        initCells();

        Button btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void initCells() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String cellID = "cell_" + i + j;
                int resID = getResources().getIdentifier(cellID, "id", getPackageName());
                cells[i][j] = findViewById(resID);
            }
        }
    }

    private void startGame() {
        initBoard();
        updateUI();
    }

    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }

        int count = 0;
        while (count < 2) {
            int randomRow = (int) (Math.random() * 4);
            int randomCol = (int) (Math.random() * 4);

            if (board[randomRow][randomCol] == 0) {
                board[randomRow][randomCol] = 2;
                count++;
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 2) {
                    board[i][j] = 0;
                }
            }
        }

        updateUI();
    }


    private void updateUI() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }


    public void goToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        moveCellsRight();
                    } else {
                        moveCellsLeft();
                    }
                    return true;
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        moveCellsDown();
                    } else {
                        moveCellsUp();
                    }
                    return true;
                }
            }

            return false;
        }
    }

    private void moveCellsRight() {
        for (int row = 0; row < 4; row++) {
            for (int col = 2; col >= 0; col--) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row, col + 1);
                }
            }
        }
    }

    private void moveCellsLeft() {
        for (int row = 0; row < 4; row++) {
            for (int col = 1; col < 4; col++) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row, col - 1);
                }
            }
        }
    }

    private void moveCellsDown() {
        for (int col = 0; col < 4; col++) {
            for (int row = 2; row >= 0; row--) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row + 1, col);
                }
            }
        }
    }

    private void moveCellsUp() {
        for (int col = 0; col < 4; col++) {
            for (int row = 1; row < 4; row++) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row - 1, col);
                }
            }
        }
    }

    private void moveCell(int fromRow, int fromCol, int toRow, int toCol) {
        int value = board[fromRow][fromCol];
        if (board[toRow][toCol] == value) {
            board[toRow][toCol] *= 2;
            board[fromRow][fromCol] = 0;
        } else if (board[toRow][toCol] == 0) {
            board[toRow][toCol] = value;
            board[fromRow][fromCol] = 0;
        }
    }
}
