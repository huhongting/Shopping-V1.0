package cn.edu.jnu.web.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * ���͵����ʼ�������
 * @author HHT
 *
 */
public class MailSender {
    private static final String charset = "UTF-8";// ����
    
    public static String MIMETYPE_TEXT_PLAIN = "text/plain";// �ı�����
    public static String MIMETYPE_TEXT_HTML = "text/html";// �ı�����
    
    private static final String defaultMimetype = MIMETYPE_TEXT_PLAIN;// Ĭ���ı�����
    
    public static String hostName = "smtp.gmail.com";// �ʼ����ͷ�������ַ
    public static String userName = "sh.huht@gmail.com";// �ʼ��ʻ�
    public static String userPass = "hht@justin1021";// �ʻ�����
    public static String fromUserMail = userName;// �ʼ������ʻ�
    
    /*public static void main(String[] args) 
    		throws Exception {
    	MailSender.send("huht@live.cn", "ishare.com", "IShare.com test mail!", null);
    }*/
    /**
     * �����ʼ�
     * @param receiver �ռ���
     * @param subject ����
     * @param mailContent �ʼ�����
     * @param mimetype �������� Ĭ��Ϊtext/plain,���Ҫ����HTML����,Ӧ����Ϊtext/html
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     * @throws AddressException 
     */
    public static void send(String receiver, String subject, 
    		String mailContent, String mimetype) 
    				throws AddressException, UnsupportedEncodingException, MessagingException {
    	send(new String[]{receiver}, subject, mailContent, mimetype);
    }
    /**
     * �����ʼ�
     * @param receivers �ռ���
     * @param subject ����
     * @param mailContent �ʼ�����
     * @param mimetype �������� Ĭ��Ϊtext/plain,���Ҫ����HTML����,Ӧ����Ϊtext/html
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     * @throws AddressException 
     */
    public static void send(String[] receivers, String subject, 
    		String mailContent, String mimetype) 
    				throws AddressException, UnsupportedEncodingException, MessagingException {
    	send(receivers, subject, mailContent, null, mimetype);
    }
    /**
     * �����ʼ�
     * @param receivers �ռ���
     * @param subject ����
     * @param mailContent �ʼ�����
     * @param attachements ����
     * @param mimetype �������� Ĭ��Ϊtext/plain,���Ҫ����HTML����,Ӧ����Ϊtext/html
     * @throws MessagingException 
     * @throws AddressException 
     * @throws UnsupportedEncodingException 
     */
    public static void send(String[] receivers, String subject, 
    		String mailContent, File[] attachements, String mimetype) 
    				throws AddressException, MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", MailSender.hostName);//Smtp��������ַ
        props.put("mail.smtp.auth", "true");//��ҪУ��
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.quitwait", false);
        
        //props.put("mail.smtp.ssl.trust", "*");// ��Ҫ�Է���������SSL֤����֤
        
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailSender.userName, MailSender.userPass);//��¼�û���/����
            }
        });
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(MailSender.userName));//�������ʼ�

        InternetAddress[] toAddress = new InternetAddress[receivers.length];
        for (int i=0; i<receivers.length; i++) {
            toAddress[i] = new InternetAddress(receivers[i]);
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);//�ռ����ʼ�
        mimeMessage.setSubject(subject, charset);
        
        Multipart multipart = new MimeMultipart();
        //����
        MimeBodyPart body = new MimeBodyPart();
       // body.setText(message, charset);��֧��html
        body.setContent(mailContent, (mimetype!=null && !"".equals(mimetype) ? mimetype : defaultMimetype)+ ";charset="+ charset);
        multipart.addBodyPart(body);//��������
        //����
        if(attachements!=null){
            for (File attachement : attachements) {
                MimeBodyPart attache = new MimeBodyPart();
               //ByteArrayDataSource bads = new ByteArrayDataSource(byte[],"application/x-any");
                attache.setDataHandler(new DataHandler(new FileDataSource(attachement)));
                String fileName = getLastName(attachement.getName());
                attache.setFileName(MimeUtility.encodeText(fileName, charset, null));
                multipart.addBodyPart(attache);
            }
        }
        mimeMessage.setContent(multipart);
       // SimpleDateFormat formcat = new SimpleDateFormat("yyyy-MM-dd");            
        mimeMessage.setSentDate(new Date());//formcat.parse("2010-5-23")
        Transport.send(mimeMessage);            
    }

    private static String getLastName(String fileName) {
        int pos = fileName.lastIndexOf("\\");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        pos = fileName.lastIndexOf("/");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }
}
