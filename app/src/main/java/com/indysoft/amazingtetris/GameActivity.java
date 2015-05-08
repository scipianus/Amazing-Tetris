package com.indysoft.amazingtetris;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    final int NUM_ROWS = 20;
    final int NUM_COLUMNS = 10;
    final int BOARD_HEIGHT = 800;
    final int BOARD_WIDTH = 400;
    final Handler handler = new Handler();
    BoardCell[][] gameMatrix;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bitmap = Bitmap.createBitmap(BOARD_WIDTH, BOARD_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        linearLayout = (LinearLayout) findViewById(R.id.game_board);

        GameInit();

    }

    void PaintMatrix() {

        // Paint the game board background
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT, paint);

        // Paint the grid on the game board
        paint.setColor(Color.WHITE);
        for (int i = 0; i <= NUM_ROWS; ++i) {
            canvas.drawLine(0, i * (BOARD_HEIGHT / NUM_ROWS), BOARD_WIDTH, i * (BOARD_HEIGHT / NUM_ROWS), paint);
        }
        for (int i = 0; i <= NUM_COLUMNS; ++i) {
            canvas.drawLine(i * (BOARD_WIDTH / NUM_COLUMNS), 0, i * (BOARD_WIDTH / NUM_COLUMNS), BOARD_HEIGHT, paint);
        }

        // Paint the tetris blocks
        for (int i = 0; i < NUM_ROWS; ++i) {
            for (int j = 0; j < NUM_COLUMNS; ++j) {
                if (gameMatrix[i][j].getState() == 1) {
                    paint.setColor(gameMatrix[i][j].getColor());
                    canvas.drawRect(j * (BOARD_WIDTH / NUM_COLUMNS),
                            i * (BOARD_HEIGHT / NUM_ROWS),
                            (j + 1) * (BOARD_WIDTH / NUM_COLUMNS),
                            (i + 1) * (BOARD_HEIGHT / NUM_ROWS),
                            paint);
                }
            }
        }

        // Display the current painting
        linearLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    void GameInit() {

        // Create the game board (backend)
        gameMatrix = new BoardCell[NUM_ROWS][];
        for (int i = 0; i < NUM_ROWS; ++i) {
            gameMatrix[i] = new BoardCell[NUM_COLUMNS];
            for (int j = 0; j < NUM_COLUMNS; ++j) {
                gameMatrix[i][j] = new BoardCell();
            }
        }

        // Paint the initial matrix (frontend)
        PaintMatrix();

        // Set a timer
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                //Perform background work here

                handler.post(new Runnable() {
                    public void run() {
                        //Perform GUI updation work here
                        PaintMatrix();
                        Toast toast = Toast.makeText(getApplicationContext(), "Timer working", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        };
        timer.schedule(timerTask, 10, 3000); // after 10ms, it runs every 3000ms
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public class BoardCell {

        private int state, color;
        // state = 0 means free cell, state = 1 means occupied cell

        public BoardCell() {
            state = 0;
            color = Color.BLACK;
        }

        public BoardCell(int state, int color) {
            this.state = state;
            this.color = color;
        }

        public int getState() {
            return state;
        }

        public int getColor() {
            return color;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}

