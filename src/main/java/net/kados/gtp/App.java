package net.kados.gtp;

import javafx.application.Application;
import javafx.stage.Stage;
import net.kados.gtp.app.config.AppConfig;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App extends Application
{
    @Override
    public void start(Stage stage)
    {
        ApplicationContext ctx  = new AnnotationConfigApplicationContext(AppConfig.class);
        SceneManager man        = ctx.getBean(SceneManager.class);

        man.setStageCurrent(stage);
        man.moveToScene("main");
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
