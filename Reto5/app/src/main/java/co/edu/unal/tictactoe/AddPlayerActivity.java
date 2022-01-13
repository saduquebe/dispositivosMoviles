package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

public class AddPlayerActivity extends AppCompatActivity {

    Button CrearUsuario;
    EditText Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        CrearUsuario = findViewById(R.id.RegistrarId);
        Username = findViewById(R.id.Username);
        CrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });
    }

    private void switchActivities() {
        String username = Username.getText().toString();
        Intent lobbyIntent = new Intent(this, Lobby.class);
        lobbyIntent.putExtra("username", username);
        startActivity(lobbyIntent);
    }
}