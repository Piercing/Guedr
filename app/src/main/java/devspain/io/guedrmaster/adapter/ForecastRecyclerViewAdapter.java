package devspain.io.guedrmaster.adapter;

/**
 * Created by macbookpro on 7/5/16.
 */

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.fragment.ForecastFragment;
import devspain.io.guedrmaster.model.Forecast;

/**
 * Clase controladora del RecyclerView
 * donde definimos qué pasa con la vista
 * que tenmos que mostrar.
 * Esta sabe cuantos elementos hay y
 * cómo manejarla internamente
 */
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

    // Guardo aquí la lista a mostrar
    private LinkedList<Forecast> mForecasts;
    private Context mContext;

    // Necesitamos un constructor que nos pase los elementos a mostrar, la lista
    public ForecastRecyclerViewAdapter(LinkedList<Forecast> forecasts, Context context) {
        super();
        mForecasts = forecasts;
        mContext = context;
    }

    @Override
    public ForecastRecyclerViewAdapter.ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Aquí me creo la interfaz donde voy a mostrar cada uno de
        // los elementos, tenemos que crearnos el archivo XML para ello
        // Creo la 'fila', no tiene porque ser siempre una fila, y se
        // la asigno al 'ViewHolder'
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_forecast, parent, false);
        // Le pasamos la vista ya que sabe cómo pintarla
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {

        // Aquí es donde han enganchado un 'ViewHolder' a mi adaptador
        // Es cuando ya va a mostrar las celdas en la pantalla, por tanto
        // necesita darle valores y mostrarlos en la pantalla.
        holder.bindForecast(mForecasts.get(position), mContext);
    }

    @Override
    public int getItemCount() {

        // Devuelvo tantos elementos como haya guardado la lista
        return mForecasts.size();
    }


    /**
     * Clase que va a representar cada una de las filas de nuestro adaptador
     */
    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView mMaxTemp;
        private TextView mMinTemp;
        private TextView mHumidity;
        private TextView mDescription;
        //private TextView mCityName;
        private ImageView mForecastImage;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            mMaxTemp = (TextView) itemView.findViewById(R.id.max_temp);
            mMinTemp = (TextView) itemView.findViewById(R.id.min_temp);
            mHumidity = (TextView) itemView.findViewById(R.id.humidity);
            mDescription = (TextView) itemView.findViewById(R.id.forecast_description);
            mForecastImage = (ImageView) itemView.findViewById(R.id.forecast_image);
        }



        // Método al que le puedo pasar el forecast que tiene que mostrar en la interfaz
        public void bindForecast(Forecast forecast, Context context) {

            // Muestro en la interfaz mi modelo
            float maxTemp = forecast.getMaxTemp();
            float minTemp = forecast.getMinTemp();

            // Cargamos los valores de la última vez (guardados en SharedPreferences)
            // Si es la primera vez, le paso true, para que los cargue en 'Celsius'
            boolean showCelsius = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(ForecastFragment.PREFERENCE_UNITS, true);

            if (!showCelsius) {
                maxTemp = toFarenheit(maxTemp);
                minTemp = toFarenheit(minTemp);
            }

            mMaxTemp.setText(String.format(context.getString(R.string.max_temp_label), maxTemp));
            mMinTemp.setText(String.format(context.getString(R.string.min_temp_label), minTemp));
            mHumidity.setText(String.format(context.getString(R.string.humidity_label), forecast.getHumifity()));
            mDescription.setText(forecast.getDescription());
            mForecastImage.setImageResource(forecast.getIcon());

        }
        protected float toFarenheit(float celsius) {
            return (celsius * 1.8f) + 32;
        }
    }
}