package com.paymentgatewayservice.config;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of("System");
    }
}
