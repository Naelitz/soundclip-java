package soundclip;

import javafx.fxml.FXMLLoader;
import javafx.util.Duration;

import java.io.IOException;

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
 * Created by nathan on 6/30/15
 */
public class Utils {

    public static String durationToString(Duration duration){
        if(duration.lessThanOrEqualTo(Duration.ZERO)){
            return "00:00.000";
        }else if(duration.equals(Duration.UNKNOWN)){
            return "UNKNOWN";
        }else{
            double millis = duration.toMillis();

            int minutes = (int)(millis / 60000);
            millis %= 60000;

            double seconds =(millis / 1000);
            return String.format("%02d:%5.3f", minutes, seconds);
        }
    }

    public static FXMLLoader load(Object controller, String fxml){
        FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(fxml));

        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return fxmlLoader;
    }

}
