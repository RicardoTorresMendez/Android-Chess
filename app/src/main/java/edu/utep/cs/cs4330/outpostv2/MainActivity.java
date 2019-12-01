package edu.utep.cs.cs4330.outpostv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity{

    MenuInflater inflater;

    GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main );

        game = new GameView( this );
        game.setNumColumns(8);
        game.setNumRows(8);

        RelativeLayout board = findViewById( R.id.board );
        board.addView( game );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        inflater = getMenuInflater();
        inflater.inflate( R.menu.menu, menu );
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshList:
                game.clear();
                return true;
            case R.id.addItem:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
