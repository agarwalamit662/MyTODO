package developer.musicindia.com.mytodo.UI;

import developer.musicindia.com.mytodo.model.TODO;

/**
 * Created by amitagarwal3 on 7/18/2017.
 */
public interface EditToDoPresenter {
    void setView(EditToDoView view);

    void updateTODOIteminDatabase(TODO todo);
    void updateTODOIteminDatabaseAndClose(TODO todo);
    void deleteTODOfromDatabase(int todoid);
}
