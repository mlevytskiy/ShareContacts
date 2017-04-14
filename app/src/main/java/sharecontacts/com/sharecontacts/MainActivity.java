package sharecontacts.com.sharecontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 223;
    private ContactsAdapter adapter;
    private Runnable readContacts;

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

        readContacts = new Runnable() {
            @Override
            public void run() {
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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "need read contacts permissions", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST);
            }
        } else {
            readContacts.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts.run();
                }
        }
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
