package sharecontacts.com.sharecontacts;

import android.text.TextUtils;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.PhoneNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 23.04.17.
 */

public class DefaultContactTitles {

    public static final DefaultContactTitles instance = new DefaultContactTitles();
    private Map<String, List<String>> phonesTitleAndName = new HashMap<>();
    private boolean isNeedUseSinglePhoneTitle = false;

    public List<String> getPhoneTitles(String contactName) {
        List<String> phonesTitle = phonesTitleAndName.get(contactName);
        List<String> defaultList = isNeedUseSinglePhoneTitle ?
                Arrays.asList("Phone", "Phone", "Phone", "Phone", "Phone") :
                Arrays.asList("Phone1", "Phone2", "Phone3", "Phone4", "Phone5");
        if (phonesTitle == null) {
            return defaultList;
        } else {
            for (int i = 0; i < 5; i++) {
                if (i < phonesTitle.size() && !TextUtils.isEmpty(phonesTitle.get(i))) {
                    defaultList.set(i, phonesTitle.get(i));
                }
            }
            return defaultList;
        }
    }

    public String getSinglePhoneTitle(String contactName) {
        List<String> phonesTitle = phonesTitleAndName.get(contactName);
        if (phonesTitle == null || TextUtils.isEmpty(phonesTitle.get(0))) {
            return CustomApplication.instance.getResources().getString(R.string.phone);
        } else {
            return phonesTitle.get(0);
        }
    }


    public String getNameTitle(String contactName) {
        return CustomApplication.instance.getResources().getString(R.string.name);
    }

    public void fillPhonesTitles(String contactName, List<String> values) {
        phonesTitleAndName.put(contactName, values);
    }

    private String phoneTypeToString(PhoneNumber.Type type) {
        String typeStr = type.name().substring(0, 1) + type.name().substring(1).toLowerCase();
        return typeStr;
    }

    public void fill(List<Contact> contacts) {
        for (Contact contact : contacts) {
            List<String> phoneLabels = new ArrayList<>();
            for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                String label = phoneNumber.getLabel();
                if (!TextUtils.isEmpty(label)) {
                    phoneLabels.add(label);
                } else {
                    PhoneNumber.Type type = phoneNumber.getType();
                    if (type != PhoneNumber.Type.UNKNOWN && type != PhoneNumber.Type.MOBILE ) {
                        phoneLabels.add(phoneTypeToString(type));
                        isNeedUseSinglePhoneTitle = true;
                    } else {
                        phoneLabels.add(null);
                    }
                }
            }
            if (!phoneLabels.isEmpty()) {
                phonesTitleAndName.put(contact.getDisplayName(), phoneLabels);
            }
        }
    }

}
