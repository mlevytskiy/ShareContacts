package sharecontacts.com.sharecontacts;

import android.app.Application;

import com.github.tamir7.contacts.Contacts;

import sharecontacts.com.sharecontacts.memory.MemoryCommunicator;

/**
 * Created by maks on 8/19/16.
 * email: m.levytskiy@gmail.com
 */
public class CustomApplication extends Application {

    public static CustomApplication instance;
    public MemoryCommunicator memory;

    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
        instance = this;
        memory = MemoryCommunicator.getInstance();
    }
}
