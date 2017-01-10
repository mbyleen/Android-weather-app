package com.mariebyleen.weather.location.view_model;

import android.content.SharedPreferences;

import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocation;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;
import com.mariebyleen.weather.preferences.Preferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SearchLocationsTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    LocationViewContract view;

    private LocationViewModel viewModel;

    private SharedPreferences sharedPreferences;
    private Preferences preferences;

    @Before
    public void init() {
        sharedPreferences = new FakeSharedPreferences();
        preferences = new Preferences(sharedPreferences, null, null);
        viewModel = new LocationViewModel(view, null, null, preferences, null);
    }

    @Test
    public void savesCoordinatesFromModel() {
        SearchLocations model = createFakeModel("Test", "Test", "Test", "12.345", "12.345");
        viewModel.setModel(model);

        when(view.getSearchTextViewText()).thenReturn("Test, Test, Test");

        viewModel.saveSearchLocationCoordinates();

        assertEquals(12.345f, preferences.getLatitude());
        assertEquals(12.345f, preferences.getLongitude());
    }

    private SearchLocations createFakeModel(String name, String stateName, String countryName,
                                            String lat, String lon) {
        SearchLocations model = new SearchLocations();
        SearchLocation[] geoNames = new SearchLocation[1];
        SearchLocation sampleLocation = new SearchLocation();
        sampleLocation.setToponymName(name);
        sampleLocation.setAdminName1(stateName);
        sampleLocation.setCountryName(countryName);
        sampleLocation.setLat(lat);
        sampleLocation.setLng(lon);
        geoNames[0] = sampleLocation;
        model.setGeonames(geoNames);
        return model;
    }
}
