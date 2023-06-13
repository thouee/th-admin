package me.th.system.user.service;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import me.th.share.common.PageData;
import me.th.share.exception.Checker;
import me.th.share.query.QueryHelper;
import me.th.share.util.EntityUtils;
import me.th.share.util.PageUtils;
import me.th.share.util.SecurityUtils;
import me.th.system.auth.service.OnlineUserService;
import me.th.system.auth.service.UserCacheManager;
import me.th.system.auth.service.dto.JwtUserDto;
import me.th.system.user.entity.User;
import me.th.system.user.repository.UserRepository;
import me.th.system.user.service.dto.UserAddDto;
import me.th.system.user.service.dto.UserDto;
import me.th.system.user.service.dto.UserLoginDto;
import me.th.system.user.service.dto.UserSignUpDto;
import me.th.system.user.service.dto.UserUpdatePasswordDto;
import me.th.system.user.service.mapstruct.UserAddMapper;
import me.th.system.user.service.mapstruct.UserLoginMapper;
import me.th.system.user.service.mapstruct.UserMapper;
import me.th.system.user.service.query.UserQueryCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final OnlineUserService onlineUserService;
    private final UserCacheManager userCacheManager;

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final UserLoginMapper userLoginMapper;
    private final UserAddMapper userAddMapper;

    @Override
    public PageData<UserDto> queryAll(UserQueryCriteria criteria) {
        Pageable pageable = QueryHelper.getPageable(criteria);
        Page<User> page = userRepository.findAll((root, query, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder), pageable);
        Page<UserDto> map = page.map(userMapper::toDto);
        return PageUtils.toPageData(map);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id) {
        User user = checkExistById(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserSignUpDto userSignUp) {
        // if 两次输入密码不相同
        Checker.TWICE_PASSWORD_NOT_EQUAL.isFalse(userSignUp.getPassword().equals(userSignUp.getCheckPassword()));

        User entity = new User();
        entity.setUsername(userSignUp.getUsername());
        checkUnique(entity);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setNickName("用户_" + entity.getUsername());
        userRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserAddDto userAdd) {
        User entity = userAddMapper.toEntity(userAdd);
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
        User merge = EntityUtils.merge(old, entity);
        // 如果用户被禁用，强制退出
        if (!merge.getEnabled()) {
            onlineUserService.kickOutByUsername(merge.getUsername());
        }
        userRepository.save(entity);
        // 清除缓存
        cleanLoginUserCache(merge.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserUpdatePasswordDto userUpdatePassword) {
        JwtUserDto currentUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        // if 用户未登录
        Checker.AUTH_NOT_FOUND.isNull(currentUser);
        // if 原密码输入错误
        boolean matches = passwordEncoder.matches(userUpdatePassword.getOldPassword(), currentUser.getPassword());
        Checker.PASSWORD_NOT_INCORRECT.isFalse(matches);
        // if 两次输入密码不相同
        Checker.TWICE_PASSWORD_NOT_EQUAL.isFalse(userUpdatePassword.getNewPassword().equals(userUpdatePassword.getCheckPassword()));

        String encodePassword = passwordEncoder.encode(userUpdatePassword.getNewPassword());
        userRepository.updatePassword(currentUser.getUsername(), encodePassword, LocalDateTime.now());
        // 清除缓存
        cleanLoginUserCache(currentUser.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            // 排除其中为 null 的非法数据
            Set<Long> fIds = ids.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            userRepository.deleteAllByIdInBatch(fIds);
            List<String> usernames = userRepository.findUsernamesByIds(fIds);
            for (String username : usernames) {
                // 清除缓存
                cleanLoginUserCache(username);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        delete(Set.of(id));
    }

    @Override
    public UserLoginDto getLoginData(String username) {
        User user = userRepository.findByUsername(username);
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
        if (user.getPhone() != null) {
            User e1 = userRepository.findByPhone(user.getPhone());
            Checker.ENTITY_EXIST_ERROR.notNull(e1, "手机号已被使用");
        }
        if (user.getEmail() != null) {
            User e2 = userRepository.findByEmail(user.getEmail());
            Checker.ENTITY_EXIST_ERROR.notNull(e2, "邮箱已被使用");
        }
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

    /**
     * 清理登录用户缓存
     *
     * @param username -
     */
    private void cleanLoginUserCache(String username) {
        userCacheManager.cleanUserCache(username);
    }
}
