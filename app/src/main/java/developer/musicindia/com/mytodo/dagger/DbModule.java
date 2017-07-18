package developer.musicindia.com.mytodo.dagger;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;

import javax.inject.Singleton;

/**
 * Created by amitagarwal3 on 7/16/2017.
 */
@Module
public class DbModule {

    @Provides
    @Singleton
    public ContentResolver provideContentResolver(Context context){
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    public DTOProviderTODO provideDTOProviderDto(Context context){
        return new DTOProviderTODO(context);
    }

}
