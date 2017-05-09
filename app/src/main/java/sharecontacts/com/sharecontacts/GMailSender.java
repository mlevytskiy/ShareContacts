package sharecontacts.com.sharecontacts;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 */
public class GMailSender extends javax.mail.Authenticator {

    public final static String HTML_TEMPLATE_START = "<!DOCTYPE html><html charset=UTF-8><head><title>Report</title><meta charset=\"utf-8\"/></head><body>";
    public final static String HTML_TEMPLATE_END = "</body></html>";

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        try{
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(buildEmailMessage(body).getBytes(), "text/html"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        }catch(Exception e){

        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

    private String buildEmailMessage(String userMessage) {
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(HTML_TEMPLATE_START);
        mailBody.append(userMessage);
        mailBody.append("<br>");
        mailBody.append("<b>Brend:</b>");
        mailBody.append(Build.BOARD);
        mailBody.append("<br>");
        mailBody.append("<b>Model:</b>");
        mailBody.append(Build.MODEL);
        mailBody.append("<br>");
        mailBody.append("<b>Version:</b>");
        mailBody.append(Build.VERSION.RELEASE);
        mailBody.append("<br>");
        mailBody.append("<b>Display:</b>");
        mailBody.append(displayInfo());
        mailBody.append("<br>");
        mailBody.append("<b>AppVersion:</b>");
        mailBody.append(BuildConfig.VERSION_NAME);
        mailBody.append("<br>");
        mailBody.append("</br>");
        mailBody.append(HTML_TEMPLATE_END);
        return mailBody.toString();
    }

    private String displayInfo() {
        double density = CustomApplication.instance.getResources().getDisplayMetrics().density;
        String screenResolution = getScreenResolution(CustomApplication.instance);
        String result;
       if (density < 1.0) {
           result = "LDPI";
       } else if (density < 1.5) {
           result = "MDPI";
       } else if (density < 2.0) {
           result = "HDPI";
       } else if (density < 3.0) {
           result = "XHDPI";
       } else if (density < 4.0) {
           result = "XXHDPI";
       } else {
           result ="XXXHDPI";
       }
       return result + screenResolution;
    }

    private static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "(" + width + "," + height + ")";
    }
}

