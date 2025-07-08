package pl.punktozaur.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.punktozaur.config.EnablePunktozaurCommon;

@SpringBootApplication
@EnablePunktozaurCommon
public class CustomerApp {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApp.class, args);
    }
}
