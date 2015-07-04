package soundclip.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.SoundClip;
import soundclip.Utils;
import soundclip.core.CueStack;

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
 * Created by nathan on 6/18/15
 */
public class PrimaryController extends BorderPane{

    private static final Logger log = LogManager.getLogger(PrimaryController.class);

    @FXML private TabPane cueStackContainer;

    public PrimaryController(){
        FXMLLoader fxmlLoader = Utils.load(this, "/ui/main.fxml");

        //TODO: Hide tab header if only one tab is present?

        SoundClip.instance().currentProjectProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                cueStackContainer.getTabs().removeAll();
                newValue.stream().forEach((stack) -> {
                    CueStackView view = new CueStackView();
                    view.setModel(stack);
                    cueStackContainer.getTabs().add(view);
                });
            }
        }));
    }

    public CueStackView getCurrentView(){
        return (CueStackView)cueStackContainer.getSelectionModel().getSelectedItem();
    }

    public CueStack getSelectedCueStack(){
        return getCurrentView().getModel();
    }


}
