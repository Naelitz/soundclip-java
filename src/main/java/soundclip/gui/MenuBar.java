package soundclip.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.SoundClip;
import soundclip.CueFactory;
import soundclip.annotation.CueMeta;
import soundclip.api.Cue;
import soundclip.api.CueCategory;
import soundclip.core.CueStack;
import soundclip.exception.CueTypeNotRegisteredException;
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
 * Created by nathan on 6/12/15
 */
public class MenuBar extends ToolBar {

    private static final Logger log = LogManager.getLogger(MenuBar.class);

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private MenuButton addItem;

    public MenuBar(){
        FXMLLoader fxmlLoader = Utils.load(this, "/ui/widgets/MenuBar.fxml");

        SoundClip.instance().currentProjectProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(titleLabel.textProperty().isBound()){
                    titleLabel.textProperty().unbind();
                }
                titleLabel.textProperty().bind(newValue.nameProperty());

                if(subtitleLabel.textProperty().isBound()){
                    subtitleLabel.textProperty().unbind();
                }
                subtitleLabel.textProperty().bind(newValue.pathOnDiskProperty());
            }
        }));

        HashMap<CueCategory, ArrayList<Pair<String,String>>> menuItems = new HashMap<>();
        //TODO: Populate addItem button
        for(CueMeta meta : CueFactory.listAvailable()){
            if(!menuItems.containsKey(meta.category())){
                menuItems.put(meta.category(), new ArrayList<>());
            }
            menuItems.get(meta.category()).add(new Pair<>(meta.id(), meta.displayName()));
        }

        menuItems.keySet().stream().sorted().forEach(cueCategory -> {
            ArrayList<Pair<String,String>> types = menuItems.get(cueCategory);
            types.stream().sorted(
                    (left, right) -> left.getValue().compareToIgnoreCase(right.getValue())
            ).forEach(cue ->{
                MenuItem item = new MenuItem(cue.getValue());
                item.setId(cue.getKey());
                item.setOnAction(e -> onAddItem(cue.getKey()));

                addItem.getItems().add(item);
            });
            addItem.getItems().add(new SeparatorMenuItem());
        });

        MenuItem addCueList = new MenuItem("Cue List");
        addCueList.setId("soundclip.core.cuelist");
        addCueList.setOnAction(e -> onAddItem("soundclip.core.cuelist"));
        addItem.getItems().add(addCueList);

    }

    private void onAddItem(String id){
        if(id.equalsIgnoreCase("soundclip.core.cuelist")){
            log.debug("TODO: Add new cue list");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Cue List");
            dialog.setContentText("Cue List Name:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                CueStack stack = new CueStack();
                stack.setName(name);

                SoundClip.instance().getCurrentProject().add(stack);
            });
        }else{
            log.debug("TODO: Creating editor dialog for cue instance of " + id);

            try {
                Cue c = CueFactory.build(id);

                CueStack stack = SoundClip.instance().getPrimaryController().getSelectedCueStack();
                Cue selected = SoundClip.instance().getPrimaryController().getCurrentView().getSelected();
                if(selected != null){
                    c.setNumber(selected.getNumber()+1);
                }

                if(CueEditor.displayEditorFor(c)){
                    if(selected == null){
                        // Just append, nothing's selected
                        stack.add(c);
                    }else{
                        int index = stack.indexOf(selected);
                        if(index == stack.size() + 1){
                            // Selected cue is at the end, just append
                            stack.add(c);
                        }else{
                            // Sammich
                            stack.add(index + 1, c);
                        }
                    }
                }
            } catch(CueTypeNotRegisteredException | IllegalAccessException | InstantiationException e) {
                log.error("Critical Error: Unable to instantiate " + id, e);
            }
        }
    }

    @FXML
    protected void onOpenProjectClicked(ActionEvent click){
        log.debug("TODO: Open Project");
    }

    @FXML
    protected void onSaveProjectClicked(ActionEvent click){
        log.debug("TODO: Save Project");
    }

    @FXML
    protected void onNewProjectClicked(ActionEvent click){
        log.debug("TODO: New Project");
    }

    @FXML
    protected void onPanicClicked(ActionEvent click){
        log.debug("TODO: Panic");
    }

    @FXML
    protected void onToggleWorkspaceLock(ActionEvent click){
        log.debug("TODO: Toggle Workspace Lock");
    }

    @FXML
    protected void onMenuRenumberAction(ActionEvent click){
        log.debug("TODO: Renumber Current Cue List");
    }

    @FXML
    protected void onMenuRenameAction(ActionEvent click){
        log.debug("TODO: Rename Current Cue List");
    }

    @FXML
    protected void onMenuPropertiesAction(ActionEvent click){
        log.debug("TODO: Project Properties");
    }

    @FXML
    protected void onMenuAboutAction(ActionEvent click){
        AboutDialog about = new AboutDialog();

        Stage dialog = new Stage();
        dialog.setTitle("About soundclip");
        dialog.setScene(new Scene(about, 480, 320));
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

}
