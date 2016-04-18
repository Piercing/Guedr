package devspain.io.guedrmaster.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.fragment.CityPagerFragment;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast);

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
        // está añadido el 'cityPagerFragment'
        if (fm.findFragmentById(R.id.fragment_city_pager) == null) {
            // Como no existe, lo añadimos con una transacción a nuestra jerarquía de vistas
            fm.beginTransaction()
                    // añado el fragment que yo quiero ('new CityPagerFragment()') y donde lo meto('fragment_city_pager')
                    .add(R.id.fragment_city_pager, new CityPagerFragment())
                    .commit(); // Mucho ojo, no olvidar de poner el commit
        }

    }
}


