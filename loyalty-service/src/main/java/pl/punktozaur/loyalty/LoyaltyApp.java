package pl.punktozaur.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.punktozaur.config.EnablePunktozaurCommon;

@SpringBootApplication
@EnablePunktozaurCommon
public class LoyaltyApp {

    public static void main(String[] args) {
        SpringApplication.run(LoyaltyApp.class, args);
    }
}
