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
                    adapter.setContactSelected(i, true);
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
                    adapter.setContactSelected(i, false);
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
                        adapter.toggleSelected(i);
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

    public void onClickEdit2(View view) {
        Toast.makeText(this, "onClickEdit2", Toast.LENGTH_LONG).show();
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
        List<ContactWrapper> contacts = adapter.getSelectedContacts();
        return TextUtils.join("\n\n", contacts);
    }

}
