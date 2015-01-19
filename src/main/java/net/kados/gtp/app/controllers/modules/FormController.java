package net.kados.gtp.app.controllers.modules;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.kados.gtp.app.libs.FileBrowser.FileInfo;
import net.kados.gtp.app.libs.FileBrowser.Browser;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.core.Database.Query;
import net.kados.gtp.core.SceneManager.Interfaces.SceneManagerShowable;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

public class FormController implements Initializable, SceneManagerShowable
{
    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private Query query;

    @Autowired
    private Browser fileBrowser;

    @Autowired
    private Parser parser;

    @FXML
    private Button formBtnULIC;
    @FXML
    private Button formBtnSIMC;
    @FXML
    private Button formBtnTERC;
    @FXML
    private Button formBtnWMRODZ;
    @FXML
    private TextField formInpULIC;
    @FXML
    private TextField formInpSIMC;
    @FXML
    private TextField formInpTERC;
    @FXML
    private TextField formInpWMRODZ;
    @FXML
    private Button formBtnStart;
    @FXML
    private Button dbBtnCheck;
    @FXML
    private TextField dbInpLogin;
    @FXML
    private TextField dbInpPass;
    @FXML
    private TextField dbInpHost;
    @FXML
    private TextField dbInpPort;
    @FXML
    private TextField dbInpBase;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }

    @Override
    public void showed()
    {

    }

    @FXML
    private void formButtonULIC(ActionEvent event)
    {
        this.addFileToParser(formInpULIC, Parser.Types.ULIC, "Zaznacz plik ulic");
    }

    @FXML
    private void formButtonSIMC(ActionEvent event)
    {
        this.addFileToParser(formInpSIMC, Parser.Types.SIMC, "Zaznacz plik miast");
    }

    @FXML
    private void formButtonTERC(ActionEvent event)
    {
        this.addFileToParser(formInpTERC, Parser.Types.TERC, "Zaznacz plik podziału terytorialnego");
    }

    @FXML
    private void formButtonWMRODZ(ActionEvent event)
    {
        this.addFileToParser(formInpWMRODZ, Parser.Types.WMRODZ, "Zaznacz plik symboli");
    }

    @FXML
    private void formButtonStart(ActionEvent event)
    {
        //jest gotowe?
        if(this.parser.isReady() && this.checkDBConection())
        {
            sceneManager.nestPartInScene("parsing");
        }
        else
        {
            Dialogs.create().owner(sceneManager.getStageCurrent())
                    .title("Nie można rozpocząć!")
                    .masthead("Brakuje wszystkich danych.")
                    .showWarning();
        }
    }

    protected void addFileToParser(TextField inp, Parser.Types patt, String title)
    {
        FileInfo promptFile  = fileBrowser.promptForFile("PLIK: " + patt, patt + ".xml", title);

        if(promptFile == null) return;

        inp.setText(promptFile.getFile().getPath());

        this.parser.setFile(patt, promptFile);
    }

    @FXML
    private void dbBtnCheck(ActionEvent event)
    {
       this.checkDBConection();
    }
    
    private boolean checkDBConection()
    {
        boolean state = false;
        
        try
        {
            Connection connection = this.query.setConnectionData(
                    dbInpHost.getText().trim(),
                    dbInpPort.getText().trim(),
                    dbInpBase.getText().trim(),
                    dbInpLogin.getText().trim(),
                    dbInpPass.getText().trim()
            ).getConn();

            if(connection.isValid(1))
            {
                state = true;
                
                Dialogs.create().owner(sceneManager.getStageCurrent())
                    .title("Połączono poprawnie")
                    .masthead("Sukces! Połączenie z bazą danych zakończyło się sukcesem.")
                    .showInformation();
            }
        }
        catch (SQLException ex)
        {
            Dialogs.create()
                    .owner(sceneManager.getStageCurrent())
                    .title("Bład SQL'a")
                    .showException(ex);
        }

        return state;
    }
}