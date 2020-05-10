package org.apromore.plugin.services.impl;

import org.apromore.plugin.services.MyService;

import java.text.DateFormat;
import java.util.Date;

import org.apromore.plugin.services.MyService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	@RequestMapping(value="/", method = RequestMethod.GET)
	@ResponseBody
	public String getFileInquiry(HttpServletRequest request){
		return null;
	}
}
