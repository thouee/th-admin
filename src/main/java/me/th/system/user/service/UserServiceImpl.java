package me.th.system.user.service;

import lombok.RequiredArgsConstructor;
import me.th.share.exception.Checker;
import me.th.system.user.entity.User;
import me.th.system.user.repository.UserRepository;
import me.th.system.user.service.dto.UserDto;
import me.th.system.user.service.dto.UserLoginDto;
import me.th.system.user.service.dto.UserSignUpDto;
import me.th.system.user.service.mapstruct.UserLoginMapper;
import me.th.system.user.service.mapstruct.UserMapper;
import me.th.system.user.service.mapstruct.UserSignUpMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final UserLoginMapper userLoginMapper;
    private final UserSignUpMapper userSignUpMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id) {
        User user = checkExistById(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserSignUpDto userSignUp) {
        User entity = userSignUpMapper.toEntity(userSignUp);
        checkUnique(entity);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        if (StringUtils.isBlank(entity.getNickName())) {
            entity.setNickName("用户_" + entity.getUsername());
        }
        userRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User entity) {
        User old = checkExistById(entity.getId());
        checkUnique(entity);
        userRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        userRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserLoginDto getLoginData(String username) {
        User user = userRepository.findByUsername(username);
        Checker.ENTITY_NOT_FOUND_ERROR.isNull(user, "用户");
        return userLoginMapper.toDto(user);
    }

    /**
     * 检查用户信息是否唯一（用户名、手机号、邮箱）
     *
     * @param user -
     */
    private void checkUnique(User user) {
        User e0 = userRepository.findByUsername(user.getUsername());
        Checker.ENTITY_EXIST_ERROR.notNull(e0, "用户名已被使用");
        User e1 = userRepository.findByPhone(user.getPhone());
        Checker.ENTITY_EXIST_ERROR.notNull(e1, "手机号已被使用");
        User e2 = userRepository.findByEmail(user.getEmail());
        Checker.ENTITY_EXIST_ERROR.notNull(e2, "邮箱已被使用");
    }

    /**
     * 检查指定 id 的用户是否存在，并返回结果
     *
     * @param id -
     * @return User
     */
    private User checkExistById(long id) {
        User user = userRepository.findById(id).orElse(null);
        Checker.ENTITY_NOT_FOUND_ERROR.isNull(user, "用户");
        return user;
    }
}
