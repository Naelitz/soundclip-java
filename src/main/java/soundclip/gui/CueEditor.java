package soundclip.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.api.Cue;
import soundclip.api.Editable;
import soundclip.Utils;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Created by nathan on 7/2/15
 */
public class CueEditor extends BorderPane {
    private static final Logger log = LogManager.getLogger(CueEditor.class);

    @FXML private TabPane editorTabs;
    @FXML private Button cancelButton;
    @FXML private Button okButton;

    private HashMap<String,Node> tabs;
    private final Consumer<Boolean> callback;

    private volatile boolean result = false;

    public static boolean displayEditorFor(Cue c){
        Stage editorStage = new Stage();

        CueEditor editor = new CueEditor(response -> {
            editorStage.close();
        });
        editor.initTabs(c.buildEditorWidget());

        editorStage.setTitle("Cue Editor: " + c.getClass().getSimpleName());
        editorStage.setScene(new Scene(editor, 640, 480));
        editorStage.initModality(Modality.APPLICATION_MODAL);
        editorStage.showAndWait();

        return editor.result;
    }

    private CueEditor(Consumer<Boolean> callback){
        this.callback = callback;

        FXMLLoader fxmlLoader = Utils.load(this, "/ui/CueEditor.fxml");
    }

    public void initTabs(HashMap<String,Node> tabs){
        this.tabs = tabs;
        tabs.keySet().forEach(key -> {
            log.debug("Adding editor tab " + key);

            Node editor = tabs.get(key);

            AnchorPane root = new AnchorPane(editor);
            AnchorPane.setRightAnchor(editor, 13.0);
            AnchorPane.setTopAnchor(editor, 13.0);
            AnchorPane.setLeftAnchor(editor, 13.0);

            ScrollPane container = new ScrollPane(root);
            container.setFitToWidth(true);
            container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            editorTabs.getTabs().add(new Tab(key, container));
        });
    }

    @FXML
    private void onCancelClicked(ActionEvent click){
        log.debug("TODO: Cancel Editor Dialog");
        doClose(false);
    }

    @FXML
    private void onOkClicked(ActionEvent click){
        log.debug("TODO: Ok");
        doClose(true);
    }

    private void doClose(boolean success){
        result = success;

        tabs.values().forEach(tab -> {
            if(tab instanceof Editable){
                try{
                    ((Editable)tab).saveEdits(!success);
                }catch(Exception e){
                    log.error("Error updating cue properties: ", e);
                }
            }else{
                log.warn("An editor tab was registered that does not implement the Editable interface. " +
                        "There is no way to sync the edits to the clip after the editor has been closed. " +
                        "Contact the cue's developer");
            }
            if(tab instanceof Closeable){
                try {
                    ((Closeable)tab).close();
                } catch(IOException e) {
                    log.error("Error auto-closing tab: ", e);
                }
            }
        });

        callback.accept(success);
    }
}
