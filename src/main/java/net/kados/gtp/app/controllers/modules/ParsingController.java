package net.kados.gtp.app.controllers.modules;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.core.SceneManager.Interfaces.SceneManagerShowable;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

public class ParsingController implements Initializable, SceneManagerShowable
{
    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private Parser parser;

    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private TextArea progressPrompt;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.parser.setCallebleAtFinish(() -> {
            
            Dialogs.create().owner(sceneManager.getStageCurrent())
                    .title("Parsing zakończony")
                    .masthead("Sukces! Przeparsowano pliki.")
                    .showInformation();
        });
    }

    @Override
    public void showed()
    {
        this.parser.messageProperty().addListener((ObservableValue<? extends String> ov, String prv, String nxt) -> {
            this.progressPrompt.appendText(nxt + "\n");
        });

        this.parser.progressProperty().addListener((ObservableValue<? extends Number> ov, Number prv, Number nxt) -> {
            this.progressIndicator.setProgress((double) nxt);
        });
        
        this.parser.setCallebleAtFinish(() -> {
            //TODO
        });

        Thread thread = new Thread(this.parser); 
        thread.setDaemon(false);
        thread.start();
    }
}