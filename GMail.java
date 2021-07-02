import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class GMail {
    final private String username;
    final private String password;
    final private String fromAddress;
    final private String recipientType;
    final private String[] toAddress;
    final private Properties properties;
    private Session session;
    final private MimeMessage msg;
    final private MimeMultipart multipart;

    /**
     *
     * @param username E-mail address of the the sender for authentication
     * @param password password of their sender's E-mail account for authentication
     * @param fromAddress The E-mail address of the sender
     * @param recipientType The type of sending the E-mail : TO | CC | BCC [this is not case-sensitive]
     * @param toAddress The list of the receivers E-mail addresses
     */
    public GMail(String username, String password, String fromAddress, String recipientType, String... toAddress) {
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.recipientType=recipientType;
        this.toAddress = toAddress;
        properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        authenticate();
        multipart=new MimeMultipart();
        msg=new MimeMessage(session);
    }
    private void authenticate(){
        session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
    }

    /**
     * @param subject The subject of the E-mail
     * Displays MessagingException on error
     */
    public void setSubject(String subject){
        try {
            msg.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param s The Body text to be added in the E-mail
     * Displays MessagingException on error
     */
    public void addText(String s){
        try {
            MimeBodyPart mimeBodyPart=new MimeBodyPart();
            mimeBodyPart.setText(s);
            multipart.addBodyPart(mimeBodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file The file to be attached in the E-mail in the form of File object
     * Displays MessagingException on error
     * Displays IOException if file not found
     */
    public void addAttachment(File file){
        try {
            MimeBodyPart mimeBodyPart=new MimeBodyPart();
            mimeBodyPart.attachFile(file);
            multipart.addBodyPart(mimeBodyPart);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileAddress The absolute path of the file to be attached in the E-mail
     * Displays MessagingException on error
     * Displays IOException if file not found
     */
    public void addAttachment(String fileAddress){
        try {
            MimeBodyPart mimeBodyPart=new MimeBodyPart();
            mimeBodyPart.attachFile(fileAddress);
            multipart.addBodyPart(mimeBodyPart);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send the message
     * @return true if the message is sent successfully or false if the message is not sent successfully
     */
    public boolean send(){
        try {
            msg.setFrom(new InternetAddress(fromAddress));
            Message.RecipientType type;
            switch (recipientType.toUpperCase()){
                case "TO": type=Message.RecipientType.TO; break;
                case "BCC": type=Message.RecipientType.BCC; break;
                case "CC": type=Message.RecipientType.CC;break;
                default: throw new IllegalStateException("Unexpected value: " + recipientType);
            }
            for(String to:toAddress)
                msg.addRecipient(type,new InternetAddress(to));
            MimeBodyPart mimeBodyPart=new MimeBodyPart();
            mimeBodyPart.setText("");
            multipart.addBodyPart(mimeBodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
