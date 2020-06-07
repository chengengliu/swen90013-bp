package org.apromore.plugin;

import java.util.List;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Window;

/**
 * Model for the expand window.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class InputExpandedExcerptViewModel {

    @Wire("#expandedWindow")
    private Window expandedWindow;

    @Wire("#inputExcerpt")
    private Grid inputExcerpt;

    /**
     * Compose view for the join excerpt view model.
     *
     * @param view get the view of join
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        expandedWindow.setVisible(false);
        expandedWindow.setTitle("Excerpt");
    }

    /**
     * When the table clicks.
     *
     * @param resultsList Results results that is join result
     * @param filename name of the file displayed
     */
    @GlobalCommand
    public void onExpandClick(@BindingParam("resultsList")
                                     List<List<String>> resultsList,
                              @BindingParam("filename")
                                      String filename) {
        expandedWindow.setTitle(filename);
        expandedWindow.setVisible(true);
        TableUtils.populateGrid(inputExcerpt, resultsList);
    }

}
