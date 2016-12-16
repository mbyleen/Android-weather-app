package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.model.WeatherData;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";
    private static final double KELVIN_TO_CELSIUS = 273.15;
    private String weatherDataTag;
    private String unitsOfMeasurementTag;

    private SharedPreferences preferences;
    private Gson gson;
    private Resources resources;
    private WeatherDataService service;

    private WeatherData weatherData;
    private Locale locale;

    private boolean useFahrenheitState;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      Gson gson,
                                      Resources resources,
                                      WeatherDataService service) {
        this.preferences = preferences;
        this.gson = gson;
        this.resources = resources;
        this.service = service;
        locale = Locale.getDefault();
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {
        referenceKeys();

        if (weatherData == null) {
            weatherData = getSavedWeatherData();
            useFahrenheitState = unitsPrefSetToFahrenheit();
        }

        service.manageUpdateJobs();
    }

    private void referenceKeys() {
        weatherDataTag = resources.getString(R.string.preference_weather_data_key);
        unitsOfMeasurementTag = resources.getString(R.string.preference_units_of_measurement_key);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(weatherDataTag)) {
            weatherData = getSavedWeatherData();
            notifyChange();
        }
        if (s.equals(unitsOfMeasurementTag)) {
            useFahrenheitState = unitsPrefSetToFahrenheit();
            notifyChange();
        }
    }

    private WeatherData getSavedWeatherData() {
        String weatherJson = preferences.getString(weatherDataTag, "");
        if (weatherJson.equals(""))
            return new WeatherData();
        return gson.fromJson(weatherJson,
                WeatherData.class);
    }

    @Bindable
    public String getTemperature() {
        double temp = weatherData.getTemperature();
        double convertedTemp;
        if (unitsPrefSetToFahrenheit())
            convertedTemp = getTemperatureInFahrenheit(temp);
        else
            convertedTemp = temp - KELVIN_TO_CELSIUS;
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedTemp)));
    }

    private boolean unitsPrefSetToFahrenheit() {
        return ((getUnitsOfMeasurementPreferenceCode()).equals("1"));
    }

    private String getUnitsOfMeasurementPreferenceCode() {
        return preferences.getString(unitsOfMeasurementTag, "");
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return (temperature - KELVIN_TO_CELSIUS)*1.8000 + 32.00;
    }

    @Bindable
    public boolean getUseFahrenheit() {
        return useFahrenheitState;
    }

    @Bindable
    public String getHumidity() {
        double humidity = Math.round(weatherData.getHumidity()) / 100.0;
        return NumberFormat.getPercentInstance().format(humidity);
    }

    @Bindable
    public String getWindSpeed() {
        double windspeed = weatherData.getWindSpeed();
        return null;
    }

    @Bindable
    public String getUpdateTime() {
        return formatTimeFromEpoch(weatherData.getUpdateTime());
    }

    @Bindable
    public String getSunriseTime() {
        return formatTimeFromEpoch(weatherData.getSunriseTime());
    }

    @Bindable
    public String getSunsetTime() {
        return formatTimeFromEpoch(weatherData.getSunsetTime());
    }

    private String formatTimeFromEpoch(long unixTime) {
        DateFormat format = new SimpleDateFormat("h:mm a", locale);
        Date updateTime = new Date(unixTime*1000);
        return format.format(updateTime);
    }

    @Bindable
    public boolean getIsDay() {
        long sunrise = weatherData.getSunriseTime();
        long sunset = weatherData.getSunsetTime();
        long updateTime = weatherData.getUpdateTime();
        return (sunrise < updateTime && updateTime < sunset);
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
