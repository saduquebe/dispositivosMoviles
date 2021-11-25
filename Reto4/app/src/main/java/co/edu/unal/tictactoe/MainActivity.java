package co.edu.unal.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    private static final int DIALOG_QUIT_ID = 1;
    // Characters used to represent the human, computer, and open spots
    private final TicTacToeGame mGame = new TicTacToeGame();
    private BoardView mBoardView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    private TextView mInfoTextView;
    private TextView mHumanCount;
    private TextView mTieCount;
    private TextView mAndroidCount;
    int humanCount = 0;
    int tieCount = 0;
    int androidCount = 0;
    private boolean mGameOver;
    private SharedPreferences mPrefs;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        loadOptionButtons();
        mGameOver = false;
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanCount = (TextView) findViewById(R.id.human_wins);
        mTieCount = (TextView) findViewById(R.id.tie_match);
        mAndroidCount = (TextView) findViewById(R.id.android_wins);
        startNewGame();
    }
    @SuppressLint("SetTextI18n")
    private void displayScores() {
        mHumanCount.setText("Human: " + humanCount);
        mTieCount.setText("Tie: " + tieCount);
        mAndroidCount.setText("Human: " + androidCount);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.x);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.o);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    private void loadOptionButtons(){
        ImageButton mDifficultyButton = (ImageButton) findViewById(R.id.level);
        mDifficultyButton.setOnClickListener(new ButtonOptionsOnClickListener());
        ImageButton mNewGameButton = (ImageButton) findViewById(R.id.new_game);
        mNewGameButton.setOnClickListener(new ButtonOptionsOnClickListener());
        ImageButton mQuitGame = (ImageButton) findViewById(R.id.quit_game);
        mQuitGame.setOnClickListener(new ButtonOptionsOnClickListener());
    }

    @SuppressLint("SetTextI18n")
    private void startNewGame(){
        mGameOver = false;
        mGame.clearBoard();
        // Reset all buttons
        mBoardView.invalidate();
        mInfoTextView.setText("You go first.");
    }
    private class ButtonOptionsOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.new_game:
                    startNewGame();
                    break;
                case R.id.level:
                    showDialog(DIALOG_DIFFICULTY_ID);
                    break;
                case R.id.quit_game:
                    showDialog(DIALOG_QUIT_ID);
                    break;
            }
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
                int selected = 0;
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if(item == 0){
                                    System.out.println("level 0 ");
                                    mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                }
                                else if (item == 1){
                                    System.out.println("level 1");
                                    mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                }
                                else if (item == 2){
                                    System.out.println("level 2 ");
                                    mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }
                                dialog.dismiss(); // Close dialog
                                Toast.makeText(getApplicationContext(), levels[item],

                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)){
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0) {
                    mInfoTextView.setText("It's your turn.");
                }
                else if (winner == 1) {
                    mInfoTextView.setText("It's a tie!");
                    tieCount++;
                    mTieCount.setText("Tie: " + tieCount);
                    disableButtons();
                }
                else if (winner == 2) {
                    humanCount++;
                    mHumanCount.setText("Human: "+ humanCount);
                    mInfoTextView.setText("You won!");
                    disableButtons();
                }
                else{
                    androidCount++;
                    mAndroidCount.setText("Android: "+ androidCount);
                    mInfoTextView.setText("Android won!");
                    disableButtons();
                }
            }

            return false;
        }
    };

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            if (player == TicTacToeGame.HUMAN_PLAYER){
                mHumanMediaPlayer.start();
            }
            if (player == TicTacToeGame.COMPUTER_PLAYER){
                mHumanMediaPlayer.start();
            }
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }
    private void disableButtons(){
        mGameOver = !mGameOver;
    }
}