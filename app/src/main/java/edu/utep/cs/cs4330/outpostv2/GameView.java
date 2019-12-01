package edu.utep.cs.cs4330.outpostv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import edu.utep.cs.cs4330.outpostv2.game.Game;

public class GameView extends View{

    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint tilePaint = new Paint();
    private Paint blackPaint = new Paint();
    private Paint selectionPaint = new Paint();
    private Paint movePaint = new Paint();
    private Bitmap[][] pieces = new Bitmap[ 2 ][ 6 ];
    private Rect[][] squares = new Rect[ 8 ][ 8 ];

    private char[][] grid;
    Game game;

    boolean gameover;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        context = context;
        tilePaint.setARGB( 255, 191, 128, 69 );
        blackPaint.setARGB( 255, 0, 0, 0 );
        selectionPaint.setARGB( 50, 0, 0, 0 );
        movePaint.setARGB( 255, 47, 94, 19 );
        calculateDimensions();
        initImages();
        initSquares();
        game = new Game();
        grid = game.getGrid();
        gameover = false;
    }

    private void initSquares(){
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                squares[ j ][ i ] = new Rect( i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight );
            }
        }
    }

    private void initImages(){
        pieces[ 0 ][ 0 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_pawn);
        pieces[ 0 ][ 1 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_knight);
        pieces[ 0 ][ 2 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_bishop);
        pieces[ 0 ][ 3 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_rook);
        pieces[ 0 ][ 4 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_queen);
        pieces[ 0 ][ 5 ] = BitmapFactory.decodeResource(getResources(), R.drawable.w_king);
        pieces[ 1 ][ 0 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_pawn);
        pieces[ 1 ][ 1 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_knight);
        pieces[ 1 ][ 2 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_bishop);
        pieces[ 1 ][ 3 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_rook);
        pieces[ 1 ][ 4 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_queen);
        pieces[ 1 ][ 5 ] = BitmapFactory.decodeResource(getResources(), R.drawable.b_king);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public void clear(){
        game.refresh();
        gameover = false;
        invalidate();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
        initSquares();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }
        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (numColumns == 0 || numRows == 0){ return; }
        canvas.drawColor(Color.rgb( 227, 180, 136 ));

        drawGrid( canvas );
        drawMoves( canvas );
    }

    private void drawMoves( Canvas canvas){
        Integer[] move;
        for( int i=0; i<game.moves.size(); i++ ){
            move = game.moves.get( i );
            if( game.board.getGrid()[ move[ 0 ] ][ move[ 1 ] ] == ' ' ){
                drawCircle( canvas, move[ 0 ] + 1, move[ 1 ] + 1 );
            }else{

            }
        }
    }

    private void drawCircle( Canvas canvas, int row, int col ){
        canvas.drawCircle( (cellHeight * col ) - (cellHeight / 2), (cellWidth * row) - ( cellWidth / 2 ), 25, movePaint );
    }


    private void drawGrid( Canvas  canvas ){
        for (int i = 0; i < numColumns; i++){
            for (int j = 0; j < numRows; j++){
                if ( i % 2 == 0 ^ j % 2 == 0 ){
                    canvas.drawRect( squares[ i ][ j ], tilePaint );
                }
                if( game.ps != '#' && game.row == i && game.col == j ){
                    canvas.drawRect( squares[ i ][ j ], selectionPaint );
                }
                drawPiece( canvas, i, j );
            }
        }

        canvas.drawLine(4 * cellWidth, 0, 4 * cellWidth, getHeight(), blackPaint);
        canvas.drawLine(( 4 * cellWidth ) + 1, 0, ( 4 * cellWidth) + 1, getHeight(), blackPaint);
        canvas.drawLine(( 4 * cellWidth ) - 1, 0, ( 4 * cellWidth) - 1, getHeight(), blackPaint);

    }

    private void drawPiece(Canvas canvas, int i, int j ){
        if( grid[ i ][ j ] == ' ' ){ return; }
        canvas.drawBitmap( piece( i, j ), null, squares[ i ][ j ], null );
    }

    private Bitmap piece( int r, int c ){
        if( grid[ r ][ c ] == 'p' ){ return pieces[ 1 ][ 0 ]; }
        else if( grid[ r ][ c ] == 'P' ){ return pieces[ 0 ][ 0 ]; }
        else if( grid[ r ][ c ] == 'r' ){ return pieces[ 1 ][ 3 ]; }
        else if( grid[ r ][ c ] == 'R' ){ return pieces[ 0 ][ 3 ]; }
        else if( grid[ r ][ c ] == 'n' ){ return pieces[ 1 ][ 1 ]; }
        else if( grid[ r ][ c ] == 'N' ){ return pieces[ 0 ][ 1 ]; }
        else if( grid[ r ][ c ] == 'b' ){ return pieces[ 1 ][ 2 ]; }
        else if( grid[ r ][ c ] == 'B' ){ return pieces[ 0 ][ 2 ]; }
        else if( grid[ r ][ c ] == 'q' ){ return pieces[ 1 ][ 4 ]; }
        else if( grid[ r ][ c ] == 'Q' ){ return pieces[ 0 ][ 4 ]; }
        else if( grid[ r ][ c ] == 'k' ){ return pieces[ 1 ][ 5 ]; }
        else if( grid[ r ][ c ] == 'K' ){ return pieces[ 0 ][ 5 ]; }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            onTouch( row, column );

            invalidate();
        }

        return true;
    }

    private void onTouch( int row, int col ){
        game.move( row, col );
        char status = game.gameStatus();
        if( status != 'p' ){
            gameover = true;
            Toast.makeText( super.getContext(), "game over!",
                    Toast.LENGTH_LONG).show();
        }
    }
}