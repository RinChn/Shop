package marketplace.converter.user;


import marketplace.dto.UserDto;
import marketplace.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto source) {
        return User.builder()
                .email(source.getEmail())
                .build();
    }
}
