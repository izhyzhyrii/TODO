package izhyzhyrii.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class TodoListActivity extends ActionBarActivity {

    private EditText input_new_todo_item;

    private ArrayList<String> todos;
    private ArrayAdapter<String> todo_list_adapter;
    private ListView todo_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        this.input_new_todo_item = (EditText)findViewById(R.id.input_new_todo_item);
        this.todo_list = (ListView)findViewById(R.id.todo_list);

        this.todos = new ArrayList<>();
        load();
        this.todo_list_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todos);
        this.todo_list.setAdapter(todo_list_adapter);

        this.todo_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                todos.remove(pos);
                todo_list_adapter.notifyDataSetChanged();
                save();
                return true;
            }
        });

        this.todo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {

                // first parameter is the context, second is the class of the activity to launch
                Intent editIntent = new Intent(TodoListActivity.this, EditItemActivity.class);
                editIntent.putExtra("position", pos);
                editIntent.putExtra("value", todos.get(pos));

                startActivityForResult(editIntent, EDIT_ITEM_REQUEST_CODE); // brings up the second activity
            }
        });
    }

    private Integer EDIT_ITEM_REQUEST_CODE = 1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
            // Extract name value from result extras
            String value = data.getExtras().getString("value");
            Integer position = data.getExtras().getInt("position");
            todos.set(position, value);
            todo_list_adapter.notifyDataSetChanged();
            save();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_display, menu);
        return true;
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

    public void onAdd(View view) {

        String todo = input_new_todo_item.getText().toString();

        if (todo == null || todo.length() == 0) {
            return; // do not add empty items
        }

        todo_list_adapter.add(todo);
        input_new_todo_item.setText("");
        save();
    }

    private void load(){
        File todoFile = this.GetTodoFile();
        try{
            this.todos = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch(IOException exception){
            exception.printStackTrace();
        }
    }

    private void save(){
        File todoFile = this.GetTodoFile();
        try{
            FileUtils.writeLines(todoFile, todos);
        } catch(IOException exception){
            exception.printStackTrace();
        }
    }

    private File GetTodoFile(){
        File dir = getFilesDir();
        return new File(dir, "todo.txt");
    }
}
