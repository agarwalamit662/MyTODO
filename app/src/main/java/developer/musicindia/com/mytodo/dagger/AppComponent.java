package developer.musicindia.com.mytodo.dagger;

import dagger.Component;
import developer.musicindia.com.mytodo.UI.AddToDoPresenterImpl;
import developer.musicindia.com.mytodo.UI.EditToDoPresenterImpl;
import developer.musicindia.com.mytodo.UI.MainPresenterImpl;
import developer.musicindia.com.mytodo.activities.AddTODOActivity;
import developer.musicindia.com.mytodo.activities.EditActivity;
import developer.musicindia.com.mytodo.activities.MainActivity;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.receivers.BootBroadcastReceiver;
import developer.musicindia.com.mytodo.services.RemindMyTODO;

import javax.inject.Singleton;


/**
 * Created by amitagarwal3 on 7/17/2017.
 */
@Singleton
@Component(modules = {AppModule.class,DbModule.class,PresenterModule.class})
public interface AppComponent {

    void inject(DTOProviderTODO target);

    void inject(MainActivity target);

    void inject(EditActivity target);

    void inject(AddTODOActivity target);

    void inject(BootBroadcastReceiver target);

    void inject(RemindMyTODO target);

    void inject(MainPresenterImpl target);

    void inject(AddToDoPresenterImpl target);
    void inject(EditToDoPresenterImpl target);
}
