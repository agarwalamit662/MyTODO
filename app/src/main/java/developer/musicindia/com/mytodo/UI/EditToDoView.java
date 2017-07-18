package developer.musicindia.com.mytodo.UI;

import developer.musicindia.com.mytodo.model.TODO;

/**
 * Created by amitagarwal3 on 7/17/2017.
 */
public interface EditToDoView {

    void savedSuccessfully(TODO todo);
    void deletedSuccessfully();
}
