package developer.musicindia.com.mytodo.UI;

import android.content.ContentValues;
import android.content.Context;
import developer.musicindia.com.mytodo.app.MyToApplication;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by amitagarwal3 on 7/17/2017.
 */
public class MainPresenterImpl implements MainPresenter{

    private MainView view;

    @Inject
    DTOProviderTODO dtoProviderDto;

    public MainPresenterImpl(Context c){
        ((MyToApplication)c).getAppComponent().inject(this);
    }

    @Override
    public void setView(MainView view) {
        this.view = view;
    }

    @Override
    public void loadToDos() {

        ArrayList<TODO> list = dtoProviderDto.getTODOListinDatabase();
        view.displayAllTodos(list);

    }

    @Override
    public void updateToDo(TODO item) {
        ContentValues values = dtoProviderDto.putContentValues(item);
        dtoProviderDto.updateTODOIteminDatabase(item.getTodoId(),values);
    }

    @Override
    public void deleteTODOfromDatabase(int id){
        dtoProviderDto.deleteTODOfromDatabase(id);
    }

}
