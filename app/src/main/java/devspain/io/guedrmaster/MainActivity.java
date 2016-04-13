package devspain.io.guedrmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // Defino una constante para mostrar logs
    private static final String TAG = "MainActivity";
    private ImageView forecastImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargamos la interfaz (fichero XML)
        setContentView(R.layout.activity_main);

        // Mostramos nuestro primer log pasándole la constante
        Log.v(MainActivity.TAG, "Hola Amundio desde Guedr");

        // Accedemos a la ImageView a través de su id
        this.forecastImage = (ImageView) findViewById(R.id.forcast_image);

        // Asociamos controlador con vista a través del indentificador
        Button change2AmericanSystem = (Button) findViewById(R.id.change_to_american_btn);
        // Le decimos que pasas cuando se pulsa el botón
        // Para eso le pasamos una instancia de  alguién
        // que implemetna la interfaz OnClickActionListener

        change2AmericanSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al ser pulsado el botón se dispara este método de esta clase anónima
                changeToAmericanSystem(v);
            }
        });
    }

    public void changeToAmericanSystem(View view) {
        Log.v(MainActivity.TAG, "Se llamó a change2AmericanSystem");
        // Cambiamos la imagen a mostrar
        // Le decimos al 'id' de la ImageView -> 'forecastImage' cual va a ser su imagen
        forecastImage.setImageResource(R.drawable.offline_weather2);
    }

}

