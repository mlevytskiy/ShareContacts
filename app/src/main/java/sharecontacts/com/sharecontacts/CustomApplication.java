package sharecontacts.com.sharecontacts;

import android.app.Application;
import com.github.tamir7.contacts.Contacts;

/**
 * Created by maks on 8/19/16.
 * email: m.levytskiy@gmail.com
 */
public class CustomApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
    }
}
