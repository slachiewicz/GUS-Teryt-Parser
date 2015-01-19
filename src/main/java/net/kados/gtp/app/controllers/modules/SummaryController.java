package net.kados.gtp.app.controllers.modules;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.annotation.Resource;
import net.kados.gtp.app.repositories.TableFillable;
import net.kados.gtp.core.SceneManager.Interfaces.SceneManagerShowable;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

public class SummaryController implements Initializable, SceneManagerShowable
{
    @FXML
    private TableView<SummaryRecord> summaryTable;
    
    @FXML
    private TableColumn<SummaryRecord, String> summaryTableKind;
    
    @FXML
    private TableColumn<SummaryRecord, String> summaryTableAmount;

    @Autowired
    private SceneManager sceneManager;
    
    @Resource(name="repos")
    private LinkedHashMap <String, TableFillable> repos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }

    @Override
    public void showed()
    {
        summaryTableKind.setCellValueFactory(new PropertyValueFactory<>("fieldKey"));
        summaryTableAmount.setCellValueFactory(new PropertyValueFactory<>("fieldVal"));
        
        ArrayList<SummaryRecord> list = new ArrayList<>();
        
        repos.forEach((String name, TableFillable repo) -> {
            try
            {
                System.out.println("DODAJE: " + name);
                list.add(new SummaryRecord(name, repo.getCount()));
            } 
            catch (SQLException ex)
            {
                Dialogs.create()
                        .owner(sceneManager.getStageCurrent())
                        .title("Bład SQL'a")
                        .showException(ex);
            }
        });

        this.summaryTable.setItems(FXCollections.observableArrayList(list));
    }
    
    public class SummaryRecord
    {
        private final SimpleStringProperty fieldKey;
        private final SimpleIntegerProperty fieldVal;

        SummaryRecord(String key, int val)
        {
            this.fieldKey = new SimpleStringProperty(key);
            this.fieldVal = new SimpleIntegerProperty(val);
        }

        public String getFieldKey()
        {
            return fieldKey.get();
        }

        public int getFieldVal()
        {
            return fieldVal.get();
        }
    }
}