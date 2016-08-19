package sharecontacts.com.sharecontacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Contact> contacts = Contacts.getQuery().hasPhoneNumber().find();
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ContactsAdapter(contacts));
    }
}
