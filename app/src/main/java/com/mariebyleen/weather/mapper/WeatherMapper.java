package com.mariebyleen.weather.mapper;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;

import javax.inject.Inject;

public class WeatherMapper {

    @Inject
    public WeatherMapper() {}

    public WeatherData map(CurrentConditionsResponse ccResponse,
                           ForecastResponse fResponse) {
        WeatherData weatherData = new WeatherData();
        mapCurrentConditionsData(weatherData, ccResponse);
        mapForecastData(weatherData, fResponse);
        return weatherData;
    }

    private void mapCurrentConditionsData(WeatherData weatherData,
                                          CurrentConditionsResponse ccResponse) {
        weatherData.setUpdateTime(ccResponse.getDt());
        weatherData.setSunriseTime(ccResponse.getSys().getSunrise());
        weatherData.setSunsetTime(ccResponse.getSys().getSunset());
        weatherData.setTemperature(ccResponse.getMain().getTemp());
        weatherData.setHumidity(ccResponse.getMain().getHumidity());
        weatherData.setWindSpeed(ccResponse.getWind().getSpeed());
        weatherData.setWindDirection(ccResponse.getWind().getDeg());
        weatherData.setIconResourceId(mapIcon(ccResponse.getWeather()[0].getIcon()));
        weatherData.setDescription(ccResponse.getWeather()[0].getDescription());
        weatherData.setCityName(ccResponse.getName());
    }

    private void mapForecastData(WeatherData weatherData, ForecastResponse fResponse) {
        int numDailyForecasts = fResponse.getList().length;

        double[] forecastMaxTemps = new double[numDailyForecasts];
        double[] forecastMinTemps = new double[numDailyForecasts];
        int[] forecastIconResourceIds = new int[numDailyForecasts];

        for (int i = 0; i < fResponse.getList().length; i++) {
            forecastMaxTemps[i] = fResponse.getList()[i].getTemp().getMax();
            forecastMinTemps[i] = fResponse.getList()[i].getTemp().getMin();
            forecastIconResourceIds[i] = mapIcon(fResponse.getList()[i].getWeather()[0].getIcon());
        }
        weatherData.setForecastMaxTemps(forecastMaxTemps);
        weatherData.setForecastMinTemps(forecastMinTemps);
        weatherData.setForecastIconResourceIds(forecastIconResourceIds);
    }

    private int mapIcon(String iconCode) {
        switch (iconCode) {
            case "01d":
                return R.drawable.clear_sky_sun;
            case "01n":
                return R.drawable.clear_sky_moon;
            case "02d":
                return R.drawable.few_clouds_sun;
            case "02n":
                return R.drawable.few_clouds_moon;
            case "03d":
            case "03n":
                return R.drawable.scattered_clouds;
            case "04d":
            case "04n":
                return R.drawable.broken_clouds;
            case "09d":
            case "09n":
                return R.drawable.shower_rain;
            case "10d":
                return R.drawable.rain_sun;
            case "10n":
                return R.drawable.rain_moon;
            case "11d":
            case "11n":
                return R.drawable.thunderstorm;
            case "13d":
            case "13n":
                return R.drawable.snow;
            case "50d":
            case "50n":
                return R.drawable.mist;
            default:
                return 0;
        }
    }
}
