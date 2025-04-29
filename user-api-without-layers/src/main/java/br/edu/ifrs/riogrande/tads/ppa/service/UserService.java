package br.edu.ifrs.riogrande.tads.ppa.service;

import br.edu.ifrs.riogrande.tads.ppa.model.NewUser;
import br.edu.ifrs.riogrande.tads.ppa.model.Profile;
import br.edu.ifrs.riogrande.tads.ppa.model.Role;
import br.edu.ifrs.riogrande.tads.ppa.model.User;
import br.edu.ifrs.riogrande.tads.ppa.repository.RoleRepository;
import br.edu.ifrs.riogrande.tads.ppa.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Set<String> defaultRoles;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder,
            Set<String> defaultRoles) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultRoles = defaultRoles;
    }

    @Transactional
    public User createUser(NewUser newUser) {
        validateNewUser(newUser);

        User user = new User();
        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));

        Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(defaultRoles));
        Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
        
        if (additionalRoles.size() != newUser.roles().size()) {
            throw new IllegalArgumentException("Alguns papéis não existem");
        }
        
        roles.addAll(additionalRoles);
        user.setRoles(roles);

        Profile profile = new Profile();
        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }

    private void validateNewUser(NewUser newUser) {
        if (newUser.email() == null || newUser.password() == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }

        if (newUser.email().isEmpty() || newUser.password().isEmpty()) {
            throw new IllegalArgumentException("Email e senha não podem estar vazios");
        }

        if (!newUser.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email não é válido");
        }

        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }

        userRepository.findByEmail(newUser.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
                });

        if (newUser.handle() != null) {
            userRepository.findByHandle(newUser.handle())
                    .ifPresent(user -> {
                        throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
                    });
        }
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}