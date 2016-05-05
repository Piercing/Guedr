package devspain.io.guedrmaster.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.fragment.CityListFragment;
import devspain.io.guedrmaster.fragment.CityPagerFragment;
import devspain.io.guedrmaster.model.City;

public class ForecastActivity extends AppCompatActivity implements CityListFragment.CityListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast);

        // Información sobre el dispositivo
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpWith = (int) (width / metrics.density);
        int dpHeight = (int) (height / metrics.density);
        int dpi = metrics.densityDpi;
        // Modelo del dispositivo
        String model = Build.MODEL;

        // Vamos a usasr la toolbar que hemos definido en @layout/toolbar_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.tooblar);

        // Le decimos a la actividad que qeremos usar esa vista toolbar como nuestar Toolbsar
        setSupportActionBar(toolbar);

        // AÑADO EL FRAGMENT DESPUÉS DE AÑADIR LA TOOLBAR, PARA QUE ÉSTE SE MUESTRE
        // CORRECTAMENTE DESPUÉS DE AÑADIR LA TOOLBAR, SINO ME DARÁ UN NULLPOINTEREXCEPTION

        // Todas las actividades tienen un método que se llaman 'getFragmentManager()'
        // Es un objeto que se encarga de gestionar el ciclo de vida de los Fragments
        // Pero también se encarga de poder incorporar fragments en mi aplicación
        // creando para ello una transacción, ya que puede que me vengan las cosas
        // en bloque, como dos fragments dentro de mi actividad, si quiero que al
        // pulsar el botón 'atras' de mi teléfono, si lo hago mediante transacciones
        // estos dos fragments que tengo dentro de mi actividad también desaparecerán
        // en bloque como en una transacción.
        FragmentManager fm = getFragmentManager();

        // Comprobamos primero que no tenemos añadido el fragment a nuestra jerarquía
        // Recordar que las actividades de Android se recrean ante varias situaciones
        // - Giro del dispositivo
        // - Falta de memoria
        // - Cuando sale el teclado
        // - Cuando cambia el tamaño de la interfaz por algún motivo (split view)
        // Para eso lo comporbamos preguntándole al FragmentManager a ver si ya existe en R.id.fragment_city_pager

        // Comprobación importante, para que al girar el dispositivo, no se quede ningún
        // fragement enganchado a la actividad, ya que al girar el dispositivo las actividades
        // se destruyen y se vuelven a crear, con lo que al llamar al método 'onCreate' de nuevo
        // pudiera ser que me mostrara también el fragment que se ha quedado enganchado, entonces
        // busco el fragment, y solo si es 'null' hago la transacción. Con esto comprobamos si ya
        // está añadido el 'Fragment'

        /**
         * PREGUNTAR A VER SI HAY HUECO PARA METER LOS DOS FRAGMENTS
         * CUANDO EL DISPOSITIVO LO GIRAMOS A PORTRAID (VERTICAL).
         */
        // Preguntamos primero a ver si hemos cargado la interfaz donde
        // tenemos un hueco para el 'City pager' para ver si hay hueco.
        if (findViewById(R.id.fragment_city_list) != null) {
            // Llegados aquí es que hay hueco, y habiendo hueco metemos el fragment
            if (fm.findFragmentById(R.id.fragment_city_list) == null) {
                // Como no existe, añado el fragment con una transacción a nuestra jerarquía de vistas
                fm.beginTransaction()
                        // añado el fragment que yo quiero, 'fragment_city_list',
                        // con una nueva instancia de 'fragment_city_list'
                        .add(R.id.fragment_city_list, new CityListFragment())
                        .commit(); // Mucho ojo, no olvidar de poner el commit
            }
        }

        /**
         * PREGUNTAR A VER SI HAY HUECO PARA METER LOS DOS FRAGMENTS
         * CUANDO EL DISPOSITIVO LO GIRAMOS A LANDSCAPE (HORIZONTAL)
         */

        // Preguntamos primero a ver si hemos cargado la interfaz donde
        // tenemos un hueco para el 'City pager' para ver si hay hueco.
        if (findViewById(R.id.fragment_city_pager) != null) {
            // Llegados aquí es que hay hueco, y habiendo hueco metemos el fragment
            if (fm.findFragmentById(R.id.fragment_city_pager) == null) {
                // Como no existe, añado el fragment con una transacción a nuestra jerarquía de vistas
                fm.beginTransaction()
                        // añado el fragment que yo quiero, 'CityPagerFragment.newInstance(0)'
                        // cargando la primera ciudad (0) , con una nueva instancia de 'fragment_city_pager'
                        .add(R.id.fragment_city_pager, CityPagerFragment.newInstance(0))
                        .commit(); // Mucho ojo, no olvidar de poner el commit
            }

        }
    }

    @Override
    public void onCitySelected(City city, int position) {
        // Aquí me entero de que una ciudad ha sido seleccionada en el CityListFragment
        // Tendré que mostara la ciudad en el CityPagerFragment
        Log.v("ForecastActivity", "Se ha seleccionado al ciudad: " + city + " número: " + position);

        // Vamos comprobar si tenemos un pager en nuestra interfaz
        // Necesito para ello el fragment
        FragmentManager fm = getFragmentManager();
        CityPagerFragment cityPagerFragment = (CityPagerFragment) fm.findFragmentById(R.id.fragment_city_pager);
        // Si cityPagerFragment no es null es que tenemos la referencia a él.
        if (cityPagerFragment != null) {
            // ESTA ES OTRA FORMA DE COMUNICAR UNA ACTIVIDAD CON UN FRAGMENT
            // Le diremos al fragment que muestre la ciudad en la 'position'
            //Llamo para eso al método que he creado en 'CityPagerFragment'
            cityPagerFragment.showCity(position);

        } else {

            // Si no tenemos referencia al pager arrancamos otra actividad
            // Para arrancar la actividad tenemos que crear un 'intent'
            // Le paso la clase, 'this' y la actividad que voy a arracancar
            Intent intent = new Intent(this, CityPagerActivity.class);

            // Le paso el índice de la ciudad que le hace falta
            intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);

            // La arranco, aquí siempre lanza una actividad, ya que cuando se  entera de
            // que el usuario ha pulsado una ciudad, siempre siempre lanzó una actividad
            startActivity(intent);
        }
    }
}


