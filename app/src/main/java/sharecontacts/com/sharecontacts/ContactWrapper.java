package sharecontacts.com.sharecontacts;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.PhoneNumber;

import java.util.List;

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

    public ContactWrapper(Contact contact, String nameTitle, String phoneTitleSingleNumber, List<String> phoneTitlesMultipleNumbers,
                          String phoneIsEmptyStr) {
        if (phoneTitlesMultipleNumbers.size() != MAX_PHONES_SIZE) {
            throw new IllegalArgumentException("Incorrect multiple phone titles number size");
        }
        this.contact = contact;
        this.nameTitle = nameTitle;
        this.phoneTitleSingleNumber = phoneTitleSingleNumber;
        this.phoneTitleMultipleNumbers = phoneTitlesMultipleNumbers;
        this.phoneIsEmptyStr = phoneIsEmptyStr;
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(nameTitle);
        strBuilder.append(": ");
        strBuilder.append(contact.getDisplayName());
        appendPhoneNumbers(contact.getPhoneNumbers(), strBuilder);
        return strBuilder.toString();
    }

    private void appendPhoneNumbers(List<PhoneNumber> phoneNumbers, StringBuilder strBuilder) {
        strBuilder.append("\n");
        if (phoneNumbers.isEmpty()) {
            strBuilder.append(phoneIsEmptyStr);
            return;
        }
        boolean isSingleNumber = phoneNumbers.size() == 1;
        if (isSingleNumber) {
            strBuilder.append(phoneTitleSingleNumber);
            strBuilder.append(": ");
            strBuilder.append(phoneNumbers.get(0).getNumber());
        } else {
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
