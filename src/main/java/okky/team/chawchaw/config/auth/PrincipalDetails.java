package okky.team.chawchaw.config.auth;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private UserEntity userEntity;

    public PrincipalDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole().getKey();
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() { return userEntity.getEmail(); }

    public UserEntity getUserEntity() { return userEntity; }
    public Long getId() { return userEntity.getId(); }
    public String getName() { return userEntity.getName(); }
    public String getSchool() { return userEntity.getSchool(); }
    public String getImageUrl() { return userEntity.getImageUrl(); }

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
