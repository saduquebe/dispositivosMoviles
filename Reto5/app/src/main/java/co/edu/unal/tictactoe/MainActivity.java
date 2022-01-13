package co.edu.unal.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
    static boolean myTurn = false;
    String username = null;
    String otherPlayer = null;
    private boolean mGameOver;
    private SharedPreferences mPrefs;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(myTurn){
            System.out.println("***********\nMI TURNO\n***********");
        }
        else{
            System.out.println("***********\nTURNO DEL OTRO\n***********");
        }
        username = getIntent().getStringExtra("username");
        otherPlayer = getIntent().getStringExtra("otherPlayer");
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
        waitPartner();
        autoUpdate();
    }

    private void waitPartner() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference player = database.getReference("Players").child(username).child("Available");
        player.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String option = snapshot.getValue().toString();
                        if(option.equals("0")){
                            if(otherPlayer == null){
                                DatabaseReference other = database.getReference("Players").child(username).child("otherPlayer");
                                other.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        otherPlayer = task.getResult().getValue().toString();

                                    }
                                });
                            }
                            startNewGame();
                        }
                        if(option.equals("1")){
                            disableButtons();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("ERROR", "loadPost:onCancelled", error.toException());
                    }
                }
        );
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

        @SuppressLint("NonConstantResourceId")
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
            if (!mGameOver){
                if(myTurn){
                    setMove(TicTacToeGame.PLAYER_1, pos);
                    validateWinner();
                    mGameOver = true;
                }
            }

            return false;
        }
    };

    private void autoUpdate() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference player = database.getReference("Players").child(username).child("move");
        player.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().contains("waiting")) {
                    System.out.println("SNAPSHOT: "+ snapshot.getValue());
                    int move = Integer.parseInt(snapshot.getValue().toString());
                    if(myTurn){
                        System.out.println("***********\nMOVI YO\n***********");
                        setMove(TicTacToeGame.PLAYER_1, move);
                        myTurn = false;
                    }
                    else{
                        System.out.println("***********\nMOVIO EL OTRO\n***********");
                        setMove(TicTacToeGame.PLAYER_2, move);
                        validateWinner();
                        mGameOver = false;
                        myTurn = true;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "loadPost:onCancelled", error.toException());
            }
        });
    }
    void validateWinner(){
        int winner = mGame.checkForWinner();
        if (winner == 0) {
            if(myTurn){
                mInfoTextView.setText("It's your turn.");
            }
            else{
                mInfoTextView.setText("Waiting");
            }
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

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            String user = "";
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference players = database.getReference("Players");
            HashMap<String, Object> update = new HashMap<>();
            update.put("move",String.valueOf(location));
            user = username;
            players.child(user).updateChildren(update);
            mHumanMediaPlayer.start();
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }
    private void disableButtons(){
        mGameOver = true;
    }

}