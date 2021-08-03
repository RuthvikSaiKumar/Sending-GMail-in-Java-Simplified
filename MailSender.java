public class MailSender {
    public static void main(String[] args) {
        try{
            GMail mail=new GMail("senderaddress@gmail.com","senderpassword","senderaddress@gmail.com","TO","recieveraddress1@gmail.com", "recieveraddress2@gmail.com");
            /*
            First argument is the senders E-mail for authentication
            Second argument is the senders E-mail password for authentication
            Third argument is the senders E-mail to send the mail
            Fourth argument is the type of recieving Message TO | CC | BCC  [ it is not case-sensitive ]
         
            From fifth argument it is the recievers' E-mail addresses [ it uses var-args for accepting multiple addresses [ please search for var-arg in Java if you don't know what it is ] ]
                You can either put a String array of the recievers' address or you can just continue to enter as arguments
            */
            mail.setSubject("multi mail test"); // to set the subject
            mail.addText("test send"); // to add the body text
            mail.addAttachment("/mailattachmenttest.txt"); // to add attachment
            mail.send(); // to send the mail
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
