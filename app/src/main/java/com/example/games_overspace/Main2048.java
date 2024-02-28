package com.example.games_overspace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int points = 0;
    private int record = 0;
    private boolean isGameOverDialogShown = false;

    private int[][] previousBoard = new int[4][4];
    private int previousPoints = 0;
    private boolean canUndo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);

        board = new int[4][4];
        cells = new TextView[4][4];
        initCells();

        TextView btnStartGame = findViewById(R.id.btnStartGame);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        record = sharedPreferences.getInt("bestScore2048", 0);
        TextView textViewRecord = findViewById(R.id.textViewRecord);
        textViewRecord.setText(String.valueOf(record));

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
        new AlertDialog.Builder(this)
                .setTitle("Nueva Partida")
                .setMessage("¿Estás seguro de que quieres comenzar una nueva partida?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (points > record) {
                            updateRecordIfNeeded();
                            showWinDialog();
                        }
                        resetGame();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showWinDialog() {
        new AlertDialog.Builder(this)
                .setTitle("¡Nuevo récord!")
                .setMessage("¡Has establecido un nuevo récord con " + points + " puntos!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                })
                .show();
    }

    private void resetGame() {
        if (points > record) {
            updateRecordIfNeeded();
        }
        points = 0;
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

        TextView textViewPoints = findViewById(R.id.textViewPoints);
        textViewPoints.setText(String.valueOf(points));

        TextView textViewRecord = findViewById(R.id.textViewRecord);

        if (isGameOver()) {
            showGameOverPopup();

            if (points > record) {
                record = points;
            }
            points = 0;
        }

        textViewRecord.setText(String.valueOf(record));
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

    private void saveCurrentState() {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(board[i], 0, previousBoard[i], 0, 4);
        }
        previousPoints = points;
        canUndo = true;
    }


    private void moveCellsLeft() {
        saveCurrentState();
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
        saveCurrentState();
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
        saveCurrentState();
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
        saveCurrentState();
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
                            board[i][j] = 2;
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

            points += board[toRow][toCol];
        } else if (board[toRow][toCol] == 0) {
            board[toRow][toCol] = value;
            board[fromRow][fromCol] = 0;
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((i < 3 && board[i][j] == board[i + 1][j]) || (j < 3 && board[i][j] == board[i][j + 1])) {
                    return false;
                }
            }
        }

        return true;
    }

    private void showGameOverPopup() {
        if (!isGameOverDialogShown) {
            updateRecordIfNeeded();
            isGameOverDialogShown = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Has obtenido: " + points)
                    .setTitle("Game Over")
                    .setCancelable(false)
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            startGame();
                            isGameOverDialogShown = false;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void updateRecordIfNeeded() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int bestScore2048 = sharedPreferences.getInt("bestScore2048", 0);
        if (points > bestScore2048) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("bestScore2048", points);
            editor.apply();
            record = points;

            TextView textViewRecord = findViewById(R.id.textViewRecord);
            textViewRecord.setText(String.valueOf(record));
        }
    }
    public void undoLastMove(View view) {
        if (canUndo) {
            for (int i = 0; i < 4; i++) {
                System.arraycopy(previousBoard[i], 0, board[i], 0, 4);
            }
            points = previousPoints;
            updateUI();
            canUndo = false;
        } else {
            showCannotUndoDialog();
        }
    }
    private void showCannotUndoDialog() {
        new AlertDialog.Builder(this)
                .setMessage("No puedes deshacer más de una vez por movimiento.")
                .setPositiveButton("OK", null)
                .show();
    }
}
