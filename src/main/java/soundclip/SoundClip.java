package soundclip;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.zafarkhaja.semver.Version;

import soundclip.core.Project;
import soundclip.gui.PrimaryController;

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
public class SoundClip extends Application{
    private static final Logger log = LogManager.getLogger(SoundClip.class);

    public static final Version VERSION = Version.valueOf("0.1.0-alpha");

    private static SoundClip singleton;

    private Stage primaryStage;
    private PrimaryController primaryController;

    private ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);

    @FXML private TabPane cueStackContainer;

    public static SoundClip instance(){
        return singleton;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        singleton = this;
        this.primaryStage = primaryStage;

        log.info("Starting Up " + VERSION.toString());
        log.info("Discovered " + CueFactory.listAvailable().size() + " Cue Types");

        primaryController = new PrimaryController();

        setCurrentProject(new Project());
        getCurrentProject().setName("Hey Look, a project!");
        getCurrentProject().setPathOnDisk("/home/nathan/projects/soundclip-java/example");

        primaryStage.setScene(new Scene(primaryController, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args){
        Application.launch(args);
    }

    public Project getCurrentProject() {
        return currentProject.get();
    }

    public ObjectProperty<Project> currentProjectProperty() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject.set(currentProject);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public PrimaryController getPrimaryController(){
        return primaryController;
    }
}
