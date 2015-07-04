package soundclip.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import soundclip.api.Cue;
import soundclip.api.ProgressProvider;
import soundclip.core.CueStack;
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
 * Created by nathan on 6/14/15
 */
public class CueStackView extends Tab{
    private static final Logger log = LogManager.getLogger(MenuBar.class);

    private ObjectProperty<CueStack> model = new SimpleObjectProperty<>(null);

    @FXML private TableView tableView;

    @FXML private TableColumn<Cue, Double> numberCell;
    @FXML private TableColumn<Cue, String> nameCell;
    @FXML private TableColumn<Cue, String> descriptionCell;
    @FXML private TableColumn<Cue, Duration> preWaitCell;
    @FXML private TableColumn<Cue, Duration> actionCell;
    @FXML private TableColumn<Cue, Duration> postWaitCell;

    public CueStackView(){
        FXMLLoader fxmlLoader = Utils.load(this, "/ui/widgets/CueStack.fxml");

        //Force Sizing:
        nameCell.prefWidthProperty().bind(tableView.widthProperty().subtract(80).multiply(.11));
        descriptionCell.prefWidthProperty().bind(tableView.widthProperty().subtract(80).multiply(.5));
        preWaitCell.prefWidthProperty().bind(tableView.widthProperty().subtract(80).multiply(.13));
        actionCell.prefWidthProperty().bind(tableView.widthProperty().subtract(80).multiply(.13));
        postWaitCell.prefWidthProperty().bind(tableView.widthProperty().subtract(80).multiply(.13));


        // Not all cells provide an action progress property
        actionCell.setCellValueFactory(cell -> {
            if (cell.getValue() instanceof ProgressProvider) {
                return ((ProgressProvider) cell.getValue()).progressProperty();
            } else {
                return new SimpleObjectProperty<>(Duration.ZERO);
            }
        });

        // Progress Bar Renderers
        preWaitCell.setCellFactory(column -> new TableCell<Cue, Duration>(){
            @Override
            protected void updateItem(Duration item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && !empty){
                    Cue model = getTableView().getItems().get(getIndex());
                    if(item.greaterThan(Duration.ZERO)){
                        setText(Utils.durationToString(item));

                        double percent = item.toMillis() / model.getActionDuration().toMillis();
                        String color = "-progress-green";
                        if(percent > 90){
                            color = "-progress-red";
                        }else if(percent > 75){
                            color = "-progress-yellow";
                        }

                        setStyle("-fx-background-color: linear-gradient(" +
                                "from 0% 100% to " + (percent) +"% 100%, " +
                                color + ", " + color + " 99.99%, transparent" +
                                ");");
                    }else{
                        setText(Utils.durationToString(model.getPreWaitDelay()));
                        setStyle("");
                    }

                }else{
                    setText("");
                }
            }
        });
        actionCell.setCellFactory(cell -> new TableCell<Cue, Duration>(){
            @Override
            protected void updateItem(Duration item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && !empty){
                    Cue model = getTableView().getItems().get(getIndex());

                    if(item.greaterThan(Duration.ZERO)){
                        setText(Utils.durationToString(item));

                        double percent = item.toMillis() / model.getActionDuration().toMillis();
                        String color = "-progress-green";
                        if(percent > 90){
                            color = "-progress-red";
                        }else if(percent > 75){
                            color = "-progress-yellow";
                        }

                        setStyle("-fx-background-color: linear-gradient(" +
                                "from 0% 100% to " + (percent) +"% 100%, " +
                                color + ", " + color + " 99.99%, transparent" +
                                ");");
                    }else{
                        setText(Utils.durationToString(model.getActionDuration()));
                        setStyle("");
                    }


                    //TODO: Style only if playing
                }else{
                    setText("");
                }
            }
        });
        postWaitCell.setCellFactory(column -> new TableCell<Cue, Duration>(){
            @Override
            protected void updateItem(Duration item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && !empty){
                    Cue model = getTableView().getItems().get(getIndex());

                    if(item.greaterThan(Duration.ZERO)){
                        setText(Utils.durationToString(item));

                        double percent = item.toMillis() / model.getPostWaitDelay().toMillis();
                        String color = "-progress-green";
                        if(percent > 90){
                            color = "-progress-red";
                        }else if(percent > 75){
                            color = "-progress-yellow";
                        }

                        setStyle("-fx-background-color: linear-gradient(" +
                                "from 0% 100% to " + (percent) +"% 100%, " +
                                color + ", " + color + " 99.99%, transparent" +
                                ");");
                    }else{
                        setText(Utils.durationToString(model.getPostWaitDelay()));
                        setStyle("");
                    }


                    //TODO: Style only if playing
                }else{
                    setText("");
                }
            }
        });

        model.addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                setText(newValue.getName());
                tableView.setItems(model.getValue());
            }
        }));

        tableView.setOnMouseClicked(event -> {
            Cue c = getSelected();
            if(event.getClickCount() == 2 && c != null){
                CueEditor.displayEditorFor(c);
            }
        });
    }

    public Cue getSelected(){
        return (Cue) tableView.getSelectionModel().getSelectedItem();
    }

    public CueStack getModel() {
        return model.get();
    }

    public ObjectProperty<CueStack> modelProperty() {
        return model;
    }

    public void setModel(CueStack model) {
        this.model.set(model);
    }
}
