package com.example.security.services;

import com.example.security.dtos.book.output.BookDTO;
import com.example.security.dtos.role.output.RoleDTO;
import com.example.security.dtos.user.input.*;
import com.example.security.dtos.user.output.*;
import com.example.security.entitys.*;
import com.example.security.exceptions.*;
import com.example.security.repositories.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import java.util.*;

import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ObjectMapper objectMapper;

    public UserDTO createUser(CreateUserDTO createUserDTO) {

        if (userRepo.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new ResourceExistsException("User already exists with email: " + createUserDTO.getEmail());
        }

        User user = objectMapper.convertValue(createUserDTO, User.class);
        Role defaultRole = roleRepo.findByName("ROLE_USER");
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setName("ROLE_USER");
            defaultRole.setDescription("User role");
            defaultRole.setUsers(new ArrayList<>());
            roleRepo.save(defaultRole);
        }
        user.enrollInCourse(defaultRole);
        User savedUser = userRepo.save(user);
        return convertToDto(savedUser);
    }


    public UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO) throws JsonMappingException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        objectMapper.updateValue(user, updateUserDTO);
        User updatedUser = userRepo.save(user);
        return convertToDto(updatedUser);
    }

    public UserDTO getUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return convertToDto(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        userRepo.deleteById(userId);
    }

    public UserWithRoleDTO addUserRole(Long userId, Long roleId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));

        user.enrollInCourse(role);
        userRepo.save(user);
        return convertToDtoWithRoles(user);
    }

    public UserWithRoleDTO removeUserRole(Long userId, Long roleId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        List<Role> rolesToRemove = user.getRoles().stream()
                .filter(r -> r.getId().equals(roleId))
                .peek(r -> r.getUsers().remove(user))
                .toList();

        if (rolesToRemove.isEmpty()) {
            throw new NotFoundException("Role not found with id: " + roleId + " for user with id: " + userId);
        }

        user.getRoles().removeAll(rolesToRemove);
        userRepo.save(user);
        return convertToDtoWithRoles(user);
    }

    public UserWithBooksDTO getUserWithBooks(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return convertToDtoWithBooks(user);
    }



    private UserDTO convertToDto(User user) {
        return objectMapper.convertValue(user, UserDTO.class);
    }

    UserWithRoleDTO convertToDtoWithRoles(User user) {
        List<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new ArrayList<>();
            user.setRoles(roles);
        }

        List<RoleDTO> roleDtoList = roles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        user.setRoles(null);

        UserWithRoleDTO userWithRoleDTO = objectMapper.convertValue(user, UserWithRoleDTO.class);
        userWithRoleDTO.setRoles(roleDtoList);

        return userWithRoleDTO;
    }


    private RoleDTO convertToDto(Role role) {
        return objectMapper.convertValue(role, RoleDTO.class);
    }

    private BookDTO convertToDto(Book book) {
        return objectMapper.convertValue(book, BookDTO.class);
    }

    private UserWithBooksDTO convertToDtoWithBooks(User user) {
        List<Book> books = user.getBooks();
        if (books == null) {
            books = new ArrayList<>();
            user.setBooks(books);
        }

        List<BookDTO> bookDtoList = books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        user.setBooks(null);

        UserWithBooksDTO userWithBooksDTO = objectMapper.convertValue(user, UserWithBooksDTO.class);
        userWithBooksDTO.setBooks(bookDtoList);

        return userWithBooksDTO;
    }


}
