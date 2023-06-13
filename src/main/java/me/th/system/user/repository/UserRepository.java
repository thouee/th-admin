package me.th.system.user.repository;

import me.th.system.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByUsername(String username);

    @Query(value = "select username from sys_user where id in (:ids)", nativeQuery = true)
    List<String> findUsernamesByIds(Set<Long> ids);

    @Modifying
    @Query(value = "update sys_user set password = ?2, pwd_reset_time = ?3 where username = ?1", nativeQuery = true)
    void updatePassword(String username, String password, LocalDateTime pwdResetTime);
}
