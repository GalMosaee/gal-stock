package gal.mosaee.galstock;

import gal.mosaee.galstock.model.Item;
import gal.mosaee.galstock.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class GalStockApplication {
	public static void main(String[] args) {
		SpringApplication.run(GalStockApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ItemRepository repository){
		return args -> {
			repository.save(new Item("Glasses", 5, "abcd"));
			repository.save(new Item("Doll", 2, "abce"));
			repository.save(new Item("Bicycle", 3, "abcf"));
			repository.save(new Item("Pants", 4, "abcg"));
			repository.save(new Item("Picture", 1, "abch"));
			repository.save(new Item("Mirror", 3, "abci"));
		};
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.regex("/stock/.*"))
				.apis(RequestHandlerSelectors.basePackage("gal.mosaee"))
				.build();
	}
}