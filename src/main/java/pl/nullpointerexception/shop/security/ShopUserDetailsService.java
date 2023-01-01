package pl.nullpointerexception.shop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nullpointerexception.shop.security.model.User;
import pl.nullpointerexception.shop.security.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        ShopUserDetails shopUserDetails = new ShopUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream()
                        .map(userRole -> (GrantedAuthority) () -> userRole.name())
                        .toList()
        );
        shopUserDetails.setId(user.getId());
        return shopUserDetails;
    }
}
