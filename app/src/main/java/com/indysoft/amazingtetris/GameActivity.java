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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    final int NUM_ROWS = 26;
    final int NUM_COLUMNS = 16;
    final int BOARD_HEIGHT = 800;
    final int BOARD_WIDTH = 400;
    final Handler handler = new Handler();
    final Shape[] shapes = new Shape[7];
    final int UP_DIRECTION = 0;
    final int RIGHT_DIRECTION = 1;
    final int DOWN_DIRECTION = 2;
    final int LEFT_DIRECTION = 3;
    int score;

    final int dx[] = {-1, 0, 1, 0};
    final int dy[] = {0, 1, 0, -1};

    Random random = new Random();

    BoardCell[][] gameMatrix;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    LinearLayout linearLayout;

    Shape currentShape;


    private void ShapesInit(){
        int[][] a = new int[5][5];

        for (int i = 0; i < 5; ++ i){
            for (int j = 0; j < 5; ++ j){
                a[i][j] = 0;
            }
        }

        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 1;
        shapes[0] = new Shape(a, Color.rgb(187, 255, 0));
        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 0;

        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 1;
        shapes[1] = new Shape(a, Color.rgb(200, 0, 255));
        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 0;

        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 1;
        shapes[2] = new Shape(a, Color.rgb(0, 28, 196));
        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 0;

        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 1;
        shapes[3] = new Shape(a, Color.rgb(227, 235, 0));
        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 0;

        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 1;
        shapes[4] = new Shape(a, Color.rgb(235, 0, 0));
        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 0;

        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 1;
        shapes[5] = new Shape(a, Color.rgb(255, 170, 0));
        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 0;

        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 1;
        shapes[6] = new Shape(a, Color.rgb(0, 138, 44));
        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 0;

    }
    private boolean MoveShape(final int direction, Shape nowShape){
        // copy the gameMatrix in aux
        BoardCell[][] aux = gameMatrix;
        int i, ii, j, jj;
        // eliminate the shape from the table
        for (ii = nowShape.x, i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y, j = 1; j <= 4; ++ j, ++ jj){
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj] = new BoardCell();
                }
            }
        }

        // try to move the shape to the specified direction
        for (ii = nowShape.x + dx[direction], i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y + dy[direction], j = 1; j <= 4; ++ j, ++ jj){
                gameMatrix[ii][jj].setState(gameMatrix[ii][jj].getState() + nowShape.mat[i][j].getState());
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj].setColor(nowShape.mat[i][j].getColor());
                }
                if (gameMatrix[ii][jj].getState() > 1){
                    gameMatrix = aux;
                    return false;
                }
            }
        }
        nowShape.x += dx[direction];
        nowShape.y += dy[direction];
        return true;
    }
    private boolean RotateLeft(Shape nowShape){
        // copy the gameMatrix in aux
        BoardCell[][] aux = gameMatrix;
        int i, ii, j, jj;
        // eliminate the shape from the gameMatrix
        for (ii = nowShape.x, i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y, j = 1; j <= 4; ++ j, ++ jj){
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj] = new BoardCell();
                }
            }
        }
        // rotate the shape to left
        nowShape.RotateLeft();
        // ... and try to put it again on the table
        for (ii = nowShape.x, i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y, j = 1; j <= 4; ++ j, ++ jj){
                gameMatrix[ii][jj].setState(gameMatrix[ii][jj].getState() + nowShape.mat[i][j].getState());
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj].setColor(nowShape.mat[i][j].getColor());
                }
                // if we can't put the rotated shape on the table
                if (gameMatrix[ii][jj].getState() > 1){
                    // then recreate the initial state of the table
                    gameMatrix = aux;
                    // ... and rotate the shape to right, to obtain its initial state
                    nowShape.RotateRight();
                    return false;
                }
            }
        }
        return true;
    }
    private boolean RotateRight(Shape nowShape){
        // copy the gameMatrix in aux
        BoardCell[][] aux = gameMatrix;
        int i, ii, j, jj;
        // eliminate the shape from the gameMatrix
        for (ii = nowShape.x, i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y, j = 1; j <= 4; ++ j, ++ jj){
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj] = new BoardCell();
                }
            }
        }
        // rotate the shape to right
        nowShape.RotateRight();
        // ... and try to put it again on the table
        for (ii = nowShape.x, i = 1; i <= 4; ++ i, ++ ii){
            for (jj = nowShape.y, j = 1; j <= 4; ++ j, ++ jj){
                gameMatrix[ii][jj].setState(gameMatrix[ii][jj].getState() + nowShape.mat[i][j].getState());
                if (nowShape.mat[i][j].getState() == 1){
                    gameMatrix[ii][jj].setColor(nowShape.mat[i][j].getColor());
                }
                // if we can't put the rotated shape on the table
                if (gameMatrix[ii][jj].getState() > 1){
                    // then recreate the initial state of the table
                    gameMatrix = aux;
                    // ... and rotate the shape to left, to obtain its initial state
                    nowShape.RotateLeft();
                    return false;
                }
            }
        }
        return true;
    }
    private boolean CreateShape(){
        // generate random shape to put on the gameMatrix
        currentShape = shapes[random.nextInt(shapes.length)];
        // generate random number of rotations
        int number_of_rotations = random.nextInt(4);
        for (int i = 1; i <= number_of_rotations; ++ i){
            currentShape.RotateRight();
        }
        currentShape.x = 0; currentShape.y = 6;
        // put the new generated shape adjacent to the top side of the table if possible
        for(int offset = 0; offset <= 3; ++ offset){
            int i, ii, j, jj;
            boolean ok = true;
            for (ii = currentShape.x + offset, i = 1; i <= 4; ++ i, ++ ii){
                for (jj = currentShape.y, j = 1; j <= 4; ++ j, ++ jj){
                    gameMatrix[ii][jj].setState(gameMatrix[ii][jj].getState() + currentShape.mat[i][j].getState());
                    if (gameMatrix[ii][jj].getState() > 1){
                        ok = false;
                    }
                }
            }
            if (ok){
                for (i = 1, ii = currentShape.x + offset; i <= 4; ++ i, ++ ii){
                    for (j = 1, jj = currentShape.y; j <= 4; ++ j, ++ jj){
                        if (currentShape.mat[i][j].getState() == 1){
                            gameMatrix[ii][jj].setColor(currentShape.mat[i][j].getColor());
                        }
                    }
                }
                currentShape.x += offset;
                return true;
            }
            else{
                for (ii = currentShape.x + offset, i = 1; i <= 4; ++ i, ++ ii){
                    for (jj = currentShape.y, j = 1; j <= 4; ++ j, ++ jj){
                        gameMatrix[ii][jj].setState(gameMatrix[ii][jj].getState() - currentShape.mat[i][j].getState());
                    }
                }
            }
        }
        return false;
    }
    private boolean Check(){
        int k = 0;
        boolean found = false;
        for (int i = NUM_ROWS - 4; i >= 3; -- i){
            boolean ok = true;
            for (int j = 3; j < NUM_COLUMNS - 3; ++ j){
                if (gameMatrix[i][j].getState() == 0){
                    ok = false;
                }
            }
            if (ok){
                ++ k;
                found = true;
                continue;
            }
            else{
                for (int j = 3; j < NUM_COLUMNS-3; ++ j){
                    gameMatrix[i-k][j] = gameMatrix[i][j];
                }
            }
        }
        for (int pas = 1; pas <= k; ++ pas){
            for (int j = 3; j < NUM_COLUMNS - 3; ++ j){
                gameMatrix[3 + pas - 1][j] = new BoardCell();
            }
        }
        score += k * (k + 1) / 2;
        return found;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bitmap = Bitmap.createBitmap(BOARD_WIDTH, BOARD_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        linearLayout = (LinearLayout) findViewById(R.id.game_board);
        score = 0;

        ShapesInit();

        GameInit();
    }

    void PaintMatrix() {

        // Paint the game board background
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT, paint);

        // Paint the grid on the game board
        paint.setColor(Color.WHITE);
        for (int i = 0; i <= (NUM_ROWS - 6); ++i) {
            canvas.drawLine(0, i * (BOARD_HEIGHT / (NUM_ROWS - 6)), BOARD_WIDTH, i * (BOARD_HEIGHT / (NUM_ROWS - 6)), paint);
        }
        for (int i = 0; i <= (NUM_COLUMNS - 6); ++i) {
            canvas.drawLine(i * (BOARD_WIDTH / (NUM_COLUMNS - 6)), 0, i * (BOARD_WIDTH / (NUM_COLUMNS - 6)), BOARD_HEIGHT, paint);
        }

        // Paint the tetris blocks
        for (int i = 3; i < NUM_ROWS - 3; ++i) {
            for (int j = 3; j < NUM_COLUMNS - 3; ++j) {
                if (gameMatrix[i][j].getState() == 1) {
                    paint.setColor(gameMatrix[i][j].getColor());
                    canvas.drawRect((j - 3) * (BOARD_WIDTH / (NUM_COLUMNS - 6)),
                            (i - 3) * (BOARD_HEIGHT / (NUM_ROWS - 6)),
                            (j + 1 - 3) * (BOARD_WIDTH / (NUM_COLUMNS - 6)),
                            (i + 1 - 3) * (BOARD_HEIGHT / (NUM_ROWS - 6)),
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

        // Marking the first and the last 3 lines and columns as occupied.

        for (int j = 0; j < NUM_COLUMNS; ++ j){
            for (int i = 0; i <= 2; ++ i) {
                gameMatrix[i][j] = new BoardCell(1, Color.BLACK);
            }
            for (int i = NUM_ROWS-3; i < NUM_ROWS; ++ i){
                gameMatrix[i][j] = new BoardCell(1, Color.BLACK);
            }
        }

        for (int i = 0; i < NUM_ROWS; ++ i){
            for (int j = 0; j <= 2; ++ j) {
                gameMatrix[i][j] = new BoardCell(1, Color.BLACK);
            }
            for (int j = NUM_COLUMNS - 3; j < NUM_COLUMNS; ++ j){
                gameMatrix[i][j] = new BoardCell(1, Color.BLACK);
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

    public class Shape{
        public int x, y;
        public BoardCell[][] mat = new BoardCell[5][5];

        Shape(){
            for (int i = 0; i < 5; ++ i){
                for (int j = 0; j < 5; ++ j){
                    mat[i][j] = new BoardCell();
                }
            }
            x = y = 0;
        }

        Shape(int[][] _mat, int _color) {
            for (int i = 0; i < 5; ++ i){
                for (int j = 0; j < 5; ++ j){
                    if (_mat[i][j] == 1) {
                        mat[i][j] = new BoardCell(_mat[i][j], _color);
                    }
                    else{
                        mat[i][j] = new BoardCell();
                    }

                }
            }
            x = y = 0;
        }

        Shape(int[][] _mat, int _color, int _x, int _y){
            for (int i = 0; i < 5; ++ i){
                for (int j = 0; j < 5; ++ j){
                    if (_mat[i][j] == 1) {
                        mat[i][j] = new BoardCell(_mat[i][j], _color);
                    }
                    else{
                        mat[i][j] = new BoardCell();
                    }

                }
            }
            x = _x;
            y = _y;
        }

        void RotateLeft(){
            BoardCell[][] aux = new BoardCell[5][5];
            for (int i = 1; i < 5; ++ i){
                for (int j = 1; j < 5; ++ j){
                    aux[4 - j + 1][i] = mat[i][j];
                }
            }
            for (int i = 1; i < 5; ++ i){
                for (int j = 1; j < 5; ++ j){
                    mat[i][j] = aux[i][j];
                }
            }
        }

        void RotateRight(){
            BoardCell[][] aux = new BoardCell[5][5];
            for (int i = 1; i < 5; ++ i){
                for (int j = 1; j < 5; ++ j){
                    aux[j][4 - i + 1] = mat[i][j];
                }
            }
            for (int i = 1; i < 5; ++ i){
                for (int j = 1; j < 5; ++ j){
                    mat[i][j] = aux[i][j];
                }
            }
        }
    }
}

