package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.net.URL;
import java.util.Properties;

@EnableAsync
@ComponentScan({"ar.edu.itba.paw.services"})
@Configuration
public class EmailConfig {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public JavaMailSender javaMailSender(){
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);

        javaMailSender.setUsername("pawmoovie@gmail.com");
        javaMailSender.setPassword("vkzg wmga omhq gead");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return javaMailSender;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver(){
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();

        classLoaderTemplateResolver.setPrefix("emailTemplates/");
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");

        return classLoaderTemplateResolver;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine(ITemplateResolver iTemplateResolver){
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();

        springTemplateEngine.setTemplateResolver(iTemplateResolver);
        springTemplateEngine.setTemplateEngineMessageSource(messageSource);

        return springTemplateEngine;
    }
}
