package pl.punktozaur.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    public static final String BASE_PACKAGE = "pl.punktozaur";
    private final JavaClasses classes = new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Test
    void checkModuleDependencies() {
        layeredArchitecture().consideringOnlyDependenciesInLayers()
                .layer("customer").definedBy("pl.punktozaur.customer..")
                .layer("loyalty").definedBy("pl.punktozaur.loyalty..")
                .layer("coupon").definedBy("pl.punktozaur.coupon..")
                .whereLayer("customer").mayNotAccessAnyLayer()
                .whereLayer("loyalty").mayNotAccessAnyLayer()
                .whereLayer("coupon").mayNotAccessAnyLayer()
                .check(classes);
    }
}