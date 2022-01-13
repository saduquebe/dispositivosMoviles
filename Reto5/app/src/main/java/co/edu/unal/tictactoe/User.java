package co.edu.unal.tictactoe;

public class User {

    public String username;
    public String Available;
    public String move;
    public String otherPlayer;

    public User(String username, String available, String move, String otherPlayer) {
        this.username = username;
        this.Available = available;
        this.move = move;
        this.otherPlayer = otherPlayer;
    }
}
