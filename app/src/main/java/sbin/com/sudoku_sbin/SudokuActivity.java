package sbin.com.sudoku_sbin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SudokuActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static String LOG_TAG = "SudokuAcitivity";

    private static int ABOUT_ACTIVITY_REQ_CODE  = 100;
    private static int SUDOKU_MENU_REQ_CODE     = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        initButtonView();
    }

    private void initButtonView() {
        View continueBtn = findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(this);
        View newBtn = findViewById(R.id.new_button);
        newBtn.setOnClickListener(this);
        View aboutBtn = findViewById(R.id.about_button);
        aboutBtn.setOnClickListener(this);
        View exitBtn = findViewById(R.id.exit_button);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        switch (item){
            case R.id.continue_button:
                break;
            case R.id.new_button:
                openNewGameDialog();
                break;
            case R.id.about_button:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void openNewGameDialog() {
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                    .setTitle(R.string.new_game_title)
                    .setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame(which);
                        }
                    }).show();

    }

    private void startGame(int level) {
        Log.i(LOG_TAG,"Start new Game clicked...");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.KEY_DIFFICULTY, level);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sudoku_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId){
            case R.id.sudoku_menu_setting:
                intent = new Intent(this, Prefs.class);
                if (intent == null) throw new AssertionError("SuDokuActivity::onOptionItemSelected");
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
