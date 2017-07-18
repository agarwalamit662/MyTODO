package developer.musicindia.com.mytodo.UI;

import developer.musicindia.com.mytodo.model.TODO;

/**
 * Created by amitagarwal3 on 7/17/2017.
 */
public interface MainPresenter {



    void setView(MainView view);

    void loadToDos();

    void updateToDo(TODO item);

    void deleteTODOfromDatabase(int id);

}
