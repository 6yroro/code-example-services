package com.test.aop;

import com.test.database.repository.DataRepository;
import com.test.services.AuditService;
import com.test.services.DataService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Alexander Zubkov
 */
@TestConfiguration
@EnableAspectJAutoProxy
public class AuditAspectTestConf {

    @Bean
    public DataService dataService(DataRepository dataRepository) {
        return new DataService(dataRepository);
    }

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }

}
