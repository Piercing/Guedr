package devspain.io.guedrmaster;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastActivity extends AppCompatActivity {

    // Constante para exponer que cosas puede hacer, un requesCode.
    // Una flag,  que le pasamos a algunos métodos como referencia.
    private static final int REQUEST_UNIT = 1;
    // Clave para preferencias de usuario.
    private static final String PREFERENCE_UNITS = "units";
    private static final String TAG = "ForecastActivity";

    private TextView mMaxTemp;
    private TextView mMinTemp;
    private TextView mHumidity;
    private TextView mDescription;
    private ImageView mForecastImage;
    private boolean showCelsius;
    private Forecast mForecast;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargamos la interfaz (fichero XML)
        setContentView(R.layout.activity_forecast);

        // Cargamos los valores de la  última vez  guardados en sharedPreferences
        // Con esto la segunda vez que el usuario arranque la aplicación mostrará
        // las unidades que aparecerán serán las útlimas seleccionadas al cerrar.
        // Le paso la clave que he creado y el booleano que contenga showCelsius.
        showCelsius = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREFERENCE_UNITS, showCelsius);


        // Sincronizamos vista controlador
        mMaxTemp = (TextView) findViewById(R.id.max_temp);
        mMinTemp = (TextView) findViewById(R.id.min_temp);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mDescription = (TextView) findViewById(R.id.forecast_description);
        mForecastImage = (ImageView) findViewById(R.id.forecast_image);

        // Creo un objeto de mi modelo
        mForecast = new Forecast(35, 22, 25, "Hace calorcito, toca cañita fresquita", R.drawable.sun_cloud);
        // Llamo al método y le paso el objeto del modelo. Mostrará 'Farenheit' o 'Celsuius' según el valor
        // guardado en las preferencias de usuario, si showCelsius es false --> Frenheit y true --> Celsius
        setForecast(mForecast);

    }

    // Este dice como es el menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Coje un fichero 'xml' y lo recorre analizándolo  y
        // creando los objetos de las etiquetas que encuentre
        // poniéndolo en un menú que le indicamos como 2º parámetro
        getMenuInflater().inflate(R.menu.menu_forecast, menu);
        return true;
    }

    // Este dice qué pasa al pulsar una opción de menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Creo una variable para devolver lo que creó el padre
        // en caso de que lo que devuelva aquí no traiga nada.
        // Lo hago así porque primero se llama a super y como
        // tengo que devolver algo si me viene vacio, devuelvo
        // lo que tenía el padre.
        boolean superReturn = super.onOptionsItemSelected(item);
        // En el parámetro 'item' que recibe como parámetro viene el item que se seleccionó
        if (item.getItemId() == R.id.menu_show_settings) {
            // Lanzamos la actividad 'SettingsActivity'
            // Le digo que actividad la va a manejar 'this' y que clase lo maneja.
            // NOTA: Las actividades son hijas de una clase que se llama 'Context'
            // y muchos de los métodos necesitan saber cosas que tiene esta
            // clase, por eso me pide en muchas ocasiones ese context --> 'this',
            // y como la activity en la que estoy hereda de context, pues le paso
            // 'this' que se refiere a esta actividad en la que estoy.
            Intent intent = new Intent(this, SettingsActivity.class);

            // Este 'intent' igual que nos sirve para mandar datos de la 2ª pantalla
            // a la 1ª, también nos va a servir  para mandar datos de la 1ª a la 2ª.
            // Le pasamos datos a la pantalla de ajustes --> 'SettingsActivity'
            intent.putExtra(SettingsActivity.EXTRA_CURRENT_UNITS, showCelsius);

            // Le pedimos a Android que lance el intent explícito para mostrar la pantalla.
            // Nos pide como parámetro un 'requestCode' que lo creo como constante -> REQUEST_UNIT.
            /* Parameters:
            intent - The intent to start.
            requestCode - If >= 0, this code will be returned in onActivityResult() when the activity exits.
                    */
            startActivityForResult(intent, REQUEST_UNIT);
            return true;
        }
        return superReturn;
    }

    // Método que es llamado cuando se vuelve de una segunda actividad y que
    // he llamado con 'startActivityForResult'. Es como los delegados en iOS
    // Pero como puedo regresar de varias pantallas, tengo  que saber  a que
    // petición  es a la que yo me refería, es  decir, de que pantalla vengo.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Averiguamos de que pantalla estamos volviendo
        // Como el item de arriba lo he lanzado con 'startActivityForResult(intent, REQUEST_UNIT);'
        // para ir a la pantalla de settings, con el 'REQUEST_UNIT' puede averiguar de que pantalla vengo.
        // Si es igual, es que vengo de settings
        if (requestCode == REQUEST_UNIT) {
            // Estoy manejando la pantalla de ajustes
            // Miro a ver primero si ha cancelado, porque de ser así no tengo que hacer nada.
            if (resultCode == RESULT_OK) {
                // Guardamos el valor previo de showCelsius por si el usuario quiere deshacerlo.
                final boolean oldShowCelsius = showCelsius;

                // El usuario ha elegido algo, recibimos un parámetro 'data de tipo Intent', y
                // es el intent que me devuelve el que he creado en 'SettingsActivity' -> 'acceptSettings()'
                // y que me está devolviendo los datos del usuario, es decir, que ha hecho algo.
                // Le digo como primer parámetro que me de lo que contiene la constante que me inventé,
                // y sino trae nada le digo que me ponga por defecto las unidades en celsius.
                int optionSelected = data.getIntExtra("units", R.id.celsius_rb);
                // Ahora para saber que opción se ha seleccionado pregunto:
                if (optionSelected == R.id.celsius_rb) {
                    // Se ha seleccionado unidades celsius
                    Log.v(TAG, "El usuario ha seleccionado Celsius");
                    showCelsius = true;
                } else if (optionSelected == R.id.farenheit_rb) {
                    // Se ha seleccionado unidades Farenheit
                    Log.v(TAG, "El usuario ha seleccionado Farenheit");
                    showCelsius = false;
                }
/*
                // Persistimos las preferencias de usuario
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                // Entramos en modo de edidición de preferencias
                SharedPreferences.Editor editor = prefs.edit();
                // Guardamos los datos, pasamos la clave que hemos creado
                editor.putBoolean(PREFERENCE_UNITS, showCelsius);
                // Hacemos commit de los cambios.
                editor.commit();*/

                // Como se suele hacer para guardar las preferencias de usuario:
                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean(PREFERENCE_UNITS, showCelsius)
                        .commit();

                // Actualizamos la interfaz con las nuevas unidades.
                setForecast(mForecast);

                // Avisamos al usuario que los ajustes han cambiado.
                //Toast.makeText(this, "Preferencias actualizadas", Toast.LENGTH_LONG).show();

                // SnackBar, accede a la vista que contiene mi pantalla 'findViewById(android.R.id.content'
                Snackbar.make(findViewById(android.R.id.content), "Preferencias actualizadas", Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aquí podemos deshacer lo que el usuario haya hecho en los ajustes
                        // Como hemos guardado previamente el valor antiguo, se lo asignamos
                        // Restauramos la variable 'showCelsius'
                        showCelsius = oldShowCelsius;
                        // Guardamos el valor anterior en las preferencias
                        // Le paso 'ForecastActivity.this' porque sino no apunta a esta clase
                        // aputaría a la clase anónima que estamos instanciando 'new View.OnClickListener()'.
                        PreferenceManager.getDefaultSharedPreferences(ForecastActivity.this)
                                .edit()
                                .putBoolean(PREFERENCE_UNITS, showCelsius)
                                .commit();
                        // Actualizamos la interfaz
                        setForecast(mForecast);
                    }
                })
                        .show();
            }
        }
    }

    public void setForecast(Forecast forecast) {
        // Muestro en la interfaz mi modelo.
        float maxTemp = forecast.getMaxTemp();
        float minTemp = forecast.getMinTemp();

        if (!showCelsius) {
            maxTemp = toFarenheit(maxTemp);
            minTemp = toFarenheit(minTemp);
            mMaxTemp.setText(String.format(getString(R.string.max_temp_label), maxTemp) + "F");
            mMinTemp.setText(String.format(getString(R.string.min_temp_label), minTemp) + "F");
        } else {
            mMaxTemp.setText(String.format(getString(R.string.max_temp_label), maxTemp) + "C");
            mMinTemp.setText(String.format(getString(R.string.min_temp_label), minTemp) + "C");
        }
        mHumidity.setText(String.format(getString(R.string.humidity_label), forecast.getHumifity()) + "%");
        mDescription.setText(forecast.getDescription());
        mForecastImage.setImageResource(forecast.getIcon());
    }

    protected static float toFarenheit(float celsius) {
        return (celsius * 1.8f) + 32;
    }
}

