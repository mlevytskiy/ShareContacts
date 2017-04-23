package sharecontacts.com.sharecontacts;

import android.text.TextUtils;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.PhoneNumber;

import java.util.List;
import java.util.Map;

import sharecontacts.com.sharecontacts.memory.AdditionalContactInfo;

/**
 * Created by max on 20.04.17.
 */

public class ContactWrapper {

    public static final int MAX_PHONES_SIZE = 5;

    public Contact contact;
    private boolean isSelected = false;
    private String nameTitle;
    private String phoneTitleSingleNumber;
    private List<String> phoneTitleMultipleNumbers;
    private String phoneIsEmptyStr;
    private Map<String, AdditionalContactInfo> map;

    public ContactWrapper(ContactWrapper oldContactWrapper, Map<String, AdditionalContactInfo> newMap) {
        this(oldContactWrapper.contact, oldContactWrapper.nameTitle, oldContactWrapper.phoneTitleSingleNumber,
                oldContactWrapper.phoneTitleMultipleNumbers, oldContactWrapper.phoneIsEmptyStr, newMap);
    }

    public ContactWrapper(Contact contact, String nameTitle, String phoneTitleSingleNumber, List<String> phoneTitlesMultipleNumbers,
                          String phoneIsEmptyStr, Map<String, AdditionalContactInfo> map) {
        if (phoneTitlesMultipleNumbers.size() != MAX_PHONES_SIZE) {
            throw new IllegalArgumentException("Incorrect multiple phone titles number size");
        }
        this.contact = contact;
        this.nameTitle = nameTitle;
        this.phoneTitleSingleNumber = phoneTitleSingleNumber;
        this.phoneTitleMultipleNumbers = phoneTitlesMultipleNumbers;
        this.phoneIsEmptyStr = phoneIsEmptyStr;
        this.map = map;
    }

    public String toString() {
        AdditionalContactInfo additionalContactInfo = map.get(contact.getDisplayName());
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(additionalContactInfo == null ?
                nameTitle :
                TextUtils.isEmpty(additionalContactInfo.nameTitle) ?
                        nameTitle :
                        additionalContactInfo.nameTitle);
        strBuilder.append(": ");
        strBuilder.append(contact.getDisplayName());
        appendPhoneNumbers(contact.getPhoneNumbers(), strBuilder, additionalContactInfo);
        return strBuilder.toString();
    }

    private void appendPhoneNumbers(List<PhoneNumber> phoneNumbers, StringBuilder strBuilder, AdditionalContactInfo additionalContactInfo) {
        strBuilder.append("\n");
        if (phoneNumbers.isEmpty()) {
            strBuilder.append(phoneIsEmptyStr);
            return;
        }
        boolean isSingleNumber = phoneNumbers.size() == 1;
        if (isSingleNumber) {
            strBuilder.append(additionalContactInfo == null ? phoneTitleSingleNumber : additionalContactInfo.phoneTitleForSingleNumber);
            strBuilder.append(": ");
            strBuilder.append(phoneNumbers.get(0).getNumber());
        } else {
            if (additionalContactInfo != null) {
                for (int i = 0; i < additionalContactInfo.phoneTitles.size(); i++) {
                    String title = additionalContactInfo.phoneTitles.get(i);
                    if ( !TextUtils.isEmpty(title) ) {
                        phoneTitleMultipleNumbers.set(i, title);
                    }
                }
            }
            int size = Math.min(MAX_PHONES_SIZE, phoneNumbers.size());
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    strBuilder.append("\n");
                }
                strBuilder.append(phoneTitleMultipleNumbers.get(i));
                strBuilder.append(": ");
                strBuilder.append(phoneNumbers.get(i).getNumber());
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
