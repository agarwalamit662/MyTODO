package developer.musicindia.com.mytodo.dagger;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import developer.musicindia.com.mytodo.UI.*;

import javax.inject.Singleton;

/**
 * Created by amitagarwal3 on 7/16/2017.
 */
@Module
public class PresenterModule {

    @Provides
    @Singleton
    MainPresenter provideFoodzPresenter(Context context) {
        return new MainPresenterImpl(context);
    }

    @Provides
    @Singleton
    AddToDoPresenter provideToDoPresenter(Context context) {
        return new AddToDoPresenterImpl(context);
    }

    @Provides
    @Singleton
    EditToDoPresenter provideEditToDoPresenter(Context context) {
        return new EditToDoPresenterImpl(context);
    }

}
