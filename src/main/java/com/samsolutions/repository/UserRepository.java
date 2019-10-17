package com.samsolutions.repository;

import com.samsolutions.entity.Health;
import com.samsolutions.entity.Role;
import com.samsolutions.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The User repository provides ready-made methods for working with user table.
 *
 * @author Vladislav Brazovskij <u.brazouski@sam-solutions.com>
 * @package com.samsolutions.repository
 * @link http ://sam-solutions.com/
 * @copyright 2019 SaM
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Method for find user by username.
     *
     * @param username username to be set.
     * @return User
     */
    @Query(value = "select u from User u inner join u.roles where u.username = ?1")
    User findByUsername(String username);

    Long countUsersByRoles(Role role);

    Page<User> findByRolesIs(Role roles, Pageable pageable);

    List<User> findAllByHealthIsNullAndRolesIs(Role role);

    List<User> getAllByRolesIs(Role role);

    List<User> findUsersByIdIn(List<Long> ids);

    List<User> findByRolesInOrderById(List<Role> roles);

    @Query(value = "select u from User u inner join u.roles where u.id = ?1")
    User getOneWithRoles(Long id);

    @Query(value = "select u from User u inner join u.roles inner join u.health inner join u.doctorTicket inner join u.patientTicket where u.id = ?1")
    User fetchOne(Long id);

    @Query(value = "select u from User u inner join u.health where u.id = ?1")
    User getOneWithHealth(Long id);
}
