package ua.dp.maxym.demo8.order;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.messaging.ScopeAwareProvider;
import org.axonframework.spring.config.SpringAxonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Demo8AxonOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo8AxonOrderApplication.class, args);
    }

    @Bean
    public DeadlineManager deadlineManager(
            final ScopeAwareProvider scopeAwareProvider,
            final TransactionManager transactionManager) {
        return SimpleDeadlineManager.builder()
                                    .scopeAwareProvider(scopeAwareProvider)
                                    .transactionManager(transactionManager)
                                    .build();
    }

    @Bean
    public ScopeAwareProvider scopeAwareProvider(SpringAxonConfiguration configuration) {
        return new ConfigurationScopeAwareProvider(configuration.getObject());
    }

}
