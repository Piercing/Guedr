package devspain.io.guedrmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        Button change2EuropeanSystem = (Button) findViewById(R.id.change_to_european_btn);

        // Le decimos que pasa cuando se pulsa el botón, le paso
        // la propia vista que ya implemta --->  onClickListener.
        // Para eso le paso una instancia de alguien que implementa la inrefaz OnClisListener
        change2AmericanSystem.setOnClickListener(this);

        // Hacemos lo propio con el botón 'europeo'
        change2EuropeanSystem.setOnClickListener(this);
    }

    public void changeToAmericanSystem(View view) {
        Log.v(MainActivity.TAG, "Se llamó a change2AmericanSystem");
        // Cambiamos la imagen a mostrar
        // Le decimos al 'id' de la ImageView -> 'forecastImage' cual va a ser su imagen
        forecastImage.setImageResource(R.drawable.offline_weather);
    }

    public void changeToEuropeanSystem(View view) {
        Log.v(MainActivity.TAG, "Se llamó a change2EuropeanSystem");
        // Cambiamos la imagen a mostrar
        // Le decimos al 'id' de la ImageView -> 'forecastImage' cual va a ser su imagen
        forecastImage.setImageResource(R.drawable.offline_weather2);
    }

    @Override
    public void onClick(View v) {

        // Compruebo el id del botón que llama
        switch (v.getId()) {
            // Se ha pulsado el botón americano
            case R.id.change_to_american_btn:
                changeToAmericanSystem(v);
                break;
            case R.id.change_to_european_btn:
                // Se ha pulsado el europeo
                changeToEuropeanSystem(v);
                break;
        }
    }
}

