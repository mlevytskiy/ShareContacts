package sharecontacts.com.sharecontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        TextView selectAll = (TextView) findViewById(R.id.select_all);
        linkTextStyle(selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    selectContact(adapter.getItem(i));
                }
                adapter.notifyDataSetInvalidated();
            }
        });

        TextView deselectAll = (TextView) findViewById(R.id.deselect_all);
        linkTextStyle(deselectAll);
        deselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    deselectContact(adapter.getItem(i));
                }
                adapter.notifyDataSetInvalidated();
            }
        });

        List<Contact> contacts = Contacts.getQuery().hasPhoneNumber().find();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ContactsAdapter(contacts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact c = adapter.getItem(i);
                if ( TextUtils.equals("null", c.getPhotoUri()) ) {
                    deselectContact(c);
                } else {
                    selectContact(c);
                }
                adapter.notifyDataSetInvalidated();
            }
        });
        listView.setAdapter(adapter);
    }

    private void linkTextStyle(TextView tv) {
        final CharSequence text = tv.getText();
        final SpannableString spannableString = new SpannableString( text );
        spannableString.setSpan(new URLSpan(""), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void deselectContact(Contact c) {
        try {
            Field field = c.getClass().getDeclaredField("photoUri");
            field.setAccessible(true);
            field.set(c, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void selectContact(Contact c) {
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
            strB.append(contact.getPhoneNumbers().get(0).getNormalizedNumber());
            strB.append("\n");
            strB.append("name: ");
            strB.append(contact.getDisplayName());
            strB.append("\n\n");
        }
        if (strB.length() == 0) {
            return "";
        } else {
            return strB.substring(0, strB.length()-2).toString();
        }
    }

}
