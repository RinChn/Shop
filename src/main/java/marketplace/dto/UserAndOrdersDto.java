package marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import marketplace.controller.response.OrderResponse;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserAndOrdersDto {
    String email;
    List<OrderResponse> orders;
}
