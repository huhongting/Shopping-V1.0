package cn.edu.jnu.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 密码管理器类，根据原始输入字符串输出加密后的字符串，加密不可逆。<br>
 * 加密方法：MD5加密、SHA加密，默认为MD5加密。<br>
 * MD5加密：生成长度为32的十六进制字符串。<br>
 * SHA加密：生成长度为40的十六进制字符串。
 * @author HHT
 *
 */
public class PasswordUtil {
	// 加密算法
	public static String MD5 = "md5";
	public static String SHA = "sha";

	/**
	 * 默认加密算法，采用MD5加密。
	 * @param input 需要加密的原始字符串。
	 * @return 加密后的十六进制字符串。
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncodeString(String input) 
			throws NoSuchAlgorithmException {
		return getEncodeString(MD5, input);
	}
	
	/**
	 * 根据指定的加密算法(MD5、SHA)进行加密。<br>
	 * 若指定不存在的加密算法，则抛出异常。
	 * @param algorithm 加密算法(MD5、SHA)。
	 * @param input 需要加密的原始字符串。
	 * @return 加密后的十六进制字符串。
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncodeString(String algorithm, String input) 
			throws NoSuchAlgorithmException {
		byte[] bytes = MessageDigest.getInstance(algorithm).digest(input.getBytes());
		return bytes2String(bytes);
	}

	/**
	 * 根据字节数组生成字符串并返回。
	 * @param bytes 需要转换成字符串的字节数组。
	 * @return 十六进制字符串。
	 */
	private static String bytes2String(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(byte2String(b));
		}
		return builder.toString();
	}

	// 十六进制数数组
	private static String[] digests = new String[] {
		"0", "1", "2", "3", "4", "5", "6", "7", "8",
		"9", "A", "B", "C", "D", "E", "F"
	};
	
	/**
	 * 根据单个字节生成十六进制数字符串。<br>
	 * 算法：<br>
	 * 首先byte类型转换成int类型，如果数值小于零则加上256；<br>
	 * 然后将整形数除以16，获取该字节的第一个4bit；整形数对16取余数获取第二个4bit；<br>
	 * 最后将两个4bit数值对应的十六进制字符串拼接并返回。
	 * @param b 需要转换成字符串的字节。
	 * @return 十六进制字符串。
	 */
	private static String byte2String(byte b) {
		int n = b;
		if(n < 0) n += 256;
		int i = n / 16;
		int j = n % 16;
		return digests[i] + digests[j];
	}
	

	/**
	 * 随机生成指定长度的数字密码
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