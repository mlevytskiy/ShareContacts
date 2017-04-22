package sharecontacts.com.sharecontacts.memory;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 17.04.17.
 */

public class AdditionalContactInfo {

    private static final String SEPARATOR = "ⓡ";
    private static final String SEPARATOR2 = "Ⓕ";
    private static final String SEPARATOR3 = "ⓩ";

    public String nameTitle;
    public String phoneTitleForSingleNumber;
    public List<String> phoneTitles = new ArrayList<>();

    public static Map<String, AdditionalContactInfo> fromStr(String str) {
        String[] array = str.split(SEPARATOR3);
        Map<String, AdditionalContactInfo> map = new HashMap<>();
        for (String item : array) {
            addEntryFromStr(map, item);
        }
        return map;
    }

    public static String toStr(Map<String, AdditionalContactInfo> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, AdditionalContactInfo> entry : map.entrySet()) {
            stringBuilder.append(entryToStr(entry));
            stringBuilder.append(SEPARATOR3);
        }
        return stringBuilder.substring(0, stringBuilder.length()-1).toString();
    }

    private static String entryToStr(Map.Entry<String, AdditionalContactInfo> entry) {
        String[] strings = new String[] {
                entry.getKey(),
                TextUtils.join(SEPARATOR2, entry.getValue().phoneTitles)
        };
        return TextUtils.join(SEPARATOR, strings);
    }

    private static void addEntryFromStr(Map<String, AdditionalContactInfo> map, String str) {
        String[] array = str.split(SEPARATOR);
        AdditionalContactInfo additionalContactInfo = new AdditionalContactInfo();
        additionalContactInfo.phoneTitles = Arrays.asList(array[1].split(SEPARATOR2));
        map.put(array[0], additionalContactInfo);
    }



}
