package sharecontacts.com.sharecontacts;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;

import java.util.ArrayList;
import java.util.List;

import sharecontacts.com.sharecontacts.view.PhonesView;

/**
 * Created by maks on 8/19/16.
 * email: m.levytskiy@gmail.com
 */
public class ContactsAdapter extends BaseAdapter {

    private List<Contact> contacts;

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_contact, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.fill(contacts.get(i));
        return view;
    }

    private static class ViewHolder {

        private TextView name;
        private PhonesView phonesView;
        private CheckBox checkbox;

        public ViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.name);
            this.phonesView = (PhonesView) view.findViewById(R.id.phones_view);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }

        public void fill(Contact c) {
            checkbox.setChecked( TextUtils.equals("null", c.getPhotoUri()) );
            phonesView.setPhones(c.getPhoneNumbers());
            name.setText(c.getDisplayName());
        }

    }

    public List<Contact> getSelectedContacts() {
        List<Contact> selected = new ArrayList<>();
        for (Contact contact : contacts) {
            if ( TextUtils.equals("null", contact.getPhotoUri())) {
                selected.add(contact);
            }
        }
        return selected;
    }

}
