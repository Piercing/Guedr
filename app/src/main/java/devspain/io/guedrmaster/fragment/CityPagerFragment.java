package devspain.io.guedrmaster.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.model.Cities;
import devspain.io.guedrmaster.model.City;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityPagerFragment extends Fragment {


    public CityPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Obtengo una referencia a la raíz para sacar cosas como el ViewPager
        View root = inflater.inflate(R.layout.fragment_city_pager, container, false);

        // Accedemos al ViewPager de nuestra interfaz
        ViewPager pager = (ViewPager) root.findViewById(R.id.view_pager);

        // Lo que necesita un pager para saber que fragments tiene que mostrar,
        // es decir, las páginas a mostrar que son las distintas ciudades, necesita
        // una clase llamada 'adptador' que nos permite unir el modelo con la interfaz
        // pasandole nuestro modelo con nuestrar ciudades, y el 'pager' le va a ir
        // consultando que tengo que poner en cada pantalla, y el adaptador se lo va
        // a ir diciendo, pasándole un índice para saber en que pantalla se encuentra.

        // Le decimos al ViewPager quién es su adptaor, que le dará los fragment que debe dibujar
        // Le paso una instancia de la clase que he creado 'CityPagerAdapter' que será su adaptador.
        // Con esto ya tiene acceso a los métodos abstractos, para ir preguntándole la página y cuántas son.
        pager.setAdapter(new CityPagerAdapter(getFragmentManager()));


        return root;
    }

}

// Creo una clase abstracta que luego voy a pasar una instancia de ésta al pager como su adaptador
class CityPagerAdapter extends FragmentPagerAdapter {
    private Cities mCities;

    // Implementa el constructor de la clase padre por ser abstracta
    public CityPagerAdapter(FragmentManager fm) {
        super(fm);
        // Instancio mCities
        mCities = new Cities();
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
