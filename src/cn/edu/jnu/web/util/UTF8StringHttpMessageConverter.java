package cn.edu.jnu.web.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

/**
 * SpringMVC��StringHttpMessageConverter�ཫ�ַ���ת����JSONʱ��ʹ�õ���ISO-8859-1���룬<br>
 * Ϊ�˷�ֹ������������ʹ���Զ����UTF8StringHttpMessageConverter�࣬����̳�StringHttpMessageConverter�ࡣ<br>
 * ����ǿ��ʹ��UTF-8�������ת����
 * @author HHT
 *
 */
public class UTF8StringHttpMessageConverter extends StringHttpMessageConverter {
	private static final MediaType mediaType = 
			new MediaType("text", "plain", Charset.forName("utf-8"));
	private boolean writeAcceptCharset = true;
	
	
	
	@Override
	protected MediaType getDefaultContentType(String t) throws IOException {
		return mediaType;
	}

	@Override
	protected List<Charset> getAcceptedCharsets() {
		return Arrays.asList(mediaType.getCharSet());
	}

	@Override
	protected Long getContentLength(String s, MediaType contentType) {
		// ǿ��ʹ��UTF-8����������ݳ��ȼ��㡣
		return (long) s.getBytes(mediaType.getCharSet()).length;
	}

	@Override
	protected String readInternal(Class<? extends String> clazz,
			HttpInputMessage inputMessage) throws IOException {
		// ǿ��ʹ��UTF-8�����ȡ�������ݡ�
		MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : mediaType.getCharSet();
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
	}

	@Override
	protected void writeInternal(String s, HttpOutputMessage outputMessage)
			throws IOException {
		// ǿ��ʹ��UTF-8���뷵����Ӧ���ݡ�
		outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		outputMessage.getHeaders().setContentType(mediaType);
		/*if(this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}*/
		Charset charset = mediaType.getCharSet();
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
	}

	@Override
	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}
	
	public boolean isWriteAcceptCharset() {
		return writeAcceptCharset;
	}

}
