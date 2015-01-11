package izhyzhyrii.todo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    private EditText input_edit_item;
    private Integer itemPositionInParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        this.input_edit_item = (EditText)findViewById(R.id.input_edit_item);

        String initialValue = getIntent().getExtras().getString("value");
        this.itemPositionInParent = getIntent().getExtras().getInt("position");

        this.input_edit_item.setText(initialValue);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);

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

    public void onSave(View view) {

        String todo = input_edit_item.getText().toString();

        if (todo == null || todo.length() == 0) {
            return; // do not add empty items
        }

        Intent data = new Intent();

        data.putExtra("value", todo);
        data.putExtra("position", itemPositionInParent);

        setResult(RESULT_OK, data);
        this.finish();
    }
}
