package org.apromore.plugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apromore.plugin.eventHandlers.ExpandIconDiv;
import org.apromore.plugin.eventHandlers.EyeIconDiv;
import org.apromore.plugin.services.FileHandlerService;
import org.apromore.plugin.services.Transaction;
import org.apromore.plugin.services.impl.IllegalFileTypeException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

/**
 * Model for the upload view.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileUploadViewModel {
    private static final String NULL_UPLOAD_MESSAGE = "No file is selected";
    private static final String ERROR = "Error";
    private static final Integer MAX_FILES_NUMBER = 10;
    private String textTable;
    private List<String> filenames = new ArrayList<>();

    @WireVariable
    private FileHandlerService fileHandlerService;

    @Wire("#inputFileList")
    private Div inputFileList;

    @Wire("#noFilesValue")
    private Label noFilesValue;

    @WireVariable
    private Transaction transactionService;

    /**
     * Initialise.
     */
    @Init
    public void init() { }

    /**
     * Convert the list result into String.
     *
     * @param tableVal snippet list values
     * @return converted string
     */
    private String createTableOutput(List<List<String>> tableVal) {

        String text = "";
        for (List<String> rowList : tableVal) {
            for (String rowVal : rowList) {
                text += rowVal + ", ";
            }
            text += "\n";
        }

        return text;
    }

    /**
     * Defines what actions must be taken after the view model is composed.
     *
     * @param view the view associated with this view model.
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * Describes the actions taken when a file is uploaded.
     */
    @Command("onFileUpload")
    public void onFileUpload() {
        Media[] medias = Fileupload.get(MAX_FILES_NUMBER);

        if (medias != null && medias.length > 0 && medias.length <= 10) {
            String returnMessage;

            try {
                returnMessage = fileHandlerService.writeFiles(medias);

                // If the file was written then load in impala and get snippet
                if (returnMessage.equals("Upload Success")) {
                    List<List<String>> resultsList = null;

                    for (int i = 0; i < medias.length; i++) {
                        Media media = medias[i];
                        try {

                            transactionService.addTable(media.getName());
                            resultsList = transactionService
                                    .getSnippet(media.getName(), 50);

                            // Create result String
                            textTable = createTableOutput(resultsList);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // Prevent the same file from appearing
                        // in the list twice
                        if (!filenames.contains(media.getName())) {
                            filenames.add(media.getName());

                            Map<String,Object> args =
                                new HashMap<String,Object>();
                            args.put("filenames", this.filenames);
                            BindUtils.postGlobalCommand(null, null,
                                    "newFileUpload", args);

                            noFilesValue.detach();
                            addFileToUIList(
                                FilenameUtils.removeExtension(media.getName()),
                                resultsList);
                        }
                    }
                }

                Messagebox.show(returnMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalFileTypeException e) {
                Messagebox.show(e.getMessage());
            }

        } else {
            Messagebox.show(
                NULL_UPLOAD_MESSAGE,
                ERROR,
                Messagebox.OK,
                Messagebox.ERROR);
        }
    }

    /**
     * Adds a file to the list in UI.
     *
     * @param filename    the name of the file to add to the UI.
     * @param resultsList snippet data.
     */
    private void addFileToUIList(
            String filename,
            List<List<String>> resultsList) {

        Hlayout fileListRow = new Hlayout();
        fileListRow.setHflex("1");
        fileListRow.setSclass("file-list-row");
        inputFileList.appendChild(fileListRow);

        // Show in joined excerpt
        HtmlNativeComponent tableIcon = new HtmlNativeComponent("i");
        tableIcon.setDynamicProperty("class", "z-icon-table");
        fileListRow.appendChild(tableIcon);

        // Create the label
        Label fileLabel = new Label(filename);
        fileLabel.setHflex("1");
        fileLabel.setSclass("file-label");
        fileListRow.appendChild(fileLabel);

        // Construct the popup box and contents
        Popup popupBox = new Popup();
        popupBox.setId(filename + "Snippet");
        fileListRow.appendChild(popupBox);

        ExpandIconDiv expandButton = new ExpandIconDiv(resultsList);
        expandButton.setId("expand" + filename + "Snippet");
        popupBox.appendChild(expandButton);

        HtmlNativeComponent expandIcon = new HtmlNativeComponent("i");
        expandIcon.setDynamicProperty("class", "z-icon-external-link-square");
        expandButton.appendChild(expandIcon);

        Div scrollArea = new Div();
        scrollArea.setSclass("input-table-scroll-area");
        scrollArea.setId("scrollArea" + filename);
        popupBox.appendChild(scrollArea);

        Grid inputGrid = new Grid();
        inputGrid.setId(filename + "Grid");
        inputGrid.setSclass("input-excerpt-grid-new");
        scrollArea.appendChild(inputGrid);

        // Add some spacing
        Space space = new Space();
        space.setSpacing("3px");
        fileListRow.appendChild(space);

        // Construct eye icon
        EyeIconDiv eyeButton = new EyeIconDiv(resultsList);
        eyeButton.setId("view" + filename + "Snippet");
        eyeButton.setSclass("eye-button");
        eyeButton.setPopupAttributes(popupBox, null, null, null, "toggle");
        fileListRow.appendChild(eyeButton);

        HtmlNativeComponent eyeIcon = new HtmlNativeComponent("i");
        eyeIcon.setDynamicProperty("class", "z-icon-eye");
        eyeButton.appendChild(eyeIcon);
    }

    /**
     * Get filenames.
     *
     * @return filenames
     */
    public List<String> getFilenames() {
        return filenames;
    }
}
