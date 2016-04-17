package devspain.io.guedrmaster.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.activity.SettingsActivity;
import devspain.io.guedrmaster.model.Forecast;


public class ForecastFragment extends Fragment {
    private static final int REQUEST_UNITS = 1;
    private static final String TAG = "ForecastActivity";
    private static final String PREFERENCE_UNITS = "units";

    private TextView mMaxTemp;
    private TextView mMinTemp;
    private TextView mHumidity;
    private TextView mDescription;
    private ImageView mForecastImage;
    private boolean showCelsius;
    private Forecast mForecast;

    protected static float toFarenheit(float celsius) {
        return (celsius * 1.8f) + 32;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Le digo que tengo un menú,
        // para que lo muestre la actividad
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Cargamos la interfaz (fichero XML)
        View root = inflater.inflate(R.layout.fragment_forecast, container, false);

        // Mostramos un ejemplo de recurso integer
        Log.v(TAG, "Ejemplo de integer en recurso: " + getResources().getInteger(R.integer.piercing));

        // Cargamos los valores de la última vez (guardados en SharedPreferences)
        // Si es la primera vez, le paso true, para que los cargue en 'Celsius'
        showCelsius = PreferenceManager.getDefaultSharedPreferences(ForecastFragment.this.getActivity())
                .getBoolean(PREFERENCE_UNITS, true);

        // Asociamos vista con controlador
        mMaxTemp = (TextView) root.findViewById(R.id.max_temp);
        mMinTemp = (TextView) root.findViewById(R.id.min_temp);
        mHumidity = (TextView) root.findViewById(R.id.humidity);
        mDescription = (TextView) root.findViewById(R.id.forecast_description);
        mForecastImage = (ImageView) root.findViewById(R.id.forecast_image);

        // Creo mi modelo
        mForecast = new Forecast(30, 15, 25, "Hace calorcito, toca cañita fresquita", R.drawable.sun_cloud);

        setForecast(mForecast);

        return root;
    }

    // Este dice cómo es el menú
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forecast, menu);
    }

    // Este dice qué pasa al pulsar una opción de menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menu_show_settings) {
            // Lanzamos la actividad SettingsActivity
            Intent intent = new Intent(ForecastFragment.this.getActivity(), SettingsActivity.class);

            // Le pasamos datos a la pantalla de ajustes
            intent.putExtra(SettingsActivity.EXTRA_CURRENT_UNITS, showCelsius);

            // Le pedimos a Android que lance el intent explícito para mostrar la pantalla
            startActivityForResult(intent, REQUEST_UNITS);

            return true;
        }

        return superReturn;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UNITS) {
            // Estoy manejando el resultado de la pantalla de ajustes
            if (resultCode == Activity.RESULT_OK) {
                // Guardamos el valor previo de showCelsius por si el usuario quiere deshacer
                final boolean oldShowCelsius = showCelsius;

                // El usuario ha elegido algo
                int optionSelected = data.getIntExtra("units", R.id.celsius_rb);
                if (optionSelected == R.id.celsius_rb) {
                    // Se ha seleccionado unidades celsius
                    Log.v(TAG, "El usuario ha seleccionado Celsius");
                    showCelsius = true;
                } else if (optionSelected == R.id.farenheit_rb) {
                    // Se ha seleccionado unidades farenheit
                    Log.v(TAG, "El usuario ha seleccionado Farenheit");
                    showCelsius = false;
                }

                /*
                // Persistimos las preferencias
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                // Entramos en modo edición de preferencias
                SharedPreferences.Editor editor = prefs.edit();
                // Guardamos los datos
                editor.putBoolean(PREFERENCE_UNITS, showCelsius);
                // Hacemos commit de los cambios
                editor.commit();
                */
                PreferenceManager.getDefaultSharedPreferences(ForecastFragment.this.getActivity())
                        .edit()
                        .putBoolean(PREFERENCE_UNITS, showCelsius)
                        .commit();


                // Actualizamos la interfaz con las nuevas unidades
                setForecast(mForecast);

                // Avisamos al usuario de que los ajustes han cambiado
                //Toast.makeText(this, "Preferencias actualizadas", Toast.LENGTH_LONG).show();
                Snackbar.make(getView(), R.string.updated_preferences, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Restauramos la variable showCelsius
                                showCelsius = oldShowCelsius;

                                // Guardamos el valor anterior en las preferencia
                                    PreferenceManager.getDefaultSharedPreferences(ForecastFragment.this.getActivity())
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
        // Muestro en la interfaz mi modelo
        float maxTemp = forecast.getMaxTemp();
        float minTemp = forecast.getMinTemp();

        if (!showCelsius) {
            maxTemp = toFarenheit(maxTemp);
            minTemp = toFarenheit(minTemp);
        }

        mMaxTemp.setText(String.format(getString(R.string.max_temp_label), maxTemp));
        mMinTemp.setText(String.format(getString(R.string.min_temp_label), minTemp));
        mHumidity.setText(String.format(getString(R.string.humidity_label), forecast.getHumifity()));
        mDescription.setText(forecast.getDescription());
        mForecastImage.setImageResource(forecast.getIcon());
    }
}
