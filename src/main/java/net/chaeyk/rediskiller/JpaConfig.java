package net.chaeyk.rediskiller;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = MemberRepository.class)
public class JpaConfig {
}
