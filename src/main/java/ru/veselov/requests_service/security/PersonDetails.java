package ru.veselov.requests_service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.veselov.requests_service.models.Person;
import java.util.Collection;
import java.util.List;

public class PersonDetails implements UserDetails {
    private final Person person;
    private final List<GrantedAuthority> authorities;

    public PersonDetails(Person person, List<GrantedAuthority> authorities) {
        this.person = person;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
