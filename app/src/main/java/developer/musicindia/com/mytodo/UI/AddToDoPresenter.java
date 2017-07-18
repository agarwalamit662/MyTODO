package developer.musicindia.com.mytodo.UI;

import android.content.ContentValues;
import developer.musicindia.com.mytodo.model.TODO;

/**
 * Created by amitagarwal3 on 7/18/2017.
 */
public interface AddToDoPresenter {
    void setView(AddToDoView view);

    void insertTODOIteminDatabase(TODO todo);
}
