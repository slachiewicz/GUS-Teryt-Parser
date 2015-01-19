package net.kados.gtp.app.config;

import java.util.LinkedHashMap;
import net.kados.gtp.app.controllers.layouts.MainController;
import net.kados.gtp.app.controllers.modules.FormController;
import net.kados.gtp.app.controllers.modules.ParsingController;
import net.kados.gtp.app.controllers.modules.SummaryController;
import net.kados.gtp.app.libs.FileBrowser.Browser;
import net.kados.gtp.app.libs.FileBrowser.FileInfo;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.core.Database.Query;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
@Import({
    ParsersConfig.class,
    RepositoriesConfig.class
})
public class AppConfig
{
    @Bean
    public SceneManager sceneManager()
    {
        return new SceneManager();
    }

    @Bean
    public MainController mainController()
    {
        return new MainController();
    }

    @Bean
    public FormController formController()
    {
        return new FormController();
    }
    
    @Bean
    public SummaryController summaryController()
    {
        return new SummaryController();
    }

    @Bean
    public ParsingController parsingController()
    {
        return new ParsingController();
    }

    @Bean
    public Browser fileBrowser()
    {
        return new Browser();
    }

    @Bean
    public LinkedHashMap <String, FileInfo> bfis()
    {
        LinkedHashMap<String, FileInfo> bfis = new LinkedHashMap();

        for(Parser.Types type : Parser.Types.values())
        {
            bfis.put(type.toString(), null);
        }

        return bfis;
    }

    @Bean
    public Query query()
    {
        return new Query();
    }
}
