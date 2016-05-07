package devspain.io.guedrmaster.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.activity.DetailActivity;
import devspain.io.guedrmaster.activity.SettingsActivity;
import devspain.io.guedrmaster.adapter.ForecastRecyclerViewAdapter;
import devspain.io.guedrmaster.model.City;
import devspain.io.guedrmaster.model.Forecast;


public class ForecastFragment extends Fragment implements ForecastRecyclerViewAdapter.OnForecastClickListener {
    public static final String ARG_CITY = "city";
    public static final String PREFERENCE_UNITS = "units";

    private static final int REQUEST_UNITS = 1;
    private static final String TAG = "ForecastActivity";
    private static final int LOADING_VIEW_INDEX = 0;
    private static final int FORECAST_VIEW_INDEX = 1;


    // Obtener una referencia al ViewSwitcher
    private ViewSwitcher mViewSwitcher;
    // Obtener una referencia a la ProgressBar
    private ProgressBar mProgressBar;
    private RecyclerView mList;
    private boolean showCelsius;
    private City mCity;

    /**
     * Patrón para crear un método llamado newInstance, recibe como argumentos lo
     * que necesita un 'fragment', facilitando las cosas a la hora de instanciar
     * un fragment
     *
     * @param city
     * @return
     */
    public static ForecastFragment newInstance(City city) {
        // Creo un fragment
        ForecastFragment fragment = new ForecastFragment();
        // Para pasar al fragment el argumento 'cityName':

        // 1º creo un objeto bundle, una especie de diccionario
        Bundle argumets = new Bundle();
        // Como es un diccionario necesita una clave y un valor
        // La clave me la invento y le paso city como valor, la ciudad
        argumets.putSerializable(ForecastFragment.ARG_CITY, city);
        // Aquí es cuando le pasamos al fragment los argumentos que necesita
        // ya que el método 'setArgumets' espera recibir unos argumentos.
        fragment.setArguments(argumets);

        // Devuelvo el fragments
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Le digo que tengo un menú,
        // para que lo muestre la actividad
        setHasOptionsMenu(true);

        // NOTA: se puede hacer también en el método de arriba, 'newInstance'
        // Sacamos los argumentos de este fragment con 'getArgumets médodo de la clase Fragments de Android'
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_CITY);


            /*// CON LO ANTERIOR YA NO ME HACE FALTA ESTO
            // Con los argumentos que me pasan configuro la vista
            // Para sacar los argumentos de un fragment:
            Bundle arguments = getArguments();
            // Le digo la clave con la que la he guardado antes y la saco
            String cityName = arguments.getString(ARG_CITY);
            // Le digo que el texto de esa etiqueta es lo que me han pasado como argumento
            mCityName.setText(cityName);*/
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Cargamos la interfaz (fichero XML)
        View root = inflater.inflate(R.layout.fragment_forecast, container, false);

        // Mostramos un ejemplo de recurso integer
        Log.v(TAG, "Ejemplo de integer en recurso: " + getResources().getInteger(R.integer.piercing));

        // Asociamos vista con controlador
        mViewSwitcher = (ViewSwitcher) root.findViewById(R.id.view_switcher);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress);

        // Le decimos al ViewSwitcher cómo queremos las animaciones entre vistas
        // Animación de cuando va a entrar una nueva vista,
        // cogemos alguna de las que ya tiene Android definidas
        mViewSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        // Animación de cuando va a salir una nueva vista
        mViewSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);

        // Obtenmos el RecyclerView (que es como una lista pero más configurable)
        mList = (RecyclerView) root.findViewById(android.R.id.list);
        // Le decimos como queremos que se muestren cada uno de los elementos de este recyclerView
        // o utilizar uno de los que tiene Android, le decimos que se muestre como una tabla.
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // El GridLayoutManager permite mostrar los elementos como una tabla
        //mList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Le indicamos también como debe animarse, le decimos
        // que por defecto, lo podemos también personalizar
        mList.setItemAnimator(new DefaultItemAnimator());
        // Por útlimo, nos hace falta el ADAPTADOR, le paso una lista vacía porque aún no tengo datos aquí
        // Cuando los tenga le pasaré otro adaptador diciéndole que tiene datos nuevos y que se acutalice
        // Le paso el 'getActivity' porque los necesita el contexto y le digo que el OnClickListener soy yo.
        mList.setAdapter(new ForecastRecyclerViewAdapter(new LinkedList<Forecast>(), getActivity(), this));


        // Actualizamos la interfaz
        updateCityInfo();

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
                updateCityInfo();

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
                                updateCityInfo();
                            }
                        })
                        .show();
            }
        }
    }

    public void updateCityInfo() {

        // Comprobamos si tenemos datos o hay que bajárselos
        // si no tenemos datos los bajamos
        if (mCity.getForecast() == null) {
            downloadWeather();
        } else {
            // Le digo al Switcher que muestre la lista
            mViewSwitcher.setDisplayedChild(FORECAST_VIEW_INDEX);
            // Aquí tenemos datos, en vez de una lista vacía le paso
            //  lo que hay en la ciudad y los mostramos en la lista
            mList.setAdapter(new ForecastRecyclerViewAdapter(mCity.getForecast(), getActivity(), this));
        }
    }

    /**
     * Método para bajar de la red la información del tiempo
     */
    private void downloadWeather() {

        // Implementamos la clase abstracta para descargar datos en 2º plano, (lo suyo es hacerla aparte)
        AsyncTask<City, Integer, LinkedList<Forecast>> weatherDownloaderAndUpdate = new AsyncTask<City, Integer, LinkedList<Forecast>>() {

            // Guardo la ciudad
            private City mCity;

            // NORMALMENTE HAY QUE IMPLEMENTAR ESTOS TRES MÉTODOS

            // Este método se ejecuta en el hilo principal ojo!!, pero
            // si que tiene acceso a la interfaz (al hilo principal)
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Aquí preparamos la interfaz para la tarea larga. Estamos en el hilo principal.
                mViewSwitcher.setDisplayedChild(LOADING_VIEW_INDEX);
            }

            @Override
            // Puede recibir todas las City que queramos, de ahi City...params (a params la podemos llamar como queramos)
            protected LinkedList<Forecast> doInBackground(City... params) {
                // Aquí estamos en un hilo que NO es el principal, podemos tardar lo que queramos
                // La city que nos interesa es la primera
                City city = params[0];
                // Guardo la city que obtengo
                mCity = city;

                // 1º creo una URL
                URL url;
                // 2º creo un inputStream, clase que me permite recibir datos unos detrás de otros, un flujo
                InputStream input = null;

                try {
                    url = new URL(String.format(
                            "http://api.openweathermap.org/data/2.5/forecast/daily?" +
                                    "q=%s&appid=2547fa23ee52c05ff6f7ac92560a5c1d&units=metrics&lang=sp",
                            city.getName()));// %s ==> parametrización de la ciudad
                    // Creo una conexión
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    // Me conecto
                    con.connect();
                    // Longitud de la respuesta (para más info), nos puede servir por si la respuesta es muy larga
                    // poder hacer una barra de progreso en donde nos indique la carga de dichos datos hasta el total.
                    int responseLenght = con.getContentLength();
                    // Descargamos los datos de un Kb en un Kb
                    byte data[] = new byte[1024];
                    // Datos pra saber cuanto me he bajado
                    long currentBytes = 0;
                    int downloadedBytes;
                    input = con.getInputStream();
                    // Me creo una cadena que voy a ir construyendo cada
                    // vez más grande, los voy añadiendo a esta variable
                    StringBuilder sb = new StringBuilder();
                    // El 'read(data) me devuelve -1 cuando hay terminado,
                    // por tanto mientras no sea -1
                    while ((downloadedBytes = input.read(data)) != -1) {

                        // Añado al nuevo String que le paso una nueva cadena con los datos,
                        // comenzando en cero hasta todos los datos que se haya descargado
                        sb.append(new String(data, 0, downloadedBytes));
                        if (responseLenght > 0) {
                            // Añadimos los Bytes que llevamos descargados al total descargado
                            currentBytes += downloadedBytes;
                            // Aquí actulazaríamos nuestra barra de progreso con curretBytes
                            // Llamo a un método que si puede trabajar en 2º plano, ya que el
                            // 'onProgressUpdate' actualiza la interfaz, y como sabemos para
                            // eso tiene que realizarlo en 1º plano, con este obtemos los datos
                            // descargados en 2º plano
                            publishProgress((int) (currentBytes * 100) / responseLenght);
                        }
                    }

                    // Analizamos los datos para convertirlos de JSON a algo que podamos manejar en código
                    // Este objeto ya se encarga de parsear una cadena en algo que podamos manejar
                    // 'jsonRoot' contendrá todo el objeto JSON que descargarmos de OpenWeather
                    JSONObject jsonRoot = new JSONObject(sb.toString());
                    // Para acceder al objeto 'list' que nos viene en el 'jsonRoot' y que es un array
                    JSONArray days = jsonRoot.getJSONArray("list");

                    // Si vamos a guardar una lista,'LinkedList<Forecast>',
                    // tenemos que crearnos una lista donde guardar todos los dias
                    LinkedList<Forecast> forecasts = new LinkedList<>();
                    // Hacemos un bucle para repetir tantos días de predicción como nos del el JSON
                    for (int i = 0; i < days.length(); i++) {
                        // De el array 'days' sólo necesito el de hoy, que es el primero (0)
                        JSONObject currentDay = days.getJSONObject(i);// TENER EN CUENTA CONTROLAR LOS ERRORES

                        // Ahora tengo que acceder al 'min' y al 'max' del objeto
                        // 'temp' el cual está dentro de este primer objeto de 'list'
                        float max = (float) currentDay.getJSONObject("temp").getDouble("max");
                        float min = (float) currentDay.getJSONObject("temp").getDouble("min");
                        // Este como no es un objeto lo saco directamente, no está dentro de 'temp'
                        float humidity = (float) currentDay.getDouble("humidity");

                        // Para acceder a la 'description' primero tengo que pasar por el objeto 'weather' de mi objeto
                        // 'currentDay' (el currentDay es el que he creado yo más arriba y que contiene el primer objeto de
                        // 'list'), por tanto obtengo primero 'weather' y después 'description', lo mismo para el icono
                        String description = currentDay.getJSONArray("weather").getJSONObject(0).getString("description");
                        String iconString = currentDay.getJSONArray("weather").getJSONObject(0).getString("icon");

                        // Quitamos el último caracter del valor de 'icon: 10d' le quitamos la 'd' para evitar problemas
                        // Cojo desde el primer caracter mas lo que mida la cadena menos 2, l
                        iconString.substring(0, iconString.length() - 2);
                        // Ponemos un icono por defecto, el primero
                        int icon = R.drawable.ico_01;

                        switch (iconString) {
                            case "01":
                                icon = R.drawable.ico_01;
                                break;
                            case "02":
                                icon = R.drawable.ico_02;
                                break;
                            case "03":
                                icon = R.drawable.ico_03;
                                break;
                            case "04":
                                icon = R.drawable.ico_04;
                                break;
                            case "09":
                                icon = R.drawable.ico_09;
                                break;
                            case "10":
                                icon = R.drawable.ico_10;
                                break;
                            case "11":
                                icon = R.drawable.ico_11;
                                break;
                            case "13":
                                icon = R.drawable.ico_13;
                                break;
                            case "50":
                                icon = R.drawable.ico_50;
                                break;
                        }

                        // Creamos el objeto Forecast y lo guardamos en la lista
                        // Con esto ya tengo la lista de todas las predicciones del tiempo de una ciudad
                        forecasts.add(new Forecast(max, min, humidity, description, icon));

                    }

                    // Simulamos artificalmente un tiempo de descarga largo, lo dormimos 5sg
                    //Thread.sleep(5000);

                    // Lo devuelvo
                    return forecasts;

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Si hay cualquier error devolvemos null
                return null;
            }

            // Método para la barra de descarga
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                // Saco el primero de los elementos porque sólo me están pasando un número como progreso
                mProgressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(LinkedList<Forecast> forecast) {
                super.onPostExecute(forecast);
                if (forecast != null) {
                    // Aquí preparamos la interfaz con los datos que nos han dado en doInBackground
                    // Estamos en el hilo principal, después de ejecutar en segundo plano, actualizamos la city(modelo)
                    mCity.setForecast(forecast);
                    // Pedimos de nuevo que se actualice la interfaz
                    updateCityInfo();
                } else {
                    // Ha habido algún error se lo indicamos al usuario
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("No se pudo descargar la información del tiempo");
                    alertDialog.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Si pulsa en reintentar la descarga o la conexión volvemos a llamar al método:
                            downloadWeather();
                        }
                    });
                    alertDialog.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Esto normalmente no se hace así, se le avisa a la actividad y ella decide
                            getActivity().finish();
                        }
                    });
                    // Mostrar el diálogo
                    alertDialog.show();
                }
            }
        };
        // Ejecutamos la tarea para que empiece a bajarse en 2º plano los datos y también la actualiza
        // Le paso la ciudad de la que quiero que me descargue los datos.
        // Este método no va a bloquear la interfaz, ejecuta en segundo plano y devuelve el control a la siguiente línea
        weatherDownloaderAndUpdate.execute(mCity);
    }


    @Override
    public void onForecastClick(Forecast forecast, View view) {
        // Aquí me entero que se ha pulsasdo una de las tarjetas (CARDS)
        // Lanzo la DetailActivity, pero de una forma especial para indicar que hay un elemetno común
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        // Esto nos permite comprvar qué vesión de Android estamos ejecutando
        // y así evitar que nos casque si no existe el método que pongo abajo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Lanzo la actividad, pero pasándole unas opciones con un transición que hace que las dos
            // pantallas tengan algo en común y le decimos quién va a estar en común, la vista que me están pasando (v)
            // y el nombre de la transición para que lo encuentre en la parte destino
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(), // Contexto que necestia este método
                    view,// La vista común en origen
                    getString(R.string.transition_to_detail // El nombre de la vista en destino
                    )).toBundle());
        }else{
            startActivity(intent);
        }
    }
}
