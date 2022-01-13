package co.edu.unal.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lobby extends AppCompatActivity {

    ListView salas;
    ImageButton newGameLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference players = database.getReference("Players");
        String username = getIntent().getStringExtra("username");
        Intent switchActivityIntent = new Intent(Lobby.this, MainActivity.class);
        setContentView(R.layout.activity_lobby);
        salas = findViewById(R.id.salasDeJuego);
        newGameLobby = findViewById(R.id.newGameLobby);
        getPartidas(new getGames() {
            @Override
            public void setAvailableGames(List<String> result) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Lobby.this,android.R.layout.simple_list_item_1,result);
                salas.setAdapter(adapter);
            }
        });
        salas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String otherPlayer = salas.getItemAtPosition(position).toString();
                switchActivityIntent.putExtra("otherPlayer", username);
                switchActivityIntent.putExtra("username", otherPlayer);
                HashMap<String, Object> update = new HashMap<>();
                update.put("Available","0");
                update.put("otherPlayer", username);
                players.child(otherPlayer).updateChildren(update);
                startActivity(switchActivityIntent);
            }
        });
        newGameLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.child(username).setValue(new User(username,"1", "waiting", ""));
                switchActivityIntent.putExtra("username", username);
                MainActivity.myTurn = true;
                startActivity(switchActivityIntent);
            }
        });
    }

    private void getPartidas(getGames getGames) {
        List<String> availableGames = new ArrayList<String>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference players = database.getReference("Players");
        players.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Iterable<DataSnapshot> players = task.getResult().getChildren();
                for(DataSnapshot player : players){
                    System.out.println(player.getValue());
                    if(player.child("Available").getValue().toString().contains("1")){
                        availableGames.add(player.getKey());
                    }
                }
                getGames.setAvailableGames(availableGames);
            }
        });
    }
}

