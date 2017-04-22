package sharecontacts.com.sharecontacts.memory;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sharecontacts.com.sharecontacts.CustomApplication;

/**
 * Created by max on 30.12.16.
 * Only for small list (less 1000 items)
 */

public class MemoryCommunicator {

    private static MemoryCommunicator instance = null;
    private static final String SEPARATOR = "ⓡ";
    private SharedPreferences sp;

    public static MemoryCommunicator getInstance() {
        if (instance == null) {
            synchronized (MemoryCommunicator.class) {
                if (instance == null) {
                    instance = new MemoryCommunicator();
                }
            }
        }
        return instance;
    }

    private MemoryCommunicator() {
        sp = PreferenceManager.getDefaultSharedPreferences(CustomApplication.instance.getBaseContext());
    }

    public void saveList(List<String> list, Key key) {
        if (list == null || list.isEmpty()) {
            sp.edit().remove(key.name()).apply();
            return;
        }
        sp.edit().putString(key.name(), TextUtils.join(SEPARATOR, list)).apply();
    }

    public List<String> loadList(Key key) {
        if (sp.contains(key.name())) {
            return Arrays.asList( TextUtils.split(sp.getString(key.name(), ""), Pattern.quote(SEPARATOR)) );
        } else {
            return new ArrayList<>();
        }
    }

    public void saveAdditionalContactInfo(Key key, Map<String, AdditionalContactInfo> map) {
        if (map.isEmpty()) {
            sp.edit().remove(key.toString()).apply();
        } else {
            String str = AdditionalContactInfo.toStr(map);
            sp.edit().putString(key.toString(), str).apply();
        }
    }

    public Map<String, AdditionalContactInfo> loadAdditionalContactInfo(Key key) {
        if (sp.contains(key.toString())) {
            String str = sp.getString(key.toString(), null);
            return AdditionalContactInfo.fromStr(str);
        } else {
            return new HashMap<>();
        }
    }

    public void saveStr(String value, Key key) {
        sp.edit().putString(key.name(), value).apply();
    }

    public String loadStr(Key key) {
        return sp.getString(key.name(), "");
    }

    public boolean hasKey(Key key) {
        return sp.contains(key.name());
    }

    public boolean loadBoolean(Key key) {
        return sp.getBoolean(key.name(), false);
    }


    public void saveBoolean(Key key, boolean value) {
        sp.edit().putBoolean(key.name(), value).apply();
    }

    public void drop() {
        sp.edit().clear().apply();
    }

}
