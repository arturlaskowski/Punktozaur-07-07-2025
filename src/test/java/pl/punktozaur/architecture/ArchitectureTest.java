package pl.punktozaur.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import pl.punktozaur.coupon.web.CouponController;
import pl.punktozaur.loyalty.application.LoyaltyFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    public static final String BASE_PACKAGE = "pl.punktozaur";

    @Test
    void packagesShouldNotExist() {
        noClasses()
                .should().resideInAnyPackage(
                        "pl.punktozaur.domain",
                        "pl.punktozaur.infrastructure",
                        "pl.punktozaur.application")
                .because("After modularization, these packages may exist but only within specific modules")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void loyaltyModuleShouldNotDependOnCouponModule() {
        noClasses()
                .that()
                .resideInAPackage("..pl.punktozaur.loyalty..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..pl.punktozaur.coupon..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void couponModuleShouldNotDependOnLoyaltyModuleExcludeFacade() {
        noClasses()
                .that()
                .resideInAPackage("..pl.punktozaur.coupon..")
                .should()
                .dependOnClassesThat(originatesFromLoyaltyModuleAndIsNotFacade())
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    private static DescribedPredicate<JavaClass> originatesFromLoyaltyModuleAndIsNotFacade() {
        return new DescribedPredicate<>("originates from loyalty module and is not a facade") {
            @Override
            public boolean test(JavaClass input) {
                return input.getPackageName().startsWith("pl.punktozaur.loyalty")
                        && !input.isAssignableFrom(LoyaltyFacade.class);
            }
        };
    }

    @Test
    void couponControllerShouldBeIndependentOfCouponService() {
        var importedClasses = new ClassFileImporter().importPackages("pl.punktozaur.coupon");

        var rule = noClasses()
                .that().areAssignableTo(CouponController.class)
                .should().dependOnClassesThat().haveSimpleNameEndingWith("CouponService")
               .because("With the Mediator pattern, CouponController does not depend directly on CouponService.");

        rule.check(importedClasses);
    }
}