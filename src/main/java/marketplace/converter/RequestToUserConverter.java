package marketplace.converter;


import marketplace.dto.UserDto;
import marketplace.entity.User;
import org.springframework.core.convert.converter.Converter;

public class RequestToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto source) {
        return User.builder()
                .email(source.getEmail())
                .build();
    }
}
