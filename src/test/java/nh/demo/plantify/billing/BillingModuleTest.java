package nh.demo.plantify.billing;

import nh.demo.plantify.TestcontainersConfiguration;
import nh.demo.plantify.care.CareService;
import nh.demo.plantify.owner.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.assertj.core.api.Assertions.*;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(TestcontainersConfiguration.class)
class BillingModuleTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void add_usage_record_with_setup_fee_for_registered_plant() {

        // 🔎 Welche Module sind jetzt hochgefahren?
        // care ist keine direkte Abhängigkeit -> nicht da
        assertThat(context.getBeansOfType(CareService.class)).isEmpty();

        // owner IST direkte Abhängigkeit -> da
        assertThat(context.getBeansOfType(OwnerRepository.class)).isNotEmpty();
    }

    @Test
    void migrates_only_the_started_modules() {
        assertThat(tableExists("billing_schema", "usage_records")).isTrue();
        assertThat(tableExists("owner_schema", "owners")).isTrue();
        assertThat(tableExists("care_schema", "care_tasks")).isFalse();
    }

    private boolean tableExists(String schema, String table) {
        var count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_schema) = ? AND LOWER(table_name) = ?",
            Integer.class,
            schema,
            table
        );
        return count > 0;
    }

}
