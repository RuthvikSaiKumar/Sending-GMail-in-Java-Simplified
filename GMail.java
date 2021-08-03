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
     * @throws MessagingException
     */
    public void setSubject(String subject)throws MessagingException{
        msg.setSubject(subject);
    }

    /**
     * @param s The Body text to be added in the E-mail
     * @throws MessagingException
     */
    public void addText(String s)throws MessagingException{
        MimeBodyPart mimeBodyPart=new MimeBodyPart();
        mimeBodyPart.setText(s);
        multipart.addBodyPart(mimeBodyPart);
    }

    /**
     * @param file The file to be attached in the E-mail in the form of File object
     * @throws MessagingException, IOException
     */
    public void addAttachment(File file) throws MessagingException, IOException{
        MimeBodyPart mimeBodyPart=new MimeBodyPart();
        mimeBodyPart.attachFile(file);
        multipart.addBodyPart(mimeBodyPart);
    }

    /**
     * @param fileAddress The absolute path of the file to be attached in the E-mail
     * @throws MessagingException, IOException
     */
    public void addAttachment(String fileAddress) throws MessagingException, IOException{
        MimeBodyPart mimeBodyPart=new MimeBodyPart();
        mimeBodyPart.attachFile(fileAddress);
        multipart.addBodyPart(mimeBodyPart);
    }

    /**
     * Method to send the message
     * @return true if the message is sent successfully or false if the message is not sent successfully
     * @throws MessagingException
     */
    public void send() throws MessagingException{
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
    }
}
