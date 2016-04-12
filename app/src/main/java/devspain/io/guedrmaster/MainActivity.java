package devspain.io.guedrmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Defino una constante para mostrar logs
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Mostramos nuestro primer log pasándole la constante
        Log.v(MainActivity.TAG, "Hola Amundio desde Guedr");
    }
}
