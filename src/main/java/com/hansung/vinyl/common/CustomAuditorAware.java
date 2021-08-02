package com.hansung.vinyl.common;

import com.hansung.vinyl.account.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        Long accountId = null;

        if(principal instanceof UserDetails) {
            accountId = ((User) principal).getAccountId();
        }

        return Optional.ofNullable(accountId);
    }
}
