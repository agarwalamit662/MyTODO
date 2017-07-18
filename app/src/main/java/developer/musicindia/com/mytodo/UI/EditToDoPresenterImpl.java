package developer.musicindia.com.mytodo.UI;

import android.content.ContentValues;
import android.content.Context;
import developer.musicindia.com.mytodo.app.MyToApplication;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;

import javax.inject.Inject;

/**
 * Created by amitagarwal3 on 7/17/2017.
 */
public class EditToDoPresenterImpl implements EditToDoPresenter{

    private EditToDoView view;

    @Inject
    DTOProviderTODO dtoProviderDto;

    public EditToDoPresenterImpl(Context c){
        ((MyToApplication)c).getAppComponent().inject(this);
    }

    @Override
    public void setView(EditToDoView view) {
        this.view = view;
    }

    public void updateTODOIteminDatabase(TODO todo){
        ContentValues values = dtoProviderDto.putContentValues(todo);
        dtoProviderDto.updateTODOIteminDatabase(todo.getTodoId(), values);
    }

    @Override
    public void updateTODOIteminDatabaseAndClose(TODO todo) {
        ContentValues values = dtoProviderDto.putContentValues(todo);
        dtoProviderDto.updateTODOIteminDatabase(todo.getTodoId(), values);
        view.savedSuccessfully(todo);

    }

    @Override
    public void deleteTODOfromDatabase(int todoid) {
        dtoProviderDto.deleteTODOfromDatabase(todoid);
        view.deletedSuccessfully();
    }
}
