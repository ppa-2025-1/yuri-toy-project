package br.edu.ifrs.riogrande.tads.ppa.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.repository.ListCrudRepository;

import br.edu.ifrs.riogrande.tads.ppa.model.Role;

public interface RoleRepository extends ListCrudRepository<Role, Integer> {
    // This interface will automatically provide CRUD operations for the Role entity
    // You can add custom query methods here if needed

    Role findByName(String name);

    Set<Role> findByNameIn(Collection<String> names);

    boolean existsByName(String name);
    
}
