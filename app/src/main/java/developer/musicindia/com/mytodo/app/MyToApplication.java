package developer.musicindia.com.mytodo.app;

import android.app.Application;
import developer.musicindia.com.mytodo.dagger.AppComponent;
import developer.musicindia.com.mytodo.dagger.AppModule;
import developer.musicindia.com.mytodo.dagger.DaggerAppComponent;

/**
 * Created by amitagarwal3 on 7/17/2017.
 */
public class MyToApplication extends Application{

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    protected AppComponent initDagger(MyToApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }
}
