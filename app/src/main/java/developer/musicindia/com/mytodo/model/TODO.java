package developer.musicindia.com.mytodo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by amitagarwal3 on 6/29/2016.
 */
public class TODO implements Serializable{

    private int todoId;
    private String todoText;
    private String todoTextDes;
    private Date reminderTime;
    private int completed;

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public TODO(int todoId, String todoText, String todoTextDes, Date reminderTime,int completed) {
        this.todoId = todoId;
        this.todoText = todoText;
        this.todoTextDes = todoTextDes;
        this.reminderTime = reminderTime;
        this.completed = completed;
    }

    public TODO() {
        super();
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public String getTodoTextDes() {
        return todoTextDes;
    }

    public void setTodoTextDes(String todoTextDes) {
        this.todoTextDes = todoTextDes;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }
}
