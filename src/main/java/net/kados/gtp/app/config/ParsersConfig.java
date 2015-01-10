package net.kados.gtp.app.config;

import java.util.LinkedHashMap;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.app.libs.Teryt.Subjects.Cities;
import net.kados.gtp.app.libs.Teryt.Subjects.Kinds;
import net.kados.gtp.app.libs.Teryt.Subjects.ParserDriver;
import net.kados.gtp.app.libs.Teryt.Subjects.Precincts;
import net.kados.gtp.app.libs.Teryt.Subjects.Streets;
import net.kados.gtp.app.libs.Teryt.Subjects.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class ParsersConfig
{
    //main parser process
    @Bean
    public Parser parser()
    {
        return new Parser();
    }

    //parsers drivers
    @Bean
    public Units units()
    {
        return new Units();
    }
    
    @Bean
    public Streets streets()
    {
        return new Streets();
    }
    
    @Bean
    public Cities cities()
    {
        return new Cities();
    }
    
    @Bean
    public Precincts precincts()
    {
        return new Precincts();
    }
    
    @Bean
    public Kinds kinds()
    {
        return new Kinds();
    }

    @Bean
    @Autowired
    public LinkedHashMap <String, ParserDriver> parsers(Units u, Streets s, Cities c, Precincts p, Kinds k)
    {
        LinkedHashMap<String, ParserDriver> hm = new LinkedHashMap();

        hm.put(u.getClass().getSimpleName(), u);
        hm.put(c.getClass().getSimpleName(), c);
        hm.put(p.getClass().getSimpleName(), p);
        hm.put(s.getClass().getSimpleName(), s);
        hm.put(k.getClass().getSimpleName(), k);

        return hm;
    }
}