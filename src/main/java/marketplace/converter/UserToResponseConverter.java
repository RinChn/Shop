package marketplace.converter;

import marketplace.dto.UserDto;
import marketplace.entity.User;
import org.springframework.core.convert.converter.Converter;

public class UserToResponseConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User source) {
        return UserDto.builder()
                .email(source.getEmail())
                .build();
    }
}
