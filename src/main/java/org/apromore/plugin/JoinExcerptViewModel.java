package org.apromore.plugin;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;

import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JoinExcerptViewModel {

    @Wire("#excerpt")
    private Grid excerpt;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @GlobalCommand
    public void onTableClick(@BindingParam("resultsList") List<List<String>> resultsList) {

        TableUtils.populateGrid(excerpt, resultsList);

    }

    public Grid getExcerpt() {
        return excerpt;
    }
}
