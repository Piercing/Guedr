package devspain.io.guedrmaster.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.model.Cities;
import devspain.io.guedrmaster.model.City;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityPagerFragment extends Fragment {

    // Atributos
    private Cities mCities;
    private ViewPager mPager;

    public CityPagerFragment() {
        // Required empty public constructor
    }

    // Siempre que tengamos menún hay que implementar esta método, sino, no los vemos en el fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Le decimos que tiene menú
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Obtengo una referencia a la raíz,(root), para sacar cosas como el ViewPager
        View root = inflater.inflate(R.layout.fragment_city_pager, container, false);

        // Recupero el modelo para poder sacar la lista de ciudades
        mCities = new Cities();

        // Accedemos al ViewPager de nuestra interfaz
        mPager = (ViewPager) root.findViewById(R.id.view_pager);

        // Lo que necesita un pager para saber que fragments tiene que mostrar,
        // es decir, las páginas a mostrar que son las distintas ciudades, necesita
        // una clase llamada 'adptador' que nos permite unir el modelo con la interfaz
        // pasandole nuestro modelo con nuestrar ciudades, y el 'pager' le va a ir
        // consultando que tengo que poner en cada pantalla, y el adaptador se lo va
        // a ir diciendo, pasándole un índice para saber en que pantalla se encuentra.

        // Le decimos al ViewPager quién es su adaptador, que le dará los fragment que debe dibujar
        // Le paso una instancia de la clase que he creado 'CityPagerAdapter' que será su adaptador.
        // Con esto ya tiene acceso a los métodos abstractos, para ir preguntándole la página y cuántas son.
        mPager.setAdapter(new CityPagerAdapter(getFragmentManager(), mCities));

        // Me entero de cuándo el usuario cambia de página en el 'ViewPager', lo pongo a la escucha
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // Aquí nos enteramos de cuándo el usuario ha cambiado de página.
            @Override
            public void onPageSelected(int position) {

                // Llamo al método que he creado para actulizar el nombre de la ciudad
                // en la toolbar cada vez que cambie de página la toolbar a otra ciudad
                // Lo hago aquí en el 'onCrateView' que es cuando se incia el 'fragmet'
                updateCityInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Llamo a updateCityInfo(), para que al arrancar me muestre en la
        // toolbar la que esté en ese momento que me lo da el propio método
        updateCityInfo();

        // Devolvemos siempre el root
        return root;
    }

    public void updateCityInfo() {

        // Saco la posición en la que estoy en ese momento y la guardo
        int position = mPager.getCurrentItem();

        // Comprobamos primero si 'getActivity' es una clase de 'AppCompatActivity'
        if (getActivity() instanceof AppCompatActivity) {

            // Modificamos el título de la toolbar. Para eso necesitamos:

            // 1º) Acceder a la actividad que nos contine.
            // Pero la clase 'Activity' no tiene acceso a la toolbar, lo
            // tiene una clase hija de 'Activity' que es: 'AppCompatActivity'
            AppCompatActivity activity = (AppCompatActivity) getActivity();

            // 2º) Acceder, dentro de la actividad, a la toolbar (la actionBar)
            ActionBar actionBar = activity.getSupportActionBar();

            // 3º) Camiar el texto a la toolbar
            if (actionBar != null) {
                actionBar.setTitle(mCities.getCities().get(position).getName());
            }
        }
    }

    // Le damos menús a nuestro fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflamos 'citypager.xml', con esto tenemos las opciones de menú
        inflater.inflate(R.menu.citypager, menu);
    }

    // Qué hacer cuando el usuario pulsa cada uno de los botones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);

        // Comprobamos que opción de menú se ha pulsado
        if (item.getItemId() == R.id.previous) {
            // Retrocedemos una página, con éste método, le paso
            // el currentItem que hubiera en ese momento -1
            mPager.setCurrentItem(mPager.getCurrentItem() -1);
            // Depués de retroceder de página, actulizamos
            updateCityInfo();

            return true;

            //Si el 'itemID' es el de 'next' avanzamos
        } else if (item.getItemId() == R.id.next) {
            // Avanzamos una página, aquí le sumo 1
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

            return true;
        }

        // Sino devolvemos lo que tenía el padre de principio
        return superValue;
    }

    @Override
    // Este método se va a llamar justamente cada vez que se vayan
    // a refrescar los menús a ver si hay alguna opción nueva o no.
    // Lo hace automáticamente Android
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Compruebo primero si voy a poder utilizar el 'pager', ya que lo puedo llamar antes del 'onCreate'
        // ya que éste es el que me va a decir con el 'getCurrenteItem' en que posición, ya que si estoy en
        // la cero, desactivo el botón 'anterior' y si estoy en la última posición desactivo el botón anterior
        if (mPager != null){
            // Obtengo la referencia a 'siguiente' y 'anterior'
            MenuItem menuPrev = menu.findItem((R.id.previous));
            MenuItem menuNext = menu.findItem(R.id.next);

            // Ahora que tengo las referencias, lo que tengo que hacer es activarlos
            // o desactivarlos en función de las condiciones que he comentado más arriba
            // Si el item ciudad en la que me encuentro, es menor que el tamaño del array de ciudades menos 1
            // me devuelve true, sino false. Si es true, es que puedo seguir, sino bloqueo botón
            boolean nextEnable = mPager.getCurrentItem() < mCities.getCities().size() - 1;
            // Le paso el resultado de 'nextEnable', si es true, botón activado, sino desactivado
            menuNext.setEnabled(nextEnable);
            // Si es mayor de cero será cuando yo puedo retroceder en uno
            menuPrev.setEnabled(mPager.getCurrentItem() > 0);
        }

    }
}

// Creo una clase abstracta que luego voy a pasar una instancia de ésta al pager como su adaptador
class CityPagerAdapter extends FragmentPagerAdapter {
    private Cities mCities;

    // Implementa el constructor de la clase padre por ser abstractay además le paso las ciudades
    public CityPagerAdapter(FragmentManager fm, Cities cities) {
        super(fm);
        // Le paso las cities que recibo como parámetro y que me vienen del modelo Cities
        mCities = cities;
    }

    // Método que pregunta el pager a su adaptador que fragment tiene que ir en la posición 0
    // El nuestro ahora mismo va de 0 a 9
    @Override
    public android.app.Fragment getItem(int position) {
        // En la posición que recibo tengo que crear un nuevo ForecastFragment, una ventana nueva.
        // Saco del modelo la posición y la guardo como un objeto 'City'
        City cityName = mCities.getCities().get(position);

        // Instancio el fragment a mostar en el view pager, llamando al método
        // ' ForecastFragment.newInstance(cityName)' creado en 'ForecastFragment'
        // y que me devuelve un 'fragment' que es la ciudad que le paso y que es su posición
        ForecastFragment fragment = ForecastFragment.newInstance(cityName);

        // Devuelvo el fragment listo para se mostrado
        return fragment;
    }

    // Método el cuál el pager la va a preguntar cuántas páginas tiene que mostrar (pantallas)
    @Override
    public int getCount() {
        // Le digo que tiene tantas ciudades como tenga el arrayList
        return mCities.getCities().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        return mCities.getCities().get(position).getName();

    }
}
