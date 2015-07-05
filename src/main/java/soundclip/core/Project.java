package soundclip.core;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.util.Duration;

import soundclip.api.Storable;

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
public class Project extends SimpleListProperty<CueStack> implements Storable{

    private StringProperty name = new SimpleStringProperty("Untitled Project");
    private StringProperty author = new SimpleStringProperty("");

    private ObjectProperty<Duration> panicFadeTime = new SimpleObjectProperty<>(Duration.millis(1500));

    private StringProperty pathOnDisk = new SimpleStringProperty("Not Saved");
    private long lastPanicTime = 0L;

    public Project(){
        super(FXCollections.observableArrayList());
        CueStack defaultCueStack = new CueStack();
        defaultCueStack.setName("Default Cue Stack");
        add(defaultCueStack);
    }

    public void panic(boolean hard){
        Duration delta = Duration.millis(System.currentTimeMillis() - lastPanicTime);

        forEach((cueStack) -> cueStack.panic(
                hard || getPanicFadeTime().subtract(delta).greaterThan(Duration.ZERO)
        ));

        lastPanicTime = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public Duration getPanicFadeTime() {
        return panicFadeTime.get();
    }

    public ObjectProperty<Duration> panicFadeTimeProperty() {
        return panicFadeTime;
    }

    public void setPanicFadeTime(Duration panicFadeTime) {
        this.panicFadeTime.set(panicFadeTime);
    }

    public String getPathOnDisk() {
        return pathOnDisk.get();
    }

    public StringProperty pathOnDiskProperty() {
        return pathOnDisk;
    }

    public void setPathOnDisk(String pathOnDisk) {
        this.pathOnDisk.set(pathOnDisk);
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
