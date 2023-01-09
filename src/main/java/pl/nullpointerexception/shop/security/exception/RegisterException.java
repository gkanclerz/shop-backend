package pl.nullpointerexception.shop.security.exception;

import pl.nullpointerexception.shop.common.exception.BusinessException;

public class RegisterException extends BusinessException {
    public RegisterException(String msg) {
        super(msg);
    }
}
