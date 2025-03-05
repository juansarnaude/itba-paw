package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//@EnableWebMvc
@EnableTransactionManagement
@ComponentScan({ "ar.edu.itba.paw.webapp.controller" , "ar.edu.itba.paw.services" , "ar.edu.itba.paw.persistence" })
@Configuration
@PropertySource("classpath:/config/mail-config-production.properties")
public class WebConfig {

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Value("classpath:media.sql")
    private Resource mediaSql;

    @Value("classpath:featured_lists.sql")
    private Resource featuredLists;

    @Autowired
    private Environment environment;


//    @Bean
//    public ViewResolver viewResolver() {
//        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/jsp/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("/resources/");
//    }


    @Bean(name = "basePath")
    public String basePath() {
        return environment.getProperty("base_path");
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource ds){
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());
        return dsi;
    }

    private DatabasePopulator databasePopulator(){
        final ResourceDatabasePopulator dbp =  new ResourceDatabasePopulator();
        // TODO UNCOMMENT THIS
        //dbp.addScript(schemaSql);
        //dbp.addScript(mediaSql);
        //dbp.addScript(featuredLists);
        // UNTIL HERE 
        return dbp;
    }


    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(400000);
        multipartResolver.setDefaultEncoding("utf-8");
        return multipartResolver;
    }

    //Donde	 /paw 	al	final	de	la	url	indica	que	la	base	de	datos	se	llama	 paw y
    // el	username	y	la	password	son	aquellos	establecidos	en	la	creación	dela	base	de	datos	PostgreSQL

    //    ds.setUrl("jdbc:postgresql://localhost:5432/paw");
    //        ds.setUsername("postgres");
    //        ds.setUsername("postgres");
    //        ds.setPassword("admin");

    //    ds.setUrl("jdbc:postgresql://localhost/paw-2023b-06");
    //        ds.setUsername("paw-2023b-06");
    //        ds.setPassword("u5Ho8Kdaa");



    /*IMPORTANTE: CAMBIAR ESTA PARTE DEPENDIENDO SI QUIERO SUBIRLO A PAMPERO O NO!*/
    @Bean
    public DataSource dataSource(){
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl("jdbc:postgresql://localhost/paw-2023b-06");
        ds.setUsername("paw-2023b-06");
        ds.setPassword("u5Ho8Kdaa");
        return ds;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.ISO_8859_1.displayName());
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setPackagesToScan("ar.edu.itba.paw.models");
        factoryBean.setDataSource(dataSource());

        final HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaAdapter);

        final Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        factoryBean.setJpaProperties(properties);

       //TODO Remove before last push
//
//
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("format_sql", "true");

        return factoryBean;
    }
}