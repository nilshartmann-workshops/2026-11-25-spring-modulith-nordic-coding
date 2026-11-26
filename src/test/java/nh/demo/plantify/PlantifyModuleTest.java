package nh.demo.plantify;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.core.Violation;
import org.springframework.modulith.docs.Documenter;

import java.util.function.Predicate;

public class PlantifyModuleTest {

    @Test
    void test_architektur() {
        var modules = ApplicationModules
            .of(PlantifyApplication.class);

        modules.verify();
//        modules.detectViolations()
//            .filter(Predicate.not(this::isPlantCareModuleCycle))
//            .throwIfPresent();
    }

    private boolean isPlantCareModuleCycle(Violation violation) {
        return violation.hasMessageContaining("Cycle detected")
               && violation.hasMessageContaining("Slice plant")
               && violation.hasMessageContaining("Slice care");
    }

    @Test
    void architektur_doku() {
        var modules = ApplicationModules
            .of(PlantifyApplication.class);

        new Documenter(modules)
            .writeDocumentation();
    }

}
