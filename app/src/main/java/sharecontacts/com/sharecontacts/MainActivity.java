package sharecontacts.com.sharecontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Contact> contacts = Contacts.getQuery().hasPhoneNumber().find();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ContactsAdapter(contacts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact c = adapter.getItem(i);
                if ( TextUtils.equals("null", c.getPhotoUri()) ) {
                    try {
                        Field field = c.getClass().getDeclaredField("photoUri");
                        field.setAccessible(true);
                        field.set(c, null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Field field = c.getClass().getDeclaredField("photoUri");
                        field.setAccessible(true);
                        field.set(c, "null");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetInvalidated();
            }
        });
        listView.setAdapter(adapter);
    }

    public void onClickShare(View view) {
        startActivity(new IntentApi().share(getPreparedStringForShare()));
    }

    public void onClickEdit(View view) {
        Intent editIntent = new Intent(this, EditActivity.class);
        editIntent.putExtra(EditActivity.KEY_TEXT, getPreparedStringForShare());
        startActivity(editIntent);
    }

    private String getPreparedStringForShare() {
        List<Contact> contacts = adapter.getSelectedContacts();
        if (contacts.isEmpty()) {
            return "";
        } else {
            return createMessage(contacts);
        }
    }

    private String createMessage(List<Contact> selected) {
        StringBuilder strB = new StringBuilder();
        for (Contact contact : selected) {
            strB.append("phone: ");
            strB.append(contact.getPhoneNumbers().get(0));
            strB.append("\n");
            strB.append("name: ");
            strB.append(contact.getDisplayName());
            strB.append("\n");
        }
        return strB.toString();
    }

}
