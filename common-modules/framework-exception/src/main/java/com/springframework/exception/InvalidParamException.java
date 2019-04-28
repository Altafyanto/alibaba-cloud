package com.springframework.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author summer
 * 2018/8/27
 */
@AllArgsConstructor
public class InvalidParamException extends BaseException {


    @Override
    public BaseException get() {
        return this;
    }
}
