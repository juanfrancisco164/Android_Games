package com.example.games_overspace;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Arrays;


public class MainSenku extends AppCompatActivity {
    int[][] board = new int[7][7];
    TextView pieceSelected = null;
    TextView positionSelected = null;
    private TextView timeView;
    private CountDownTimer timer;
    private long timeLeftInMillis;
    GridLayout gridLayout;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senku);
        gridLayout = findViewById(R.id.gridLayoutSenku);
        timeView = findViewById(R.id.timeView);

        createTableSenku();
        startCountdownTimer();

        findViewById(R.id.btnGoToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMenu();
            }
        });
    }

    private void createBaseBoard() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 2 || row > 4) && (column < 2 || column > 4)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    PositionSenkuPiece position = new PositionSenkuPiece(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    PositionSenkuPiece position = new PositionSenkuPiece(row, column, "void");
                    textView.setTag(position);
                }



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }

    private void createPieces() {
        for (int row = 0; row < 7; row++){
            for (int column = 0; column < 7; column++){
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if (board[row][column] == 1 && (row != 3 || column != 3)){
                    textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
                    board[row][column] = 2;
                    PositionSenkuPiece position = new PositionSenkuPiece(row, column, "piece");
                    textView.setTag(position);
                }
            }
        }
    }

    private void addClickListenerToPiece(View piece) {
        piece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected == view){
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                    pieceSelected = null;
                    return;
                }

                if (pieceSelected != null){
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                }

                pieceSelected = (TextView) view;
                pieceSelected.setBackgroundResource(R.drawable.piece_senku_selected);
            }
        });
    }

    private void addClickListenerToVoid(View voidPiece){
        voidPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected != null){
                    positionSelected = (TextView) view;

                    if (checkIfPieceCanMove()){
                        pieceSelected.setBackgroundResource(R.drawable.piece_senku);

                        // Animacion moviemiento fichas
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Path path = new Path();
                            path.moveTo(pieceSelected.getX(), pieceSelected.getY());
                            path.lineTo(view.getX(), view.getY());

                            // Crear y ejecutar la animación
                            ObjectAnimator animator = ObjectAnimator.ofFloat(pieceSelected, View.X, View.Y, path);
                            animator.setDuration(200);
                            animator.start();
                        }
                        PositionSenkuPiece piecePosition = (PositionSenkuPiece) pieceSelected.getTag();
                        piecePosition.setRow(((PositionSenkuPiece) positionSelected.getTag()).getRow());
                        piecePosition.setColumn(((PositionSenkuPiece) positionSelected.getTag()).getColumn());
                        pieceSelected = null;
                        if (checkGameOver()){
                            showGameOverDialog();
                        } else if (checkWin()){
                            showWinDialog();
                        }
                    }else{
                        System.out.println("No se puede mover");
                    }
                }
            }
        });
    }

    private boolean checkIfPieceCanMove(){
        boolean canMove = false;

        PositionSenkuPiece piecePosition = (PositionSenkuPiece) pieceSelected.getTag();
        PositionSenkuPiece movePosition = (PositionSenkuPiece) positionSelected.getTag();

        if (piecePosition.getRow() == movePosition.getRow()){
            if (piecePosition.getColumn() - movePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() - 1] == 2){
                    deletePiece(new PositionSenkuPiece(piecePosition.getRow(), piecePosition.getColumn() - 1, "piece"));
                    canMove = true;
                }
            } else if (movePosition.getColumn() - piecePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() + 1] == 2){
                    deletePiece(new PositionSenkuPiece(piecePosition.getRow(), piecePosition.getColumn() + 1, "piece"));
                    canMove = true;
                }
            }
        } else if (piecePosition.getColumn() == movePosition.getColumn()){
            if (piecePosition.getRow() - movePosition.getRow() == 2){
                if (board[piecePosition.getRow() - 1][piecePosition.getColumn()] == 2){
                    deletePiece(new PositionSenkuPiece(piecePosition.getRow() - 1, piecePosition.getColumn(),"piece"));
                    canMove = true;
                }
            } else if (movePosition.getRow() - piecePosition.getRow() == 2){
                if (board[piecePosition.getRow() + 1][piecePosition.getColumn()] == 2){
                    deletePiece(new PositionSenkuPiece(piecePosition.getRow() + 1, piecePosition.getColumn(),"piece"));
                    canMove = true;
                }
            }
        }

        if (canMove){
            board[piecePosition.getRow()][piecePosition.getColumn()] = 1;
            board[movePosition.getRow()][movePosition.getColumn()] = 2;
        }

        return canMove;
    }

    private boolean checkPossibleMoves(PositionSenkuPiece position) {
        // Verificar arriba
        if (position.getRow() >= 2 && board[position.getRow() - 1][position.getColumn()] == 2 && board[position.getRow() - 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar abajo
        if (position.getRow() <= 4 && board[position.getRow() + 1][position.getColumn()] == 2 && board[position.getRow() + 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar izquierda
        if (position.getColumn() >= 2 && board[position.getRow()][position.getColumn() - 1] == 2 && board[position.getRow()][position.getColumn() - 2] == 1) {
            return true;
        }
        // Verificar derecha
        if (position.getColumn() <= 4 && board[position.getRow()][position.getColumn() + 1] == 2 && board[position.getRow()][position.getColumn() + 2] == 1) {
            return true;
        }
        return false;
    }

    private boolean checkGameOver() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) {
                    if (checkPossibleMoves(new PositionSenkuPiece(row, column, "piece"))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkWin() {
        int pieces = 0;
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) {
                    pieces++;
                }
            }
        }
        return pieces == 1;
    }

    private void showWinDialog() {
        long secondsLeft = timeLeftInMillis / 1000;
        saveWinTime(secondsLeft);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Another try?")
                .setTitle("Congratulations")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startNewGame(view);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveWinTime(long secondsLeft) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("bestTimeSenku", secondsLeft);
        editor.apply();
    }

    private void deletePiece(PositionSenkuPiece position){
        board[position.getRow()][position.getColumn()] = 1;

        for (int i = 0; i < gridLayout.getChildCount(); i++){
            TextView piece = (TextView) gridLayout.getChildAt(i);
            PositionSenkuPiece piecePosition = (PositionSenkuPiece) piece.getTag();
            if (piecePosition.getRow() == position.getRow() && piecePosition.getColumn() == position.getColumn() && piecePosition.getType().equals("piece")){
                gridLayout.removeView(piece);
                break;
            }
        }
    }

    public void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GAME OVER!!!\nAnother try?")
                .setTitle("Game over")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startNewGame(view);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startCountdownTimer() {
        timer = new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeView.setText("" + (millisUntilFinished / 1000));
                timeLeftInMillis = millisUntilFinished;
            }
            public void onFinish() {
                showGameOverDialog();
            }
        }.start();
    }

    private void createTableSenku() {
        createBaseBoard();
        createPieces();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void startNewGame(View view) {
        resetBoard();
        restartTimer();
    }

    private void resetBoard() {
        gridLayout.removeAllViews();
        for (int row = 0; row < board.length; row++) {
            Arrays.fill(board[row], 0);
        }
        createTableSenku();
    }

    private void restartTimer() {
        if (timer != null) {
            timer.cancel();
        }
        startCountdownTimer();
    }


    public void goToMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}