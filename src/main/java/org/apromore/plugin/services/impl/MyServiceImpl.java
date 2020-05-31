package org.apromore.plugin.services.impl;

import java.text.DateFormat;
import java.util.Date;

import org.apromore.plugin.services.MyService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * Service implementation.
 */
@Service("myService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyServiceImpl implements MyService {

    /**
     * Return current day.
     *
     * @param question question being asked
     * @return current day
     */
    public String ask(String question) {
        return DateFormat.getDateInstance().format(new Date());
    }
}