package ee.golive.finants.config;

import ee.golive.finants.menu.Menu;
import ee.golive.finants.menu.MenuItem;
import ee.golive.finants.menu.MenuPreparer;
import ee.golive.finants.menu.MenuService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

@Configuration
@ComponentScan("ee.golive.finants")
@EnableWebMvc
@EnableJpaRepositories("ee.golive.finants.repository")
@Import({DbConfig.class})
@PropertySource("classpath:/config/${APP_ENV:development}.properties")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/WEB-INF/tiles.xml"});
        tilesConfigurer.setCheckRefresh(true);
        tilesConfigurer.setPreparerFactoryClass(org.springframework.web.servlet.view.tiles3.SpringBeanPreparerFactory.class);
        return tilesConfigurer;
    }

    @Bean
    public InternalResourceViewResolver InternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MenuService menuService() {
        MenuService menus = new MenuService();
        Menu sidebar = new Menu();
        sidebar.addItem(new MenuItem("P2P Income", "/p2p", "p2p"));
        sidebar.addItem(new MenuItem("Portfolio", "/portfolio", "portfolio"));
        sidebar.addItem(new MenuItem("Investing", "/investing", "investing"));
        sidebar.addItem(new MenuItem("Income", "/income", "income"));
        sidebar.addItem(new MenuItem("Networth", "/networth", "networth"));
        sidebar.addItem(new MenuItem("Expenses", "/expenses", "expenses"));

        sidebar.addItem(new MenuItem("Saving rate", "/savings", "savings"));

        menus.addMenu(sidebar, "sidebar");
        return menus;
    }

    @Bean
    public MenuPreparer menuPreparer() {
        MenuPreparer menuPreparer = new MenuPreparer();
        return menuPreparer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }
}
