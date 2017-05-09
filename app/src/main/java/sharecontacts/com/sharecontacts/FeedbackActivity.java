package sharecontacts.com.sharecontacts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import sharecontacts.com.sharecontacts.util.SecureStr;
import sharecontacts.com.sharecontacts.util.SecureUtils;

/**
 * Created by max on 01.05.17.
 */

public class FeedbackActivity extends Activity {

    private EditText mailEditText;
    private EditText messageEditText;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_feedback);
        mailEditText = (EditText) findViewById(R.id.mail_edit_text);
        messageEditText = (EditText) findViewById(R.id.message_edit_text);
        fillMail();
    }

    private void fillMail() {
        String gmailUserName = getEmail();
        if (!TextUtils.isEmpty(gmailUserName)) {
            mailEditText.setText(gmailUserName);
        }
    }

    public void onClickClose(View view) {
        hideKeyboard();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);

    }

    public void onClickSend(View view) {
        String email = getEmail();
        String message = messageEditText.getText().toString();

        final String[] senderPasswrdReceiver = SecureUtils.decrypt(SecureStr.STR).split("\\|");

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(FeedbackActivity.this, "message is empty", Toast.LENGTH_LONG).show();
            return;
        }
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(FeedbackActivity.this, "message is sending...", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            protected Boolean doInBackground(String... strs) {
                try {
                    new GMailSender(senderPasswrdReceiver[0], senderPasswrdReceiver[1])
                            .sendMail(strs[0], strs[1] + ":" + strs[0], senderPasswrdReceiver[0], senderPasswrdReceiver[2]);
                } catch (Exception e) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean hasError) {
                super.onPostExecute(hasError);
                Toast.makeText(FeedbackActivity.this, hasError ?
                        "Message wasn't sent" : "Message was sent"
                        , Toast.LENGTH_SHORT).show();
            }
        }.execute(message, email);
    }

    public String getEmail() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                return account.name;
            }
        }
        return null;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
