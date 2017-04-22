package sharecontacts.com.sharecontacts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by max on 20.04.17.
 */

public class ContactsAdapter extends BaseAdapter {

    private List<ContactWrapper> contactWrappers = new ArrayList<>();

    public ContactsAdapter(List<Contact> contacts) {
        List<String> phoneTitles = Arrays.asList("Phone1", "Phone2", "Phone3", "Phone4", "Phone5");
        for (int i = 0; i < contacts.size(); i++) {
            contactWrappers.add(new ContactWrapper(contacts.get(i), "Name", "Phone", phoneTitles, "Phone is empty"));
        }
    }

    @Override
    public int getCount() {
        return contactWrappers.size();
    }

    @Override
    public ContactWrapper getItem(int i) {
        return contactWrappers.get(i);
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
        viewHolder.fill(contactWrappers.get(i));
        return view;
    }

    public List<ContactWrapper> getSelectedContacts() {
        List<ContactWrapper> selected = new ArrayList<>();
        for (ContactWrapper contactWrapper : contactWrappers) {
            if (contactWrapper.isSelected()) {
                selected.add(contactWrapper);
            }
        }
        return selected;
    }

    public void setContactSelected(int index, boolean value) {
        contactWrappers.get(index).setSelected(value);
    }

    public void toggleSelected(int index) {
        ContactWrapper current = contactWrappers.get(index);
        boolean newSelected = !current.isSelected();
        current.setSelected(newSelected);
    }

    private static class ViewHolder {

        private CheckBox checkbox;
        private TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.contact_str);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }

        public void fill(ContactWrapper contactWrapper) {
            textView.setText(contactWrapper.toString());
            this.checkbox.setChecked(contactWrapper.isSelected());
        }

    }

}
