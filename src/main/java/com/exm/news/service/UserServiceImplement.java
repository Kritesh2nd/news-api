package com.exm.news.service;

import java.util.*;
import java.util.stream.Collectors;

import com.exm.news.customRepo.auth.LoginRepoImplement;
import com.exm.news.entity.auth.Login;
import com.exm.news.repository.auth.LoginRepository;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exm.news.dto.user.GeneralUserDto;
import com.exm.news.dto.user.LoginUserDto;
import com.exm.news.dto.user.RegisterUserDto;
import com.exm.news.dto.user.UpdateAuthorityDto;
import com.exm.news.dto.user.UpdateUserDto;
import com.exm.news.entity.app.Authority;
import com.exm.news.entity.app.User;
import com.exm.news.repository.app.AuthorityRepository;
import com.exm.news.repository.app.UserRepository;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.response.LoginResponse;
import com.exm.news.security.authentication.UserAuth;
import com.exm.news.security.provider.BasicAuthProvider;
import com.exm.news.service.interfaces.UserServiceInterfaces;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImplement implements UserServiceInterfaces {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BasicAuthProvider authProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginRepoImplement customLoginRepository;

    public List<LoginUserDto> findLoginByEmailCus(String email) {
        List<Login> loginEmails = customLoginRepository.findLoginSameEmail(email);

        List<LoginUserDto> listt = loginEmails.stream()
                .map(login -> {
                    return modelMapper.map(login, LoginUserDto.class);
                })
                .toList();
        return listt;
    }

    @Override
    public List<GeneralUserDto> getGeneralUserList() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream()
                .map(user -> {
                    GeneralUserDto generalUser = modelMapper.map(user, GeneralUserDto.class);
                    Login login = loginRepository.findLoginById(user.getUserId());
                    generalUser.setRole(setAuthorityToListAuthority(user.getAuthorities()));
                    generalUser.setEmail(login.getEmail());
                    return generalUser;
                })
                .toList();
    }

    @Override
    public GeneralUserDto getGeneralUserById(Long id) {
        User user = getUserById(id);
        GeneralUserDto generalUser = modelMapper.map(user, GeneralUserDto.class);
        generalUser.setRole(setAuthorityToListAuthority(user.getAuthorities()));
        return generalUser;
    }

    @Override
    public UserAuth authenticate(LoginUserDto input) {
        return null;
    }

    @Override
    public LoginResponse getUserToken() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        System.out.println("userAuth" + userAuth.toString());
        if (userAuth == null) {
            throw new NoSuchElementException("user not found, cannot make token");
        }

        String jwtToken = jwtService.generateToken(userAuth);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        List<String> userRoles = userAuth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return loginResponse;
    }

    @Override
    public GeneralUserDto getMe() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder
                .getContext().getAuthentication();
        if (userAuth == null) {
            throw new NoSuchElementException("User not found");
        }
        Login login = loginRepository.findLoginByEmail(userAuth.getEmail());
        GeneralUserDto generalUserDto = getGeneralUserById(login.getId());
        generalUserDto.setEmail(login.getEmail());
        return generalUserDto;
    }

    @Override
    public List<GeneralUserDto> generalUserDtoListByRoleEditor(){
        List<User> usersList = userRepository.findByAuthorityNameRequestEditor();
        return usersList.stream()
                .map(user -> {
                    GeneralUserDto generalUser = modelMapper.map(user, GeneralUserDto.class);
                    Login login = loginRepository.findLoginById(user.getUserId());
                    generalUser.setRole(setAuthorityToListAuthority(user.getAuthorities()));
                    generalUser.setEmail(login.getEmail());
                    return generalUser;
                })
                .toList();
    }

    @Override
    public List<GeneralUserDto> generalUserDtoListByRoleRequestEditor(){
        List<User> usersList = userRepository.findByAuthorityNameRequestEditor();
        return usersList.stream()
                .map(user -> {
                    GeneralUserDto generalUser = modelMapper.map(user, GeneralUserDto.class);
                    Login login = loginRepository.findLoginById(user.getUserId());
                    generalUser.setRole(setAuthorityToListAuthority(user.getAuthorities()));
                    generalUser.setEmail(login.getEmail());
                    return generalUser;
                })
                .toList();
    }

    @Transactional
    @Override
    public BasicResponseDto signup(RegisterUserDto newUserData) {
        System.out.println("signup repo");
        Login loginUserEmail = loginRepository.findLoginByEmail(newUserData.getEmail());
        if (loginUserEmail != null) {
            return new BasicResponseDto("User with this email already exits.", false);
        }

        Login login = modelMapper.map(newUserData, Login.class);
        login.setPassword(encodePassword(newUserData.getPassword()));
        loginRepository.save(login);

        User user = modelMapper.map(newUserData, User.class);

        Set<Authority> userAuthorities = new HashSet<Authority>();
        Authority userAuthority = authorityRepository.findAuthorityByName("reader");
        userAuthorities.add(userAuthority);
        user.setAuthorities(userAuthorities);

        userRepository.save(user);

        return new BasicResponseDto("New user added successfully.", true);

    }

    @Transactional
    @Override
    public BasicResponseDto updateUser(UpdateUserDto newUserData) {

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        User updateThisUser = getUserById(newUserData.getUserId());
        Login updateThisUserEmail = loginRepository.findLoginById(newUserData.getUserId());

        if (userAuth == null || updateThisUser == null) {
            throw new NoSuchElementException("User not found");
        }

        Login loggedInUser = loginRepository.findLoginByEmail(userAuth.getEmail());
        Login newUserEmail = loginRepository.findLoginByEmail(newUserData.getEmail());

//		User loggedInUser = userRepository.findUserByEmail(userAuth.getEmail());
//		User newUserEmail = userRepository.findUserByEmail(newUserData.getEmail());

        if (!loggedInUser.getId().equals(newUserData.getUserId())) {
            throw new AccessDeniedException("Cannot update this user");
        }

        if (newUserEmail == null) {

        } else if (!newUserData.getEmail().equals(loggedInUser.getEmail()) && newUserData.getEmail().equals(newUserEmail.getEmail())) {
            throw new DuplicateKeyException("Cannot update. This email already in use");
        }
        updateThisUserEmail.setEmail(newUserData.getEmail());
        updateThisUser.setFirstName(newUserData.getFirstName());
        updateThisUser.setLastName(newUserData.getLastName());
        updateThisUser.setUsername(newUserData.getUsername());
        userRepository.save(updateThisUser);
        loginRepository.save(updateThisUserEmail);

        return new BasicResponseDto("User updated successfully.", true);
    }

    @Override
    public BasicResponseDto updateUserPassword(Long id, String password) {
        Login userLogin = getLoginById(id);
        userLogin.setPassword(password);
        loginRepository.save(userLogin);

        return new BasicResponseDto("Password updated successfully.", true);
    }

    @Override
    public BasicResponseDto updateUserAuthority(UpdateAuthorityDto userAuthority) {
        try {
            User user = getUserById(userAuthority.getUserId());

            Set<Authority> userAuthorities = user.getAuthorities();
            Authority newUserAuthority = authorityRepository.findAuthorityByName("editor");
            Authority userAuthorityRequestEditor = authorityRepository.findAuthorityByName("request editor");
            userAuthorities.add(newUserAuthority);
            userAuthorities.remove(userAuthorityRequestEditor);
            user.setAuthorities(userAuthorities);
            userRepository.save(user);
        } catch (Exception e) {
            return new BasicResponseDto("Unable to update user authority for id: " + userAuthority.getUserId(), false);
        }
        return new BasicResponseDto("User authority updated successfully.", true);
    }

    @Override
    public BasicResponseDto requestEditorAccess() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        System.out.println("userAuth.toString(): "+userAuth);
        if (userAuth == null) {
            throw new NoSuchElementException("user not found.");
        }

        Login login = loginRepository.findLoginByEmail(userAuth.getEmail());
        Optional<User> user = Optional.ofNullable(userRepository.findUserById(login.getId()));
        if (user.isEmpty()) throw new NoSuchElementException("User not found");

        Set<Authority> userAuthorities = user.get().getAuthorities();
        Authority newUserAuthority = authorityRepository.findAuthorityByName("request editor");
        userAuthorities.add(newUserAuthority);
        user.get().setAuthorities(userAuthorities);

        userRepository.save(user.get());
        return new BasicResponseDto("Requested for editor role.", true);
    }

    @Override
    public BasicResponseDto deleteMyAccount() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();

        if (userAuth == null) {
            throw new NoSuchElementException("user not found, cannot delete the user account");
        }
        Login deleteUserLogin = loginRepository.findLoginByEmail(userAuth.getEmail());
        User deleteUser = userRepository.findUserById(deleteUserLogin.getId());
        loginRepository.delete(deleteUserLogin);
        userRepository.delete(deleteUser);

        return new BasicResponseDto("Your account is deleted successfully.", true);
    }

    @Override
    public BasicResponseDto removeUserAuthority(UpdateAuthorityDto userAuthority) {
        try {
            User user = getUserById(userAuthority.getUserId());

            Set<Authority> userAuthorities = user.getAuthorities();
            Authority newUserAuthority = authorityRepository.findAuthorityByName("editor");
            userAuthorities.remove(newUserAuthority);
            user.setAuthorities(userAuthorities);

            userRepository.save(user);
        } catch (Exception e) {
            return new BasicResponseDto("Unable to remove user authority for id: " + userAuthority.getUserId(), false);
        }
        return new BasicResponseDto("User authority removed successfully.", true);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public User getUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return user;
    }

    public Login getLoginById(Long id) {
        Login login = loginRepository.findLoginById(id);
        if (login == null) {
            throw new NoSuchElementException("User not found");
        }
        return login;
    }

    public List<String> setAuthorityToListAuthority(Set<Authority> setAuthority) {
        return setAuthority.stream()
                .map(Authority::getName)
                .collect(Collectors.toList());
    }
}
