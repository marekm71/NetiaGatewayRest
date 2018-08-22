package pl.netia.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.netia.gateway.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
