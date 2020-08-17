package com.github.codingdebugallday.plugin.framework.exceptions;

/**
 * <p>
 * 自定义异常 PluginException
 * </p>
 *
 * @author isaac 2020/6/16 14:24
 * @since 1.0
 */
public class PluginException extends RuntimeException {

    public PluginException() {
        super();
    }

    public PluginException(String s) {
        super(s);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
