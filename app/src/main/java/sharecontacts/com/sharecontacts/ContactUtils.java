package sharecontacts.com.sharecontacts;

import com.github.tamir7.contacts.Contact;

import java.util.List;

/**
 * Created by max on 15.04.17.
 */

public class ContactUtils {

    public static String toString(List<Contact> selected) {
        StringBuilder strB = new StringBuilder();
        if (selected.isEmpty()) {
            return "";
        }
        for (Contact contact : selected) {
            strB.append("name: ");
            strB.append(contact.getDisplayName());
            strB.append("\n");
            contactToString(contact, strB);
            strB.append("\n");
        }
        return strB.substring(0, strB.length()-2).toString();
    }

    private static void contactToString(Contact contact, StringBuilder strB) {
        if (contact.getPhoneNumbers().size() == 1) {
            strB.append("phone: ");
            strB.append(contact.getPhoneNumbers().get(0).getNormalizedNumber());
            strB.append("\n");
        } else {
            for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
                strB.append("phone");
                strB.append(i+1);
                strB.append(": ");
                strB.append(contact.getPhoneNumbers().get(i).getNormalizedNumber());
                strB.append("\n");
            }
        }

    }

}
