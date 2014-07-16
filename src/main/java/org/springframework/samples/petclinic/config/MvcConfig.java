package org.springframework.samples.petclinic.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.web.PetTypeFormatter;
import org.springframework.samples.petclinic.web.VetsAtomView;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

@ComponentScan("org.springframework.samples.petclinic.web")
@EnableWebMvc
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private PetTypeFormatter petTypeFormatter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(petTypeFormatter);
    }

    @Bean
    public PetTypeFormatter petTypeFormatter(ClinicService clinicService) {
        return new PetTypeFormatter(clinicService);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // all resources inside folder src/main/webapp/resources are mapped so they can be refered to inside JSP files
        // (see header.jsp for more details)
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        // uses WebJars so Javascript and CSS libs can be declared as Maven dependencies (Bootstrap, jQuery...)
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.setOrder(-2);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("welcome");
        registry.setOrder(-1);
    }

    /*
     * serve static resources (*.html, ...) from src/main/webapp/
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Message source for this context, loaded from localized "messages_xx" files.
     * Files are stored inside src/main/resources
     * @return
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

    /**
     * This bean resolves specific types of exceptions to corresponding logical
     * view names for error views.
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver =
                new SimpleMappingExceptionResolver();
        simpleMappingExceptionResolver.setDefaultErrorView("exception");
        simpleMappingExceptionResolver.setWarnLogCategory("warn");
        return simpleMappingExceptionResolver;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true);
        configurer.ignoreAcceptHeader(true);
        configurer.defaultContentType(MediaType.TEXT_HTML);
        Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
        mediaTypes.put("html", MediaType.TEXT_HTML);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        mediaTypes.put("atom", MediaType.APPLICATION_ATOM_XML);
        configurer.mediaTypes(mediaTypes);
    }

    /**
     * The ContentNegotiatingViewResolver delegates to the InternalResourceViewResolver and BeanNameViewResolver,
     * and uses the requested media type (determined by the path extension) to pick a matching view.
     * When the media type is 'text/html', it will delegate to the InternalResourceViewResolver's JstlView,
     * otherwise to the BeanNameViewResolver.
     *
     * @param contentNegotiationManager
     * @return
     */
    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(
            ContentNegotiationManager contentNegotiationManager) {
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(contentNegotiationManager);
        viewResolver.setViewResolvers(Arrays.asList(
                internalResourceViewResolver(), beanNameViewResolver()));
        return viewResolver;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        return new BeanNameViewResolver();
    }
    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name="vets/vetList.atom")
    public VetsAtomView vetsAtomView() {
        return new VetsAtomView();
    }

    @Bean(name="vets/vetList.xml")
    public MarshallingView marshallingView(Marshaller marshaller) {
        MarshallingView marshallingView = new MarshallingView();
        marshallingView.setMarshaller(marshaller);
        return marshallingView;
    }

    @Bean(name="marshaller")
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Vets.class);
        return marshaller;
    }
}
