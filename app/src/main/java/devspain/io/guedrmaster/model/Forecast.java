package devspain.io.guedrmaster.model;

import java.io.Serializable;

/**
 * Created by macbookpro on 13/4/16.
 */
public class Forecast implements Serializable {

    private float mMaxTemp;
    private float mMinTemp;
    private float mHumifity;
    private String mDescription;
    // guardo la referencia a su nombre que es un número
    private int mIcon;

    public Forecast(float maxTemp, float minTemp, float humifity, String description, int icon) {
        mMaxTemp = maxTemp;
        mMinTemp = minTemp;
        mHumifity = humifity;
        mDescription = description;
        mIcon = icon;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public float getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        mMaxTemp = maxTemp;
    }

    public float getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(float minTemp) {
        mMinTemp = minTemp;
    }

    public float getHumifity() {
        return mHumifity;
    }

    public void setHumifity(float humifity) {
        mHumifity = humifity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
