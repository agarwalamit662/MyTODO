package developer.musicindia.com.mytodo.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import developer.musicindia.com.mytodo.UI.MainPresenter;
import developer.musicindia.com.mytodo.UI.MainView;
import developer.musicindia.com.mytodo.R;
import developer.musicindia.com.mytodo.app.MyToApplication;
import developer.musicindia.com.mytodo.model.TODO;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView{


    @Inject
    MainPresenter mainPresenter;

    public static String TODO;
    private ArrayList<TODO> arrList;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private TODORecyclerViewAdapter adapter;

	public static int[] colorArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((MyToApplication)getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        colorArr = getApplication().getResources().getIntArray(R.array.colorArrChoice);
        setSupportActionBar(toolbar);

        mainPresenter.setView(this);
        mainPresenter.loadToDos();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTODOActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_temp, menu);
        return true;
    }

    @Override

    public void displayAllTodos(ArrayList<TODO> arrList){

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new TODORecyclerViewAdapter(MainActivity.this,arrList );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();


        mainPresenter.setView(this);
        mainPresenter.loadToDos();

    }




    public class TODORecyclerViewAdapter 
            extends RecyclerView.Adapter<TODORecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<TODO> mValues;
        private Context mContext;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

            public String mBoundString;
            public CircleImageView taskCompleted;
            public TextView titleToDo;
            public ImageView deleteToDo;
            public ImageView alarmTime;
            public TextView alarmTimeValue;
            public TextView toDoDesc;
            public ImageView clickForMore;
            public TextView hiddenDesText;
            public final View mView;
            public CardView cardView;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                taskCompleted = (CircleImageView) view.findViewById(R.id.taskCompleted);
                titleToDo = (TextView) view.findViewById(R.id.titleToDo);
                deleteToDo = (ImageView) view.findViewById(R.id.deleteToDo);
                alarmTime = (ImageView) view.findViewById(R.id.alarmTime);
                alarmTimeValue = (TextView) view.findViewById(R.id.alarmTimeValue);
                toDoDesc = (TextView) view.findViewById(R.id.toDoDesc);
                clickForMore = (ImageView) view.findViewById(R.id.clickForMore);
                hiddenDesText = (TextView) view.findViewById(R.id.hiddenDesText);
                cardView = (CardView) view.findViewById(R.id.todoItemCardView);

            }

            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        }

        public TODO getValueAt(int position) {
            return mValues.get(position);
        }

        public TODORecyclerViewAdapter(Context context, List<TODO> items) {
            mValues = items;
            mContext = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_main, parent, false);

            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            int posColor = position % colorArr.length;
            holder.cardView.setCardBackgroundColor(colorArr[posColor]);
            holder.titleToDo.setText(mValues.get(position).getTodoText());
            holder.titleToDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TODO intentItem = mValues.get(position);
                    Intent i = new Intent(mContext, EditActivity.class);
                    i.putExtra(MainActivity.TODO, intentItem);
                    mContext.startActivity(i);


                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TODO intentItem = mValues.get(position);
                    Intent i = new Intent(mContext, EditActivity.class);
                    i.putExtra(MainActivity.TODO, intentItem);
                    mContext.startActivity(i);

                }
            });

            String day = formatDate("d MMM, yyyy",mValues.get(position).getReminderTime());
            String dateFormat;
            if(DateFormat.is24HourFormat(mContext)){
                dateFormat = "k:mm";
            }
            else{
                dateFormat = "h:mm a";

            }
            String time = formatDate(dateFormat,mValues.get(position).getReminderTime());
            holder.alarmTimeValue.setText(day + " @ " + time);

            holder.toDoDesc.setText(mValues.get(position).getTodoTextDes());
            //String url = mValues.get(position).getImageURL();

            holder.clickForMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.hiddenDesText.setVisibility(View.VISIBLE);
                    holder.hiddenDesText.setTypeface(null, Typeface.ITALIC);
                    holder.hiddenDesText.setText(mValues.get(position).getTodoTextDes());


                }
            });

            if(mValues.get(position).getCompleted() == 0 ){

                Drawable d = mContext.getResources().getDrawable(R.drawable.btn_chkbox_disabled);
                holder.taskCompleted.setImageDrawable(d);
            }
            else{
                Drawable d = mContext.getResources().getDrawable(R.drawable.ic_check_todo_enabled);
                holder.taskCompleted.setImageDrawable(d);
            }

            holder.taskCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animFade;

                    animFade = AnimationUtils.loadAnimation(mContext, R.anim.fadeout);


                    animFade.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {

                        }
                        public void onAnimationRepeat(Animation animation) {}
                        public void onAnimationEnd(Animation animation) {
                            // when fadeout animation ends, fade in your second image

                            if(mValues.get(position).getCompleted() == 0){
                                Drawable d = mContext.getResources().getDrawable(R.drawable.ic_check_todo_enabled);
                                holder.taskCompleted.setImageDrawable(d);

                                TODO item = mValues.get(position);
                                item.setCompleted(1);

                                /*ContentValues values = dtoProviderDto.putContentValues(item);
                                dtoProviderDto.updateTODOIteminDatabase(item.getTodoId(),values);*/
                                mainPresenter.updateToDo(item);


                                mValues.get(position).setCompleted(1);
                                notifyDataSetChanged();
                            }
                            else{
                                Drawable d = mContext.getResources().getDrawable(R.drawable.btn_chkbox_disabled);
                                holder.taskCompleted.setImageDrawable(d);

                                TODO item = mValues.get(position);
                                item.setCompleted(0);

                                /*ContentValues values = dtoProviderDto.putContentValues(item);
                                dtoProviderDto.updateTODOIteminDatabase(item.getTodoId(),values);*/

                                mainPresenter.updateToDo(item);

                                mValues.get(position).setCompleted(0);
                                notifyDataSetChanged();
                            }

                        }
                    });
                    holder.taskCompleted.startAnimation(animFade);

                }
            });

            holder.toDoDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.hiddenDesText.setVisibility(View.VISIBLE);
                    holder.hiddenDesText.setTypeface(null, Typeface.ITALIC);
                    holder.hiddenDesText.setText(mValues.get(position).getTodoTextDes());


                }
            });

            holder.hiddenDesText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.hiddenDesText.setVisibility(View.INVISIBLE);
                }
            });

            holder.deleteToDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animRotate;

                    animRotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate);


                    animRotate.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {


                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                            alertDialogBuilder.setMessage("Are you sure,You wanted to delete this TODO");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {

                                    TODO todo = mValues.get(position);

                                    mainPresenter.deleteTODOfromDatabase(todo.getTodoId());

                                    Toast.makeText(mContext, "Deleted TODO Successfully", Toast.LENGTH_SHORT).show();

                                    removeAt(position);
                                    dialog.dismiss();

                                }
                            });

                            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    });

                    holder.deleteToDo.startAnimation(animRotate);

                }
            });

        }


        public String formatDate(String formatString, Date dateToFormat){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
            return simpleDateFormat.format(dateToFormat);
        }

        @Override
        public int getItemCount() {
            if(mValues != null && mValues.size() > 0)
                return mValues.size();
            else
                return 0;
        }

        public void removeAt(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mValues.size());
        }


    }

}
