package sharecontacts.com.sharecontacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by maks on 8/19/16.
 * email: m.levytskiy@gmail.com
 */
public class EditActivity extends AppCompatActivity {

    public static final String KEY_TEXT = "key_text";
    private EditText editText;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String message = getIntent().getStringExtra(KEY_TEXT);
        editText = (EditText) findViewById(R.id.edit_text);
        editText.setText(message);
    }

    public void onClickShare(View view) {
        startActivity(new IntentApi().share(editText.getText().toString()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
