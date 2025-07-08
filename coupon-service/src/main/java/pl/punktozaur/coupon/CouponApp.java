package pl.punktozaur.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.punktozaur.config.EnablePunktozaurCommon;

@SpringBootApplication
@EnablePunktozaurCommon
public class CouponApp {

    public static void main(String[] args) {
        SpringApplication.run(CouponApp.class, args);
    }
}
