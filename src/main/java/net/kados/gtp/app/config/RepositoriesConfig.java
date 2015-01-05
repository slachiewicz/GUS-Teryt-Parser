package net.kados.gtp.app.config;

import net.kados.gtp.app.repositories.City;
import net.kados.gtp.app.repositories.Commune;
import net.kados.gtp.app.repositories.District;
import net.kados.gtp.app.repositories.Precinct;
import net.kados.gtp.app.repositories.Province;
import net.kados.gtp.app.repositories.Street;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class RepositoriesConfig
{
    @Bean
    public Province province()
    {
        return new Province();
    }

    @Bean
    public District district()
    {
        return new District();
    }

    @Bean
    public Commune commune()
    {
        return new Commune();
    }

    @Bean
    public City city()
    {
        return new City();
    }

    @Bean
    public Precinct precinct()
    {
        return new Precinct();
    }
    
    @Bean
    public Street street()
    {
        return new Street();
    }
}