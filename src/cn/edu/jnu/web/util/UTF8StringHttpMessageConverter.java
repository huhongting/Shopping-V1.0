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
 * SpringMVC的StringHttpMessageConverter类将字符串转换成JSON时，使用的是ISO-8859-1编码，<br>
 * 为了防止中文乱码现象，使用自定义的UTF8StringHttpMessageConverter类，该类继承StringHttpMessageConverter类。<br>
 * 该类强制使用UTF-8编码进行转换。
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
		// 强制使用UTF-8编码进行内容长度计算。
		return (long) s.getBytes(mediaType.getCharSet()).length;
	}

	@Override
	protected String readInternal(Class<? extends String> clazz,
			HttpInputMessage inputMessage) throws IOException {
		// 强制使用UTF-8编码读取请求数据。
		MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : mediaType.getCharSet();
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
	}

	@Override
	protected void writeInternal(String s, HttpOutputMessage outputMessage)
			throws IOException {
		// 强制使用UTF-8编码返回响应数据。
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
