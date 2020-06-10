package org.apromore.plugin;

import java.util.List;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;

/**
 * Join View Model to show except of the Join data.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JoinExcerptViewModel {

    @Wire("#excerpt")
    private Grid excerpt;

    /**
     * Compose view for the join excerpt view model.
     *
     * @param view get the view of join
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        excerpt.setVisible(false);
    }

    /**
     * When the table clicks.
     *
     * @param resultsList Results results that is join result
     */
    @GlobalCommand
    public void onTableClick(@BindingParam("resultsList")
                                         List<List<String>> resultsList) {
        excerpt.setVisible(true);
        TableUtils.populateGrid(excerpt, resultsList);
    }

    /**
     * Get the excerpt of the joined data.
     *
     * @return Grid to show table
     */
    public Grid getExcerpt() {
        return excerpt;
    }
}
