package com.kovalievskyi.labwave;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBase db;
    private ListView tasksView;
    private ArrayAdapter<String> arrayAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DataBase(this);
        tasksView = findViewById(R.id.tasks_view);

        loadAllTasks();
    }

    private void loadAllTasks() {
        ArrayList<String> tasks = db.getAllTasks();

        if (arrayAdapter == null){
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.tasks_row, R.id.task_name, tasks);
            tasksView.setAdapter(arrayAdapter);
        }else{
            arrayAdapter.clear();
            arrayAdapter.addAll(tasks);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_new_task){
            final EditText task_description = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Додати завдання")
                    .setMessage("Вкажіть що Ви плануєте?")
                    .setView(task_description)
                    .setPositiveButton("Додати", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(task_description.getText());
                            db.addTask(task);
                            loadAllTasks();
                        }
                    })
                    .setNegativeButton("Відмінити", null)
                    .create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View button) {
        View purrent = (View) button.getParent();
        TextView textView = purrent.findViewById(R.id.task_name);
        String task = String.valueOf(textView.getText());
        db.delTask(task);
        loadAllTasks();
    }
}