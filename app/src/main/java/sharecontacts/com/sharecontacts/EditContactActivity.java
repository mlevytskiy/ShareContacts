package sharecontacts.com.sharecontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sharecontacts.com.sharecontacts.memory.AdditionalContactInfo;
import sharecontacts.com.sharecontacts.memory.Key;

/**
 * Created by max on 22.04.17.
 */

public class EditContactActivity extends Activity {

    public final static int PHONES_LENGTH = 5;
    public final static String PHONES = "phonesEditText";
    public final static String NAME = "name";
    public final static String PHONES_DELIMITER = "|";
    private EditText nameTitleEditText;
    private TextView nameValue;
    private EditText[] phonesEditText = new EditText[PHONES_LENGTH];
    private TextView[] phoneValues = new TextView[PHONES_LENGTH];
    private View[] phonesContainer = new View[PHONES_LENGTH];
    private Map<String, AdditionalContactInfo> map;
    private String name;

    private List<String> editTextsStartValues = new ArrayList<>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_contact);
        initFromXml();
        fill();
        fillEditTextDefaultValues();
        setResult(RESULT_CANCELED);
    }

    private void initFromXml() {
        nameTitleEditText = (EditText) findViewById(R.id.name_title);
        nameValue = (TextView) findViewById(R.id.name_value);
        int i = 0;
        phonesEditText[i] = (EditText) findViewById(R.id.phone1_title);
        phoneValues[i] = (TextView) findViewById(R.id.phone1_value);
        phonesContainer[i] = findViewById(R.id.phone1_container);
        i++;
        phonesEditText[i] = (EditText) findViewById(R.id.phone2_title);
        phoneValues[i] = (TextView) findViewById(R.id.phone2_value);
        phonesContainer[i] = findViewById(R.id.phone2_container);
        i++;
        phonesEditText[i] = (EditText) findViewById(R.id.phone3_title);
        phoneValues[i] = (TextView) findViewById(R.id.phone3_value);
        phonesContainer[i] = findViewById(R.id.phone3_container);
        i++;
        phonesEditText[i] = (EditText) findViewById(R.id.phone4_title);
        phoneValues[i] = (TextView) findViewById(R.id.phone4_value);
        phonesContainer[i] = findViewById(R.id.phone4_container);
        i++;
        phonesEditText[i] = (EditText) findViewById(R.id.phone5_title);
        phoneValues[i] = (TextView) findViewById(R.id.phone5_value);
        phonesContainer[i] = findViewById(R.id.phone5_container);
    }

    private void fill() {
        name = getIntent().getStringExtra(NAME);
        AdditionalContactInfo additionalContactInfo = getAdditionalContactInfo(name);
        nameValue.setText(name);
        String nameTitle = (additionalContactInfo == null) ?
                DefaultContactTitles.instance.getNameTitle(name) :
                (TextUtils.isEmpty(additionalContactInfo.nameTitle)) ?
                DefaultContactTitles.instance.getNameTitle(name) :
                additionalContactInfo.nameTitle;
        nameTitleEditText.setText("");
        nameTitleEditText.append(nameTitle);
        String phonesStr = getIntent().getStringExtra(PHONES);


        if (phonesStr.contains(PHONES_DELIMITER)) {
            String[] phones = phonesStr.split("\\|");
            for (int i = 0; i < phones.length; i++) {
                phoneValues[i].setText(phones[i]);
                phonesContainer[i].setVisibility(View.VISIBLE);
                String phoneTitle = (additionalContactInfo == null) ?
                        DefaultContactTitles.instance.getPhoneTitles(name).get(i) :
                        (additionalContactInfo.phoneTitles == null) ?
                                DefaultContactTitles.instance.getPhoneTitles(name).get(i) :
                                additionalContactInfo.phoneTitles.get(i);
                phonesEditText[i].setText(phoneTitle);
            }
        } else {
            String singlePhoneTitle = (additionalContactInfo == null) ?
                    DefaultContactTitles.instance.getSinglePhoneTitle(name) :
                    additionalContactInfo.phoneTitleForSingleNumber;
            phonesEditText[0].setText(singlePhoneTitle);
            phoneValues[0].setText(phonesStr);
        }
    }

    private void fillEditTextDefaultValues() {
        editTextsStartValues.add(nameTitleEditText.getText().toString());
        for (int i = 0; i < 5; i++) {
            editTextsStartValues.add(phonesEditText[i].getText().toString());
        }

    }

    private AdditionalContactInfo getAdditionalContactInfo(String displayName) {
        map = CustomApplication.instance.memory.loadAdditionalContactInfo(Key.additionalContactInfo);
        AdditionalContactInfo additionalContactInfo = map.get(displayName);
        return additionalContactInfo;
    }


    public void onClickOK(View view) {
        String nameTitle = nameTitleEditText.getText().toString();
        AdditionalContactInfo additionalContactInfo = new AdditionalContactInfo();
        boolean isNeedSaveContactInfo = false;

        if (!TextUtils.isEmpty(nameTitle) && !TextUtils.equals(editTextsStartValues.get(0), nameTitle)) {
            additionalContactInfo.nameTitle = nameTitle;
            isNeedSaveContactInfo = true;
        }

        boolean isSinglePhoneTitle = phonesContainer[1].getVisibility() == View.GONE;

        if (isSinglePhoneTitle) {
            String phoneTitle = phonesEditText[0].getText().toString();
            if (!TextUtils.isEmpty(phoneTitle) && !TextUtils.equals(editTextsStartValues.get(1), phoneTitle)) {
                additionalContactInfo.phoneTitleForSingleNumber = phoneTitle;
                isNeedSaveContactInfo = true;
            }
        } else {

            List<String> phonesTitle = new ArrayList<>();
            for(int i = 0; i < 5; i++) {
                String phoneTitle = phonesEditText[i].getText().toString();
                if (!TextUtils.equals(editTextsStartValues.get(1+i), phoneTitle)) {
                    phonesTitle.add(phoneTitle);
                    additionalContactInfo.phoneTitles = phonesTitle;
                    isNeedSaveContactInfo = true;
                } else {
                    phonesTitle.add(editTextsStartValues.get(1+i));
                }
            }

        }

        if (isNeedSaveContactInfo) {
            map.put(name, additionalContactInfo);
            CustomApplication.instance.memory.saveAdditionalContactInfo(Key.additionalContactInfo, map);
            setResult(RESULT_OK, new Intent().putExtra(NAME, name));
        }

        onBackPressed();
    }

}
