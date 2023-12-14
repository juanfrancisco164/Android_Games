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

        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
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
    }

    private void updateUI() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextView cell = cells[i][j];
                int cellValue = board[i][j];

                if (cellValue == 0) {
                    cell.setText("");
                } else {
                    cell.setText(String.valueOf(cellValue));
                }
            }
        }
    }

    public void goToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
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

    private void moveCellsLeft() {
        for (int row = 0; row < 4; row++) {
            for (int col = 1; col < 4; col++) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row, findTargetColLeft(row, col));
                }
            }
        }
        addRandomTwo();
        updateUI();
    }

    private int findTargetColLeft(int row, int col) {
        for (int targetCol = col - 1; targetCol >= 0; targetCol--) {
            if (board[row][targetCol] != 0) {
                if (board[row][targetCol] == board[row][col]) {
                    return targetCol;
                } else {
                    return targetCol + 1;
                }
            }
        }
        return 0;
    }

    private void moveCellsDown() {
        for (int col = 0; col < 4; col++) {
            for (int row = 2; row >= 0; row--) {
                if (board[row][col] != 0) {
                    moveCell(row, col, findTargetRowDown(row, col), col);
                }
            }
        }
        addRandomTwo();
        updateUI();
    }

    private int findTargetRowDown(int row, int col) {
        for (int targetRow = row + 1; targetRow < 4; targetRow++) {
            if (board[targetRow][col] != 0) {
                if (board[targetRow][col] == board[row][col]) {
                    return targetRow;
                } else {
                    return targetRow - 1;
                }
            }
        }
        return 3;
    }

    private void moveCellsUp() {
        for (int col = 0; col < 4; col++) {
            for (int row = 1; row < 4; row++) {
                if (board[row][col] != 0) {
                    moveCell(row, col, findTargetRowUp(row, col), col);
                }
            }
        }
        addRandomTwo();
        updateUI();
    }

    private int findTargetRowUp(int row, int col) {
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            if (board[targetRow][col] != 0) {
                if (board[targetRow][col] == board[row][col]) {
                    return targetRow;
                } else {
                    return targetRow + 1;
                }
            }
        }
        return 0;
    }

    private void moveCellsRight() {
        for (int row = 0; row < 4; row++) {
            for (int col = 2; col >= 0; col--) {
                if (board[row][col] != 0) {
                    moveCell(row, col, row, findTargetColRight(row, col));
                }
            }
        }
        addRandomTwo();
        updateUI();
    }

    private int findTargetColRight(int row, int col) {
        for (int targetCol = col + 1; targetCol < 4; targetCol++) {
            if (board[row][targetCol] != 0) {
                if (board[row][targetCol] == board[row][col]) {
                    return targetCol;
                } else {
                    return targetCol - 1;
                }
            }
        }
        return 3;
    }

    private void addRandomTwo() {
        int emptyCount = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    emptyCount++;
                }
            }
        }

        if (emptyCount > 0) {
            int randomIndex = (int) (Math.random() * emptyCount) + 1;
            int count = 0;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (board[i][j] == 0) {
                        count++;
                        if (count == randomIndex) {
                            board[i][j] = 2; // You can change this to any other value if needed
                            return;
                        }
                    }
                }
            }
        }
    }

    private void moveCell(int fromRow, int fromCol, int toRow, int toCol) {
        int value = board[fromRow][fromCol];
        if (board[toRow][toCol] == value && (fromRow != toRow || fromCol != toCol)) {
            board[toRow][toCol] *= 2;
            board[fromRow][fromCol] = 0;
        } else if (board[toRow][toCol] == 0) {
            board[toRow][toCol] = value;
            board[fromRow][fromCol] = 0;
        }
    }

}
