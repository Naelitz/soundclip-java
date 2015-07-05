package soundclip.core;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.util.Duration;

import soundclip.SoundClip;
import soundclip.api.Cue;
import soundclip.api.Fadeable;
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
public class CueStack extends SimpleListProperty<Cue> implements Storable{

    private StringProperty name = new SimpleStringProperty("Untitled Cue Stack");

    public CueStack(){
        super(FXCollections.observableArrayList());
        MarkerCue test = new MarkerCue();
        test.setName("Test Cue");
        test.setNumber(1.0);
        test.setDescription("This is a test cue!");
        test.setPreWaitDelay(Duration.millis(253296));
        add(test);
    }

    public void panic(boolean hard){
        forEach((cue) -> {
            if (hard || !(cue instanceof Fadeable)) {
                cue.stop();
            } else {
                ((Fadeable) cue).fadeOutOver(SoundClip.instance().getCurrentProject().getPanicFadeTime(), (cb) -> {
                    // Just in case some cues aren't implemented correctly
                    cue.stop();
                });
            }
        });
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

    @Override
    public boolean store() {
        return false;
    }

    @Override
    public boolean load() {
        return false;
    }
}
