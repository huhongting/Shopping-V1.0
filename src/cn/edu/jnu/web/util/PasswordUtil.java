package cn.edu.jnu.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * ����������࣬����ԭʼ�����ַ���������ܺ���ַ��������ܲ����档<br>
 * ���ܷ�����MD5���ܡ�SHA���ܣ�Ĭ��ΪMD5���ܡ�<br>
 * MD5���ܣ����ɳ���Ϊ32��ʮ�������ַ�����<br>
 * SHA���ܣ����ɳ���Ϊ40��ʮ�������ַ�����
 * @author HHT
 *
 */
public class PasswordUtil {
	// �����㷨
	public static String MD5 = "md5";
	public static String SHA = "sha";

	/**
	 * Ĭ�ϼ����㷨������MD5���ܡ�
	 * @param input ��Ҫ���ܵ�ԭʼ�ַ�����
	 * @return ���ܺ��ʮ�������ַ�����
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncodeString(String input) 
			throws NoSuchAlgorithmException {
		return getEncodeString(MD5, input);
	}
	
	/**
	 * ����ָ���ļ����㷨(MD5��SHA)���м��ܡ�<br>
	 * ��ָ�������ڵļ����㷨�����׳��쳣��
	 * @param algorithm �����㷨(MD5��SHA)��
	 * @param input ��Ҫ���ܵ�ԭʼ�ַ�����
	 * @return ���ܺ��ʮ�������ַ�����
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncodeString(String algorithm, String input) 
			throws NoSuchAlgorithmException {
		byte[] bytes = MessageDigest.getInstance(algorithm).digest(input.getBytes());
		return bytes2String(bytes);
	}

	/**
	 * �����ֽ����������ַ��������ء�
	 * @param bytes ��Ҫת�����ַ������ֽ����顣
	 * @return ʮ�������ַ�����
	 */
	private static String bytes2String(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(byte2String(b));
		}
		return builder.toString();
	}

	// ʮ������������
	private static String[] digests = new String[] {
		"0", "1", "2", "3", "4", "5", "6", "7", "8",
		"9", "A", "B", "C", "D", "E", "F"
	};
	
	/**
	 * ���ݵ����ֽ�����ʮ���������ַ�����<br>
	 * �㷨��<br>
	 * ����byte����ת����int���ͣ������ֵС���������256��<br>
	 * Ȼ������������16����ȡ���ֽڵĵ�һ��4bit����������16ȡ������ȡ�ڶ���4bit��<br>
	 * �������4bit��ֵ��Ӧ��ʮ�������ַ���ƴ�Ӳ����ء�
	 * @param b ��Ҫת�����ַ������ֽڡ�
	 * @return ʮ�������ַ�����
	 */
	private static String byte2String(byte b) {
		int n = b;
		if(n < 0) n += 256;
		int i = n / 16;
		int j = n % 16;
		return digests[i] + digests[j];
	}
	

	/**
	 * �������ָ�����ȵ���������
	 * @param len
	 * @return
	 */
	public static String createPass(int len) {
		Random random = new Random();
		String pass = "";
		for(int i=0; i<len; i++) {
			pass += random.nextInt(10);
		}
		return pass;
	}
}