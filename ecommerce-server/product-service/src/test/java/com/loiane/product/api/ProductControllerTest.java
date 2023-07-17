package com.loiane.product.api;

import com.loiane.product.BaseIntegrationTest;
import com.loiane.product.Product;
import com.loiane.product.ProductController;
import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductPageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest extends BaseIntegrationTest {

    private final String BASE_URL = "/api/products";

    private static final String VALID_NAME = "Product Name";
    private static final String VALID_DESCRIPTION = "description";
    private static final String VALID_CATEGORY = "technology";
    private static final double VALID_PRICE = 10.0;

    private static final String LOREM_IPSUM = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam sed placerat mauris, eget ullamcorper massa. Mauris eget enim a tellus posuere bibendum at ac nibh. Nam tempus, ex quis facilisis dapibus, ex enim faucibus neque, sit amet rutrum quam mauris id sem. Integer elementum pellentesque est. Integer gravida rhoncus dui id imperdiet. Suspendisse libero metus, semper vel facilisis sed, efficitur id quam. Phasellus pulvinar odio in lacus blandit semper. Mauris sollicitudin nibh sed erat consectetur ornare. Donec sit amet nunc a leo accumsan commodo eget vitae lacus. Fusce diam massa, tincidunt at egestas porta, aliquet id neque. Sed aliquet neque ipsum, et tincidunt diam tempus vel. Aenean diam lorem, blandit nec vestibulum vitae, aliquam a leo. Etiam eleifend sollicitudin diam, a sodales nunc volutpat a. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris consectetur elit elementum ultrices dictum.
                        
            Pellentesque porttitor tempor ante, sed venenatis eros iaculis eu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aenean ornare justo lectus, id placerat est condimentum at. Duis viverra eu dui quis dictum. Vivamus quis elit quis ex porttitor fringilla eu sit amet quam. Proin ullamcorper tellus ac facilisis dignissim. Donec suscipit ornare iaculis.
                        
            Praesent fermentum, enim a lacinia finibus, velit nisi dictum risus, ut pharetra quam odio at dui. Donec ut ornare orci, sed porta diam. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Donec enim libero, mattis at blandit ut, lobortis non mauris. Sed quam leo, feugiat eget laoreet at, fermentum id velit. Fusce id ornare metus. Nulla pharetra posuere rhoncus. Proin est velit, congue sodales nulla sed, venenatis vestibulum ipsum. Nullam in lobortis purus. Praesent id leo odio. Sed scelerisque quis augue eu consequat. Donec vulputate risus vel consequat consectetur. Fusce finibus consectetur arcu. Vivamus a mollis nisl.
            """;

    /**
     * Method under test: {@link ProductController#findById(long)}
     */
    @Test
    @DisplayName("Should return a product by id when product exists")
    void findById() {
        // given
        Product product = new Product("Product Name", "description", "technology", 10.0);
        productRepository.save(product);

        // when
        ProductDTO productDTO = testRestTemplate.getForObject(BASE_URL + "/" + product.getId(), ProductDTO.class);

        // then
        assertNotNull(productDTO);
        assertEquals(product.getId(), productDTO.id());
        assertEquals(product.getName(), productDTO.name());
        assertEquals(product.getDescription(), productDTO.description());
        assertEquals(product.getCategory(), productDTO.category());
        assertEquals(product.getPrice(), productDTO.price());
    }

    /**
     * Method under test: {@link ProductController#findById(long)}
     */
    @Test
    @DisplayName("Should return 404 status when searching for a product by id that does not exist")
    void findByIdNotFound() {
        // given
        long id = 1L;

        // when
        var responseEntity = testRestTemplate.getForEntity(BASE_URL + "/" + id, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Method under test: {@link ProductController#findAll(int, int)}
     */
    @Test
    @DisplayName("Should return a list of products using pagination")
    void findAll() {
        // given
        Product product = new Product("Product Name", "description", "technology", 10.0);
        productRepository.save(product);

        // when
        ProductPageDTO productPageDTO = testRestTemplate.getForObject(BASE_URL, ProductPageDTO.class);

        // then
        assertNotNull(productPageDTO);
        assertEquals(1, productPageDTO.totalElements());
        assertEquals(1, productPageDTO.totalPages());
        assertEquals(1, productPageDTO.products().size());
        assertTrue(productPageDTO.products().get(0).id() > 0);
        assertEquals(product.getName(), productPageDTO.products().get(0).name());
        assertEquals(product.getDescription(), productPageDTO.products().get(0).description());
        assertEquals(product.getCategory(), productPageDTO.products().get(0).category());
        assertEquals(product.getPrice(), productPageDTO.products().get(0).price());
    }

    /**
     * Method under test: {@link ProductController#create(ProductDTO)}
     */
    @Test
    @DisplayName("Should create a product")
    void create() {
        // given
        ProductDTO productDTO = new ProductDTO(null, "Product Name", "description", "technology", 10.0);

        // when
        ProductDTO createdProductDTO = testRestTemplate.postForObject(BASE_URL, productDTO, ProductDTO.class);

        // then
        assertNotNull(createdProductDTO);
        assertTrue(createdProductDTO.id() > 0);
        assertEquals(productDTO.name(), createdProductDTO.name());
        assertEquals(productDTO.description(), createdProductDTO.description());
        assertEquals(productDTO.category(), createdProductDTO.category());
        assertEquals(productDTO.price(), createdProductDTO.price());
    }

    /**
     * Method under test: {@link ProductController#create(ProductDTO)}
     */
    @DisplayName("Should return 400 status when creating a product with invalid data")
    @ParameterizedTest(name = "Should return 400 status when creating a product with invalid data: {0}")
    @MethodSource("invalidProductDTOList")
    void createInvalid(ProductDTO productDTO) {
        // when
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(BASE_URL, productDTO, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }



    static Stream<Arguments> invalidProductDTOList() {
        return Stream.of(
                Arguments.arguments(new ProductDTO(null, null, null, null, null)),
                // name validation
                Arguments.arguments(new ProductDTO(null, null, VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, "", VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, " ", VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, "a", VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, LOREM_IPSUM, VALID_DESCRIPTION, VALID_CATEGORY, VALID_PRICE)),
                // description validation
                Arguments.arguments(new ProductDTO(null, VALID_NAME, LOREM_IPSUM, VALID_CATEGORY, VALID_PRICE)),
                // category validation
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, null, VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, "", VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, " ", VALID_PRICE)),
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, LOREM_IPSUM, VALID_PRICE)),
                // price validation
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, VALID_CATEGORY, null)),
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, VALID_CATEGORY, -1.0)),
                Arguments.arguments(new ProductDTO(null, VALID_NAME, VALID_DESCRIPTION, VALID_CATEGORY, 0.0))
        );
    }
}
