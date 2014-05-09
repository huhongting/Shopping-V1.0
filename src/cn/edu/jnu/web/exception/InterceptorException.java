package cn.edu.jnu.web.exception;

/**
 * 拦截器异常类，当拦截器捕获到用户非法操作是抛出此异常
 * @author HHT
 *
 */
public class InterceptorException extends Exception {
	private static final long serialVersionUID = 1L;

	public InterceptorException(String msg) {
		super(msg);
	}
}
