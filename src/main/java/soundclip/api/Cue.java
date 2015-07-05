package soundclip.api;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.HashMap;

import soundclip.gui.TimePicker;
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
public abstract class Cue implements Storable {

    /** The number of the cue */
    protected DoubleProperty number = new SimpleDoubleProperty(-1.0);
    /** The Name of the cue */
    protected StringProperty name = new SimpleStringProperty("Untitled Cue");
    /** A brief description of the cue */
    protected StringProperty description = new SimpleStringProperty("");
    /** Notes related to the cue */
    protected StringProperty notes = new SimpleStringProperty("");

    /**
     * The cue that should be triggered next, null if none.
     * The specific cue implementation is responsible for triggering the cue when the current one is completed.
     * */
    protected ObjectProperty<Cue> autoFollowCue = new SimpleObjectProperty<>(null);

    /** The delay before triggering the cue */
    protected ObjectProperty<Duration> preWaitDelay = new SimpleObjectProperty<>(Duration.ZERO);
    /** The timer that counts down to the pre-wait action */
    protected ObjectProperty<Timeline> preWaitTimer = new SimpleObjectProperty<>();
    /** A proxy property to the currentTime property of the preWaitTimer */
    private ObjectProperty<Duration> preWaitProgress = new SimpleObjectProperty<>(Duration.UNKNOWN);

    /** The duration of the cue */
    protected ObjectProperty<Duration> actionDuration = new SimpleObjectProperty<>(Duration.UNKNOWN);

    /** The delay before triggering the autoFollow cue (if one is assigned) */
    protected ObjectProperty<Duration> postWaitDelay = new SimpleObjectProperty<>(Duration.ZERO);
    /** The timer that counts down to play the autofollow cue */
    protected ObjectProperty<Timeline> postWaitTimer = new SimpleObjectProperty<>();
    /** A proxy property to the currentTime proeprty of the postWaitTimer */
    private ObjectProperty<Duration> postWaitProgress = new SimpleObjectProperty<>(Duration.UNKNOWN);

    private class Editor extends GridPane implements Editable{
        private final Cue cue;

        @FXML private Spinner<Double> number;
        @FXML private TextField name;
        @FXML private TextField description;
        @FXML private TextArea notes;
        @FXML private TimePicker preWait;
        @FXML private TimePicker postWait;

        public Editor(Cue cue){
            this.cue = cue;

            FXMLLoader fxmlLoader = Utils.load(this, "/ui/editor/Cue.fxml");

            number.getValueFactory().setValue(cue.getNumber());
            name.setText(cue.getName());
            description.setText(cue.getDescription());
            notes.setText(cue.getDescription());
            preWait.setTime(cue.getPreWaitDelay());
            postWait.setTime(cue.getPostWaitDelay());

        }

        @Override
        public void saveEdits(boolean cancelled) {
            if(!cancelled){
                cue.setNumber(number.getValue());
                cue.setName(name.getText());
                cue.setDescription(description.getText());
                cue.setNotes(notes.getText());
                cue.setPreWaitDelay(preWait.getTime());
                cue.setPostWaitDelay(postWait.getTime());
            }
        }
    }

    public Cue(){
        preWaitDelay.addListener((observable, oldValue, newValue) -> {
            preWaitTimer.setValue(new Timeline(new KeyFrame(newValue, event -> go())));
            preWaitProgress.unbind();
            preWaitProgress.bind(preWaitTimer.getValue().currentTimeProperty());
        });

        postWaitDelay.addListener((observable, oldValue, newValue) -> {
            postWaitTimer.setValue(new Timeline(new KeyFrame(newValue, event -> {
                if(autoFollowCue.get() != null){
                    autoFollowCue.get().go();
                }
            })));
            postWaitProgress.unbind();
            postWaitProgress.bind(postWaitTimer.getValue().currentTimeProperty());
        });
    }

    /**
     * Builds the various widgets for the editor for this cue type. Always
     * chain up this method to the supertype and add your widget to the returned
     * map, otherwise you will overwrite the other widgets
     *
     * If any of the nodes implement the java.io.Closeable interface, close() will
     * be called when the editor is closed.
     *
     * @return a map of the various editor widgets, organized into categories
     */
    public HashMap<String,Node> buildEditorWidget(){
        HashMap<String,Node> result = new HashMap<>();

        result.put("General", new Editor(this));

        return result;
    }

    /**
     * Called when the cue is next up
     */
    public void standBy(){}

    /**
     * Triggers the main cue action after the pre-wait delay has expired
     */
    public void go(){
        if(getPreWaitDelay().greaterThan(Duration.ZERO)){
            preWaitTimer.get().play();
        }else{
            action();
        }
    }

    public ReadOnlyObjectProperty<Duration> getPreWaitProgressProperty(){
        return preWaitTimer.get().currentTimeProperty();
    }

    /**
     * Triggers the main cue action
     */
    protected abstract void action();

    /**
     * Stops the main cue action. Should not trigger the autoFollow cue
     */
    public abstract void stop();

    public ReadOnlyDoubleProperty getPostWaitProgressProperty(){
        return postWaitTimer.get().currentRateProperty();
    }

    public double getNumber() {
        return number.get();
    }

    public DoubleProperty numberProperty() {
        return number;
    }

    public void setNumber(double number) {
        this.number.set(number);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public Cue getAutoFollowCue() {
        return autoFollowCue.get();
    }

    public ObjectProperty<Cue> autoFollowCueProperty() {
        return autoFollowCue;
    }

    public void setAutoFollowCue(Cue autoFollowCue) {
        this.autoFollowCue.set(autoFollowCue);
    }

    public Duration getPreWaitDelay() {
        return preWaitDelay.get();
    }

    public ObjectProperty<Duration> preWaitDelayProperty() {
        return preWaitDelay;
    }

    public void setPreWaitDelay(Duration preWaitDelay) {
        this.preWaitDelay.set(preWaitDelay);
    }

    public Timeline getPreWaitTimer() {
        return preWaitTimer.get();
    }

    public ObjectProperty<Timeline> preWaitTimerProperty() {
        return preWaitTimer;
    }

    public Duration getPreWaitProgress() {
        return preWaitProgress.get();
    }

    public ObjectProperty<Duration> preWaitProgressProperty() {
        return preWaitProgress;
    }

    public Duration getActionDuration() {
        return actionDuration.get();
    }

    public ReadOnlyObjectProperty<Duration> actionDurationProperty() {
        return actionDuration;
    }

    public void setActionDuration(Duration actionDuration) {
        this.actionDuration.set(actionDuration);
    }

    public Duration getPostWaitDelay() {
        return postWaitDelay.get();
    }

    public ObjectProperty<Duration> postWaitDelayProperty() {
        return postWaitDelay;
    }

    public void setPostWaitDelay(Duration postWaitDelay) {
        this.postWaitDelay.set(postWaitDelay);
    }

    public Timeline getPostWaitTimer() {
        return postWaitTimer.get();
    }

    public ObjectProperty<Timeline> postWaitTimerProperty() {
        return postWaitTimer;
    }

    public Duration getPostWaitProgress() {
        return postWaitProgress.get();
    }

    public ObjectProperty<Duration> postWaitProgressProperty() {
        return postWaitProgress;
    }
}
