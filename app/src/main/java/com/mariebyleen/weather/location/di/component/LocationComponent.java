package com.mariebyleen.weather.location.di.component;

import com.mariebyleen.weather.di.scope.PerActivity;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.view.LocationFragment;

import dagger.Component;

@Component(modules = LocationModule.class)
@PerActivity
public interface LocationComponent {

    void inject(LocationFragment locationFragment);
}
