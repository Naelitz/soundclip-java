package soundclip.core;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.Utils;
import soundclip.annotation.Automatable;
import soundclip.annotation.CueMeta;
import soundclip.api.*;
import soundclip.gui.TimePicker;

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
@CueMeta(id="soundclip.core.audio", displayName="Audio Cue", author="Nathan Lowe", category=CueCategory.ACTION)
public class AudioCue extends Cue implements Fadeable, Resumable, ProgressProvider{
    private final Logger log = LogManager.getLogger(AudioCue.class);

    protected StringProperty source = new SimpleStringProperty("");

    @Automatable(min=0.0, max=1.0, name="Volume")
    protected DoubleProperty volume = new SimpleDoubleProperty(1.0);
    @Automatable(min=-1.0, max=1.0, name="Pan")
    protected DoubleProperty pan = new SimpleDoubleProperty(0.0);

    @Automatable(min=0, max=Double.MAX_VALUE, name="Fade In Time")
    protected ObjectProperty<Duration> fadeInTime = new SimpleObjectProperty<>(Duration.ZERO);
    @Automatable(min=0, max=Double.MAX_VALUE, name="Fade Out Time")
    protected ObjectProperty<Duration> fadeOutTime = new SimpleObjectProperty<>(Duration.ZERO);

    private Media info;
    private MediaPlayer backend;

    private class Editor extends GridPane implements Editable{

        private final AudioCue model;

        @FXML private TextField source;
        @FXML private Slider volume;
        @FXML private Slider pan;
        @FXML private TimePicker fadeIn;
        @FXML
        private TimePicker fadeOut;

        public Editor(AudioCue model){
            FXMLLoader fxmlLoader = Utils.load(this, "/ui/editor/Audio.fxml");

            this.model = model;
            source.setText(model.getSource());
            volume.setValue(model.getVolume());
            pan.setValue(model.getPan());
            fadeIn.setTime(model.getFadeInTime());
            fadeOut.setTime(model.getFadeOutTime());
        }

        @Override
        public void saveEdits(boolean cancelled) {
            //TODO: Validation
            if(!cancelled){
                model.setSource(source.getText());
                model.setVolume(volume.getValue());
                model.setPan(pan.getValue());
                model.setFadeInTime(fadeIn.getTime());
                model.setFadeOutTime(fadeOut.getTime());
            }
        }

        @FXML
        private void onFileSourceBrowseClicked(ActionEvent click){
            log.debug("TODO: Open File Browser");
        }
    }

    public AudioCue(){
        super();

        setupBackend();
        source.addListener(((observable, oldValue, newValue) -> {
            setupBackend();
        }));
    }

    private void setupBackend(){
        try{
            info = new Media(new File(source.getValue()).toURI().toString());
            actionDuration.bind(info.durationProperty());
            backend = new MediaPlayer(info);
            backend.setVolume(volume.getValue());
            backend.balanceProperty().bind(pan);
        }catch(MediaException ex){
            info = null;
            backend = null;
        }
    }

    public void pause(Duration fadeOut) {
        if(fadeOut.greaterThan(Duration.ZERO) && !fadeOut.equals(Duration.INDEFINITE)){
            fadeOutOver(fadeOut, (event -> backend.pause()));
        }else{
            backend.pause();
        }
    }

    public void resume(Duration fadeIn) {
        if(fadeIn.greaterThan(Duration.ZERO) && !fadeIn.equals(Duration.INDEFINITE)){
            fadeInOver(fadeIn, null);
        }

        backend.play();
    }

    @Override
    public HashMap<String, Node> buildEditorWidget() {
        HashMap<String, Node> widgets = super.buildEditorWidget();

        widgets.put("Audio", new Editor(this));

        return widgets;
    }

    @Override
    public ReadOnlyObjectProperty<Duration> progressProperty() {
        return backend.currentTimeProperty();
    }

    @Override
    public Duration getProgress() {
        return backend.getCurrentTime();
    }

    @Override
    protected void action(){
        if(fadeInTime.getValue().greaterThan(Duration.ZERO) && !fadeInTime.getValue().equals(Duration.INDEFINITE)){
            fadeInOver(fadeInTime.getValue(), null);
        }
        backend.play();
    }

    @Override
    public void stop() {
        if(fadeOutTime.getValue().greaterThan(Duration.ZERO) && !fadeOutTime.getValue().equals(Duration.INDEFINITE)){
            fadeOutOver(fadeOutTime.getValue(), (event -> backend.stop()));
        }
    }

    public void fadeInOver(Duration time, EventHandler<ActionEvent> callback) {
        backend.setVolume(0.0);
        Timeline t = new Timeline(new KeyFrame(time, new KeyValue(backend.volumeProperty(), volume.getValue())));
        t.setCycleCount(1);
        if(callback != null){
            t.setOnFinished(callback);
        }
        t.play();
    }

    public void fadeOutOver(Duration time, EventHandler<ActionEvent> callback) {
        Timeline t = new Timeline(new KeyFrame(time, new KeyValue(backend.volumeProperty(), 0.0)));
        t.setCycleCount(1);
        if(callback != null){
            t.setOnFinished(callback);
        }
        t.play();
    }

    public String getSource() {
        return source.get();
    }

    public StringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
    }

    public double getVolume() {
        return volume.get();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public double getPan() {
        return pan.get();
    }

    public DoubleProperty panProperty() {
        return pan;
    }

    public void setPan(double pan) {
        this.pan.set(pan);
    }

    public Duration getFadeInTime() {
        return fadeInTime.get();
    }

    public ObjectProperty<Duration> fadeInTimeProperty() {
        return fadeInTime;
    }

    public void setFadeInTime(Duration fadeInTime) {
        this.fadeInTime.set(fadeInTime);
    }

    public Duration getFadeOutTime() {
        return fadeOutTime.get();
    }

    public ObjectProperty<Duration> fadeOutTimeProperty() {
        return fadeOutTime;
    }

    public void setFadeOutTime(Duration fadeOutTime) {
        this.fadeOutTime.set(fadeOutTime);
    }

    @Override
    public boolean store() {
        return false;
    }

    @Override
    public boolean load() {
        return false;
    }
}
