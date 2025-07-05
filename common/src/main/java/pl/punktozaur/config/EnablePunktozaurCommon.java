package pl.punktozaur.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable Punktozaur common configurations.
 * This will automatically configure Feign clients
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        FeignConfig.class
})
@ComponentScan(basePackages = {"pl.punktozaur"})
public @interface EnablePunktozaurCommon {
}
