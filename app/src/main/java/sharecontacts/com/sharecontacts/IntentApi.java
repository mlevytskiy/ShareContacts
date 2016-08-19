package sharecontacts.com.sharecontacts;

import android.content.Intent;

/**
 * Created by maks on 8/19/16.
 * email: m.levytskiy@gmail.com
 */
public class IntentApi {

    public Intent share(String str) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, str);
        return sharingIntent;
    }

}
