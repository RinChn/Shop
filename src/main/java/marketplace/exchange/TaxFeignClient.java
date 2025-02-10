package marketplace.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "taxClient", url = "${app.tax.url}" + "${app.tax.url-get-tin}")
public interface TaxFeignClient {
    @PostMapping
    List<String> getTinAboutUsers(@RequestBody List<String> usersEmail);
}
