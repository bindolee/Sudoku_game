package sbin.com.sudoku_sbin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import static android.view.MotionEvent.ACTION_BUTTON_RELEASE;

/**
 * Created by sbin on 10/19/2016.
 */

public class PuzzleView extends View{

    private static final String SELX = "selX";
    private static final String SELY = "selY";
    private static final String VIEW_STATE = "viewState";
    private static final int ID = 42;

    private static final String LOG_TAG = "PuzzleView";
    private final GameActivity game;

    private float width;    // width of one tile
    private float height;   // height of one tile
    private int selX;       // X index of selection
    private int selY;       // Y index of selection
    private final Rect selRect = new Rect();


    /* if you change the screen orientation while sudoku is running. You will notice that it forgets where its cursor is.
    ** that's because we use a custom puzzleview view. Normal android views save their view state automatically, but since
    ** we made our own, we don't get that free. Unlike persistent state, instance state is not permanent. It lives in a bundle
    ** class on Android's application stack. Instance state is intended to be used for small bits of information such as cursor positions.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        Bundle args = new Bundle();
        args.putInt(SELX, selX);
        args.putInt(SELY, selY);
        args.putParcelable(VIEW_STATE, p);
        return args;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle args = new Bundle();
        select(args.getInt(SELX), args.getInt(SELY));
        super.onRestoreInstanceState(args.getParcelable(VIEW_STATE));
    }

    public PuzzleView(Context context) {
        super(context);
        this.game = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        setId(ID);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawGridLines(canvas);
        drawNumbers(canvas);
        drawSelection(canvas);

        if (Prefs.getMusic(getContext())) {
            drawHints(canvas);
            Log.i(LOG_TAG, "PuzzleView::Pref is checked and hint is on");
        }
    }

    private void drawHints(Canvas canvas) {
        // Draw the hints...

        // Pick a hint color based on #moves left
        Paint hint = new Paint();
        int c[] = { getResources().getColor(R.color.puzzle_hint_0),
                getResources().getColor(R.color.puzzle_hint_1),
                getResources().getColor(R.color.puzzle_hint_2), };
        Rect r = new Rect();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int movesleft = 9 - game.getUsedTiles(i, j).length;
                if (movesleft < c.length) {
                    getRect(i, j, r);
                    hint.setColor(c[movesleft]);
                    canvas.drawRect(r, hint);
                }
            }
        }
    }

    private void drawSelection(Canvas canvas) {
        // Draw the selection...
        Log.i(LOG_TAG, "PuzzleView::selRect=" + selRect);
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(
                R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);
    }

    private void drawNumbers(Canvas canvas) {
        // Define color and style for numbers
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(
                R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        // Draw the number in the center of the tile
        Paint.FontMetrics fm = foreground.getFontMetrics();
        // Centering in X: use alignment (and X at midpoint)
        float x = width / 2;
        // Centering in Y: measure ascent/descent first
        float y = height / 2 - (fm.ascent + fm.descent) / 2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                canvas.drawText(this.game.getTileString(i, j), i
                        * width + x, j * height + y, foreground);

            }
        }
    }

    private void drawBackground(Canvas canvas) {
        //Draw background
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0,0,getWidth(), getHeight(), background);
    }

    private void drawGridLines(Canvas canvas) {
        //Define color for grid lines and draw the board.
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        // Draw the minor grid lines
        for (int i = 0; i < 9; i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height,
                    light);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height
                    + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(),
                    light);
            canvas.drawLine(i * width + 1, 0, i * width + 1,
                    getHeight(), hilite);
        }
        // Draw the major grid lines
        for (int i = 0; i < 9; i++) {
            if (i % 3 != 0)
                continue;
            canvas.drawLine(0, i * height, getWidth(), i * height,
                    dark);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height
                    + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
            canvas.drawLine(i * width + 1, 0, i * width + 1,
                    getHeight(), hilite);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;
        getRect(selX, selY, selRect);
        Log.d(LOG_TAG, "onSizeChanged: width " + width + ", height "
                + height);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * width), (int) (y * height), (int) (x
                * width + width), (int) (y * height + height));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(LOG_TAG, "PuzzleView::onTouch: event="
                + event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(LOG_TAG, "PuzzleView::onTouch: x: "
                        + (int)Math.round(event.getX())+ ", y: "+ (int) Math.round(event.getY())
                        + ", selRect: " + selRect);
                Log.i(LOG_TAG, "PuzzleView::onTouch: SelX: "
                        + selX+ ", Sely: "+ selY
                        + ", selRect: " + selRect);
                select(event.getX(), event.getY());
                game.showKeypadOrError(selX, selY, false);
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    // this select calculate where the touch happens (x, y) and translate into selX and selY
    private void select(float x, float y) {
        invalidate(selRect);
        int totalX = getWidth();  //get total width pixel of this display
        int totalY = getHeight(); // get total height pixel of this display
        float width = (9f * x)/totalX; // x is the width pixel position where the touch happens
        float height = (9f * y)/totalY;// y is the height pixel position where the touch happens
        Log.i(LOG_TAG, "Before PuzzleView::select-float: Selx=" + selX + ", SelY"
                + selY+ "SelRect: " + selRect);
        selX = (int) width;
        selY = (int) height;
        getRect(selX,selY,selRect);

        Log.i(LOG_TAG, "After PuzzleView::select-float: Selx=" + selX + ", SelY"
                + selY+ "SelRect: " + selRect);
        invalidate();
    }

    // This function is no use since the current android phone has no key pad..
    // only touch is happeneing.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(LOG_TAG, "PuzzleView::onKeyDown: keycode=" + keyCode + ", event="
                + event);
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;

            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break;
            case KeyEvent.KEYCODE_1:     setSelectedTile(1); break;
            case KeyEvent.KEYCODE_2:     setSelectedTile(2); break;
            case KeyEvent.KEYCODE_3:     setSelectedTile(3); break;
            case KeyEvent.KEYCODE_4:     setSelectedTile(4); break;
            case KeyEvent.KEYCODE_5:     setSelectedTile(5); break;
            case KeyEvent.KEYCODE_6:     setSelectedTile(6); break;
            case KeyEvent.KEYCODE_7:     setSelectedTile(7); break;
            case KeyEvent.KEYCODE_8:     setSelectedTile(8); break;
            case KeyEvent.KEYCODE_9:     setSelectedTile(9); break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                game.showKeypadOrError(selX, selY, true);
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void setSelectedTile(int tile) {
        if (game.setTileIfValid(selX, selY, tile)) {
            invalidate();// may change hints
        }
        else {
            // Number is not valid for this tile
            Log.i(LOG_TAG, "setSelectedTile: invalid: " + tile);

            startAnimation(AnimationUtils.loadAnimation(game,
                    R.anim.shake));
        }
    }

    private void select(int x, int y) {
        //1st invalidate tells android that the area covered by the old selection rectangle needs to be redrawn.
        //the 2nd invalidate call says that the new selection area needs to be redrawn too.
        invalidate(selRect);
        selX = Math.min(Math.max(x,0), 8);
        selY = Math.min(Math.max(y,0), 8);
        getRect(selX,selY,selRect);
        invalidate();
    }
}
