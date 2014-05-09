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
 * 发送电子邮件工具类
 * @author HHT
 *
 */
public class MailSender {
    private static final String charset = "UTF-8";// 编码
    
    public static String MIMETYPE_TEXT_PLAIN = "text/plain";// 文本类型
    public static String MIMETYPE_TEXT_HTML = "text/html";// 文本类型
    
    private static final String defaultMimetype = MIMETYPE_TEXT_PLAIN;// 默认文本类型
    
    public static String hostName = "smtp.gmail.com";// 邮件发送服务器地址
    public static String userName = "sh.huht@gmail.com";// 邮件帐户
    public static String userPass = "hht@justin1021";// 帐户密码
    public static String fromUserMail = userName;// 邮件发送帐户
    
    /*public static void main(String[] args) 
    		throws Exception {
    	MailSender.send("huht@live.cn", "ishare.com", "IShare.com test mail!", null);
    }*/
    /**
     * 发送邮件
     * @param receiver 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
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
     * 发送邮件
     * @param receivers 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
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
     * 发送邮件
     * @param receivers 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param attachements 附件
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     * @throws MessagingException 
     * @throws AddressException 
     * @throws UnsupportedEncodingException 
     */
    public static void send(String[] receivers, String subject, 
    		String mailContent, File[] attachements, String mimetype) 
    				throws AddressException, MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", MailSender.hostName);//Smtp服务器地址
        props.put("mail.smtp.auth", "true");//需要校验
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.quitwait", false);
        
        //props.put("mail.smtp.ssl.trust", "*");// 不要对服务器进行SSL证书验证
        
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailSender.userName, MailSender.userPass);//登录用户名/密码
            }
        });
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(MailSender.userName));//发件人邮件

        InternetAddress[] toAddress = new InternetAddress[receivers.length];
        for (int i=0; i<receivers.length; i++) {
            toAddress[i] = new InternetAddress(receivers[i]);
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);//收件人邮件
        mimeMessage.setSubject(subject, charset);
        
        Multipart multipart = new MimeMultipart();
        //正文
        MimeBodyPart body = new MimeBodyPart();
       // body.setText(message, charset);不支持html
        body.setContent(mailContent, (mimetype!=null && !"".equals(mimetype) ? mimetype : defaultMimetype)+ ";charset="+ charset);
        multipart.addBodyPart(body);//发件内容
        //附件
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
