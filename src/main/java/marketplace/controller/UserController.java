package marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.UserAndOrdersDto;
import marketplace.dto.UserDto;
import marketplace.service.UserService;
import marketplace.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/registration")
    public UUID registration(@Valid @RequestBody UserDto newUser) {
        return userService.register(newUser);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/orders")
    public List<UserAndOrdersDto> getAllUsersAndHisOrders() {
        return userService.getUserAndOrders();
    }
}
