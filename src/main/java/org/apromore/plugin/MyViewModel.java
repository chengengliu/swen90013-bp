package org.apromore.plugin;

import org.apromore.plugin.services.MyService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Model for view.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MyViewModel {
    @WireVariable
    private MyService myService;
    private String answer;

    /**
     * Initialise.
     */
    @Init
    public void init() {
        answer = "?";
    }

    /**
     * Ask for the day.
     */
    @Command
    @NotifyChange("answer")
    public void cmd() {
        answer = myService.ask("What day is today?");
    }

    /**
     * Return answer.
     *
     * @return answer
     */
    public String getAnswer() {
        return answer;
    }
}
