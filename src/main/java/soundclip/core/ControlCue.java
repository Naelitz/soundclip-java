package soundclip.core;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.util.Duration;

import soundclip.annotation.CueMeta;
import soundclip.api.Cue;
import soundclip.api.CueCategory;
import soundclip.api.ProgressProvider;

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
@CueMeta(id="soundclip.core.control", displayName="Control Cue", author="Nathan Lowe", category=CueCategory.CONTROL)
public class ControlCue extends Cue implements ProgressProvider{
    @Override
    public void standBy() {

    }

    @Override
    protected void action() {

    }

    @Override
    public void stop() {

    }

    @Override
    public ReadOnlyObjectProperty<Duration> progressProperty() {
        return null;
    }

    @Override
    public Duration getProgress() {
        return null;
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
