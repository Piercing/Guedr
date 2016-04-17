package devspain.io.guedrmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import devspain.io.guedrmaster.R;

public class SettingsActivity extends AppCompatActivity {
    // Punto de entrada, argunmetos que necesita 'SettingsActivity' para poder saber datos de 'ForecasActivity'.
    // Viene de 'ForecastActivity' ----->  'intent.putExtra(SettingsActivity.EXTRA_CURRENT_UNITS, showCelsius);'
    // Mediante el 'intent.putExtra' 'SettingsActivity' nos está mandando el valor que tiene ---->'showCelsius'
    public static final String EXTRA_CURRENT_UNITS = "io.devspain.guedr.SettingsActivity.EXTRA_CURRENT_UNITS";

    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Le indico cuál es la toolbar que voy a utilizar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tooblar);
        setSupportActionBar(toolbar);

        // Accedo a las vistas
        mRadioGroup = (RadioGroup) findViewById(R.id.units_rg);

        // Configuramos las acciones de los botones
        findViewById(R.id.accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // LLamo al método que creé cuando se aceptan los settings
                acceptSettings();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSettings();
            }
        });

        // Indicamos  qué radioButton debe de  estar seleccionado inicialmente.
        // Para sacar esta  información de qué  botón está seleccionado, todas
        // las actividades tienen un método que se  llama 'getIntem()' que  es
        // el método que ha generado la aparición de esta actividad, por tanto,
        // aquí es donde tenemos el EXTRA_CURRENT_UNITS. Con esto sé qué valor
        // me ha pasado la pantalla anterior 'SettingsActivity' mediante ---->
        // 'intent.putExtra(SettingsActivity.EXTRA_CURRENT_UNITS, showCelsius);'
        // 'putExtra' me envía datos desde 'ForecastActivity' al dato de entra
        // 'EXTRA_CURRENT_UNITS' y los recogo con 'getIntent().getBooleanExtra'.
        // Con esto sé si -->'showCelsius' es true o false, indicando que botón
        // debe quedarse seleccionado una vez q salgamos d la pantalla Settings
        boolean showCelsius = getIntent().getBooleanExtra(EXTRA_CURRENT_UNITS, true);

        if (showCelsius) {
            RadioButton celsiusRadio = (RadioButton) findViewById(R.id.celsius_rb);
            celsiusRadio.setChecked(true);
        } else {
            RadioButton farenheitRadio = (RadioButton) findViewById(R.id.farenheit_rb);
            farenheitRadio.setChecked(true);
        }

    }

    private void cancelSettings() {
        setResult(RESULT_CANCELED);
        // ¡¡¡¡¡ OJO, NO OLVIDAR PONERLO !!!!!
        // Solicito que la actividad finalice de forma civilizada.
        finish();// Como hacer pop en iOS
    }

    /**
     * Devolviendo datos de la pantalla setting a la principal
     */
    private void acceptSettings() {
        // Creo un intent para comunicar las dos actividades
        Intent returnIntent = new Intent();
        // Le devuelvo los datos con el botón seleccionado, me invento la clave
        // y le paso el entero con el item que se ha seleccionado.
        returnIntent.putExtra("units", mRadioGroup.getCheckedRadioButtonId());
        // Devuelvo resultado OK y el entero del item
        setResult(RESULT_OK, returnIntent);
        finish();// ¡¡¡¡¡ OJO, NO OLVIDAR PONERLO !!!!!
    }
}
