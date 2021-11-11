package co.edu.unal.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private TicTacToeGame mGame = new TicTacToeGame();
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private ImageButton mNewGameButton;
    private ImageButton mDifficultyButton;
    private ImageButton mQuitGame;
    private TextView mHumanCount;
    private TextView mTieCount;
    private TextView mAndroidCOunt;
    int humanCount = 0;
    int tieCount = 0;
    int androidCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mDifficultyButton = (ImageButton) findViewById(R.id.level);
        mDifficultyButton.setOnClickListener(new ButtonOptionsOnClickListener());
        mNewGameButton = (ImageButton) findViewById(R.id.new_game);
        mNewGameButton.setOnClickListener(new ButtonOptionsOnClickListener());
        mQuitGame = (ImageButton) findViewById(R.id.quit_game);
        mQuitGame.setOnClickListener(new ButtonOptionsOnClickListener());
        mHumanCount = (TextView) findViewById(R.id.human_wins);
        mTieCount = (TextView) findViewById(R.id.tie_match);
        mAndroidCOunt = (TextView) findViewById(R.id.android_wins);

        startNewGame();

    }

    @SuppressLint("SetTextI18n")
    private void startNewGame(){
        mGame.clearBoard();
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
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
                                    mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                }
                                else if (item == 1){
                                    mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                }
                                else if (item == 2){
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

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View v) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
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
                    mAndroidCOunt.setText("Android: "+ androidCount);
                    mInfoTextView.setText("Android won!");
                    disableButtons();
                }
            }
        }
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }
    private void disableButtons(){
        for (int i = 0; i < mBoardButtons.length; i++) {
            if(mBoardButtons[i].isEnabled()){
                mBoardButtons[i].setEnabled(false);
            }
        }
    }
}