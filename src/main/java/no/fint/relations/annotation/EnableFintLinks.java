package no.fint.relations.annotation;

import no.fint.relations.config.FintLinksConfig;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.HalHateoasConfig;
import org.springframework.hateoas.config.HypermediaSupportBeanDefinitionRegistrarExt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({HypermediaSupportBeanDefinitionRegistrarExt.class, HalHateoasConfig.class, FintLinksConfig.class})
public @interface EnableFintLinks {
}