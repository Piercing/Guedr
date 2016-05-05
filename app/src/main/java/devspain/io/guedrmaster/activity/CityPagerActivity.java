package devspain.io.guedrmaster.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.fragment.CityPagerFragment;


public class CityPagerActivity extends AppCompatActivity {

    // Necesitamos un argumento de entrada
    public static final String EXTRA_CITY_INDEX = "devspain.io.guedrmaster.activity.CityPagerActivity.EXTRA_CITY_INDEX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_pager);

        // Vamos a usar la toolbar que hemos definido en @layout/toolbar_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.tooblar);

        // Le decimos a la actividad que qeremos usar esa vista toolbar, como nuestra Toolbar
        setSupportActionBar(toolbar);
        // Añadiendo un flecha de 'Back' para retroceder
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Todas las actividades tienen un método que se llaman 'getFragmentManager()'
        FragmentManager fm = getFragmentManager();

        // Si no existe el fragment, es null
        if (fm.findFragmentById(R.id.fragment_city_pager) == null) {

            // Obtenemos la ciudad inicial que nos han pasado a esta actividad
            int initialCityIndex = getIntent().getIntExtra(EXTRA_CITY_INDEX, 0);

            // Como no existe, añado el fragment con una transacción a nuestra jerarquía de vistas
            fm.beginTransaction()
                    // Añado el fragment que yo quiero 'fragment_city_pager', e instancio un nuevo
                    // 'CityPagerFragment.newInstance', pasándole la posición de la ciudad inicial
                    // que recibo
                    .add(R.id.fragment_city_pager, CityPagerFragment.newInstance(initialCityIndex))
                    .commit(); // Mucho ojo, no olvidar de poner el commit
        }
    }

    /**
     * Cuando se pulse la flecha de 'back' nos van a llamar a este método
     * El 'item' que recibe va a ser la flecha de 'back'
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);

        // Detectamos que han pulsado la flecha mediante el 'id especial' que tiene
        if (item.getItemId() == android.R.id.home) {
            // Han pulsado la flecha de 'back' finalizamos la actividad
            finish();
            return true;
        }

        return superValue;
    }
}
