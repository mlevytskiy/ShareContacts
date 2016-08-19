package sharecontacts.com.sharecontacts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;

import java.util.List;

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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
        Contact c = contacts.get(i);
        if (c.getPhoneNumbers().isEmpty()) {
            viewHolder.phone.setText("null");
        } else {
            viewHolder.phone.setText(c.getPhoneNumbers().get(0).getNumber());
        }
        viewHolder.name.setText(c.getDisplayName());
        return view;
    }

    private static class ViewHolder {

        public TextView name;
        public TextView phone;

        public ViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.name);
            this.phone = (TextView) view.findViewById(R.id.phone);
        }

    }

}
