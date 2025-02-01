package marketplace.service;

import marketplace.dto.UserAndOrdersDto;
import marketplace.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UUID register(UserDto newUser);
    List<UserDto> getAllUsers();
    List<UserAndOrdersDto> getUserAndOrders();
}
