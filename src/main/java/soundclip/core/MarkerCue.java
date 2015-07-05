package soundclip.core;

import javafx.util.Duration;

import soundclip.annotation.CueMeta;
import soundclip.api.Cue;
import soundclip.api.CueCategory;

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
 * Created by nathan on 6/14/15
 *
 * A simple marker cue for notes. This cue does not do anything
 */
@CueMeta(id="soundclip.core.marker", displayName="Marker Cue", author="Nathan Lowe", category=CueCategory.OTHER)
public class MarkerCue extends Cue{

    public MarkerCue(){
        setActionDuration(Duration.ZERO);
    }

    @Override
    protected void action() {
        // Marker Cues don't do anything
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
