package com.ztiany.designpattern.observable.diff;

import com.ztiany.designpattern.observable.pull.Subject;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public interface Observer {
    void update(Subject subject, int type);
}
