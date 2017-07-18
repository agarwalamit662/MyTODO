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
public class AddToDoPresenterImpl implements AddToDoPresenter{

    private AddToDoView view;

    @Inject
    DTOProviderTODO dtoProviderDto;

    public AddToDoPresenterImpl(Context c){
        ((MyToApplication)c).getAppComponent().inject(this);
    }

    @Override
    public void setView(AddToDoView view) {
        this.view = view;
    }

    public void insertTODOIteminDatabase(TODO todo){
        ContentValues values = dtoProviderDto.putContentValues(todo);
        dtoProviderDto.insertTODOIteminDatabase(values);
        int toDoId = dtoProviderDto.getMAXTODOIteminDatabase();
        todo.setTodoId(toDoId);
        view.savedSuccessfully(todo);
    }

}
