package sharecontacts.com.sharecontacts.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tamir7.contacts.PhoneNumber;

import java.util.List;

import sharecontacts.com.sharecontacts.R;

/**
 * Created by max on 15.04.17.
 */

public class PhonesView extends LinearLayout {

    private static final int PHONES_MAX_COUNT = 5;

    private TextView phone1Title;
    private TextView[] phones = new TextView[PHONES_MAX_COUNT];
    private View[] phoneParents = new View[PHONES_MAX_COUNT];

    public PhonesView(Context context) {
        super(context);
        init(context);
    }

    public PhonesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhonesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.phones_view, this);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        phone1Title = (TextView) findViewById(R.id.phone1_title);
        int i = 0;
        phones[i] = (TextView) findViewById(R.id.phone1);
        phoneParents[i] = findViewById(R.id.phone1_parent);
        i++;
        phones[i] = (TextView) findViewById(R.id.phone2);
        phoneParents[i] = findViewById(R.id.phone2_parent);
        i++;
        phones[i] = (TextView) findViewById(R.id.phone3);
        phoneParents[i] = findViewById(R.id.phone3_parent);
        i++;
        phones[i] = (TextView) findViewById(R.id.phone4);
        phoneParents[i] = findViewById(R.id.phone4_parent);
        i++;
        phones[i] = (TextView) findViewById(R.id.phone5);
        phoneParents[i] = findViewById(R.id.phone5_parent);
    }

    public void setPhones(List<PhoneNumber> phones) {
        int size = Math.min(PHONES_MAX_COUNT, phones.size());
        String firstPhoneTitle = (size == 1) ? "Phone: " : "Phone1: ";
        phone1Title.setText(firstPhoneTitle);
        if (phones.size() < this.phones.length) {
            for (int i = size; i < this.phones.length; i++) {
                this.phoneParents[i].setVisibility(GONE);
            }
            for (int i = 0; i < size; i++) {
                this.phoneParents[i].setVisibility(VISIBLE);
            }
        }
        for (int i = 0; i < size; i++) {
            this.phones[i].setText("" + phones.get(i).getNumber());
        }
    }
}
