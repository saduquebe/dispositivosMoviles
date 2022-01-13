package co.edu.unal.tictactoe;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Random;

public class TicTacToeGame {

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    public static final int BOARD_SIZE = 9;
    public static final char PLAYER_1 = 'X';
    public static final char PLAYER_2 = 'O';
    public static final char OPEN_SPOT = ' ';

    private Random mRand;

    public char[] getBoardState() {
        return mBoard;
    }
    public void setBoardState(char[] mBoard){
        this.mBoard = mBoard;
    }

    public enum DifficultyLevel {Easy, Harder, Expert};
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    public TicTacToeGame() {

        // Seed the random number generator
        mRand = new Random();
    }

    public DifficultyLevel getmDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setmDifficultyLevel(DifficultyLevel mDifficultyLevel) {
        this.mDifficultyLevel = mDifficultyLevel;
    }

    public int checkForWinner(){
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == PLAYER_1 &&
                    mBoard[i+1] == PLAYER_1 &&
                    mBoard[i+2]== PLAYER_1)
                return 2;
            if (mBoard[i] == PLAYER_2 &&
                    mBoard[i+1]== PLAYER_2 &&
                    mBoard[i+2] == PLAYER_2)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == PLAYER_1 &&
                    mBoard[i+3] == PLAYER_1 &&
                    mBoard[i+6]== PLAYER_1)
                return 2;
            if (mBoard[i] == PLAYER_2 &&
                    mBoard[i+3] == PLAYER_2 &&
                    mBoard[i+6]== PLAYER_2)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == PLAYER_1 &&
                mBoard[4] == PLAYER_1 &&
                mBoard[8] == PLAYER_1) ||
                (mBoard[2] == PLAYER_1 &&
                        mBoard[4] == PLAYER_1 &&
                        mBoard[6] == PLAYER_1))
            return 2;
        if ((mBoard[0] == PLAYER_2 &&
                mBoard[4] == PLAYER_2 &&
                mBoard[8] == PLAYER_2) ||
                (mBoard[2] == PLAYER_2 &&
                        mBoard[4] == PLAYER_2 &&
                        mBoard[6] == PLAYER_2))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != PLAYER_1 && mBoard[i] != PLAYER_2)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }
    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(){
        Arrays.fill(mBoard, OPEN_SPOT);
    }

    /** Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public boolean setMove(char player, int location){
        if (mBoard[location] == OPEN_SPOT){
            mBoard[location] = player;
            return true;
        }
        return false;
    }

    public char getBoardOccupant(int i ){
        return mBoard[i];
    }

    public static void main(String[] args) {
        new TicTacToeGame();
    }
}