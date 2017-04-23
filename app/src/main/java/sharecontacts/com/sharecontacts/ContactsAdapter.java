package sharecontacts.com.sharecontacts;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sharecontacts.com.sharecontacts.memory.AdditionalContactInfo;
import sharecontacts.com.sharecontacts.memory.Key;

/**
 * Created by max on 20.04.17.
 */

public class ContactsAdapter extends BaseAdapter {

    private List<ContactWrapper> contactWrappers = new ArrayList<>();

    public ContactsAdapter(List<Contact> contacts) {
        DefaultContactTitles.instance.fill(contacts);
        for (int i = 0; i < contacts.size(); i++) {
            Contact current = contacts.get(i);
            String nameTitle = DefaultContactTitles.instance.getNameTitle(current.getDisplayName());
            String singlePhoneTitle = DefaultContactTitles.instance.getSinglePhoneTitle(current.getDisplayName());
            List<String> phoneTitles = DefaultContactTitles.instance.getPhoneTitles(current.getDisplayName());
            Map<String, AdditionalContactInfo> map = CustomApplication.instance.memory.loadAdditionalContactInfo(Key.additionalContactInfo);
            contactWrappers.add(new ContactWrapper(current, nameTitle, singlePhoneTitle, phoneTitles, "Phone is empty", map));
        }
    }

    public void update(String contactName) {
        Map<String, AdditionalContactInfo> map = CustomApplication.instance.memory.loadAdditionalContactInfo(Key.additionalContactInfo);
        int index = 0;
        for (int i = 0; i < contactWrappers.size(); i++) {
            if (TextUtils.equals(contactName, contactWrappers.get(i).contact.getDisplayName())) {
                index = i;
                break;
            }
        }
        ContactWrapper oldWrapper = contactWrappers.get(index);
        ContactWrapper newWrapper = new ContactWrapper(oldWrapper, map);
        contactWrappers.set(index, newWrapper);
        notifyDataSetChanged();
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
        private ImageButton editContact;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.contact_str);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            this.editContact = (ImageButton) view.findViewById(R.id.edit_contact_image_button);
        }

        public void fill(ContactWrapper contactWrapper) {
            this.textView.setText(contactWrapper.toString());
            this.checkbox.setChecked(contactWrapper.isSelected());
            this.editContact.setTag(contactWrapper);
        }

    }

}
