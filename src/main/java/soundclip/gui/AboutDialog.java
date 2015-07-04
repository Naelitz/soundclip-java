package soundclip.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.SoundClip;
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
 * Created by nathan on 6/17/15
 */
public class AboutDialog extends GridPane{
    private static final Logger log = LogManager.getLogger(AboutDialog.class);

    @FXML private Label versionLabel;

    public AboutDialog(){
        FXMLLoader fxmlLoader = Utils.load(this, "/ui/About.fxml");

        versionLabel.setText(SoundClip.VERSION.toString());
    }

    @FXML
    protected void onProjectLinkClicked(ActionEvent click){
        // For this to work on linux, one of the following must be in the path:
        // {"google-chrome", "firefox", "opera", "konqueror", "mozilla"}
        // This means that on arch, /usr/bin/google-chrome-stable must be symlinked to /usr/bin/google-chrome
        SoundClip.instance().getHostServices().showDocument("https://github.com/techwiz24/soundclip");
    }

    @FXML
    protected void onLicenseLinkClicked(ActionEvent click){
        // For this to work on linux, one of the following must be in the path:
        // {"google-chrome", "firefox", "opera", "konqueror", "mozilla"}
        // This means that on arch, /usr/bin/google-chrome-stable must be symlinked to /usr/bin/google-chrome
        SoundClip.instance().getHostServices().showDocument("https://www.gnu.org/licenses/gpl-3.0.html");
    }
}
