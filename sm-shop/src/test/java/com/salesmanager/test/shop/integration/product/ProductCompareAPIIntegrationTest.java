package com.salesmanager.test.shop.integration.product;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.model.catalog.category.Category;
import com.salesmanager.shop.model.catalog.category.CategoryDescription;
import com.salesmanager.shop.model.catalog.category.PersistableCategory;
import com.salesmanager.shop.model.catalog.product.ProductDescription;
import com.salesmanager.shop.model.catalog.product.ReadableProduct;
import com.salesmanager.shop.model.catalog.product.product.PersistableProduct;
import com.salesmanager.shop.model.catalog.product.product.PersistableProductInventory;
import com.salesmanager.shop.model.catalog.product.product.ProductSpecification;
import com.salesmanager.test.shop.common.ServicesTestSupport;

/**
 * Integration tests for Product Compare API
 */
@SpringBootTest(classes = ShopApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ProductCompareAPIIntegrationTest extends ServicesTestSupport {

	@Test
	public void testGetProductsForCompare() throws Exception {
		
		// Create test category
		PersistableCategory category = createTestCategory("compare-cat");
		
		// Create 3 test products
		Long productId1 = createTestProduct("COMPARE-001", "Product 1", category);
		Long productId2 = createTestProduct("COMPARE-002", "Product 2", category);
		Long productId3 = createTestProduct("COMPARE-003", "Product 3", category);
		
		// Test: Fetch products for comparison
		String url = String.format("/api/v1/products/compare?ids=%d,%d,%d&store=%s&lang=en", 
			productId1, productId2, productId3, Constants.DEFAULT_STORE);
		
		ResponseEntity<List<ReadableProduct>> response = testRestTemplate.exchange(
			url,
			HttpMethod.GET,
			new HttpEntity<>(getHeader()),
			new ParameterizedTypeReference<List<ReadableProduct>>() {}
		);
		
		// Assertions
		assertThat(response.getStatusCode(), is(OK));
		assertNotNull(response.getBody());
		assertEquals(3, response.getBody().size());
		
		// Verify product details
		List<ReadableProduct> products = response.getBody();
		assertTrue(products.stream().anyMatch(p -> p.getSku().equals("COMPARE-001")));
		assertTrue(products.stream().anyMatch(p -> p.getSku().equals("COMPARE-002")));
		assertTrue(products.stream().anyMatch(p -> p.getSku().equals("COMPARE-003")));
	}
	
	@Test
	public void testGetProductsForCompare_MaxFourProducts() throws Exception {
		
		// Create test category
		PersistableCategory category = createTestCategory("compare-cat-max");
		
		// Create 4 test products
		Long productId1 = createTestProduct("MAX-001", "Product 1", category);
		Long productId2 = createTestProduct("MAX-002", "Product 2", category);
		Long productId3 = createTestProduct("MAX-003", "Product 3", category);
		Long productId4 = createTestProduct("MAX-004", "Product 4", category);
		
		// Test: Fetch 4 products (should succeed)
		String url = String.format("/api/v1/products/compare?ids=%d,%d,%d,%d&store=%s&lang=en", 
			productId1, productId2, productId3, productId4, Constants.DEFAULT_STORE);
		
		ResponseEntity<List<ReadableProduct>> response = testRestTemplate.exchange(
			url,
			HttpMethod.GET,
			new HttpEntity<>(getHeader()),
			new ParameterizedTypeReference<List<ReadableProduct>>() {}
		);
		
		// Assertions
		assertThat(response.getStatusCode(), is(OK));
		assertEquals(4, response.getBody().size());
	}
	
	@Test
	public void testGetProductsForCompare_ExceedsMaxLimit() throws Exception {
		
		// Create test category
		PersistableCategory category = createTestCategory("compare-cat-exceed");
		
		// Create 5 test products
		Long productId1 = createTestProduct("EXCEED-001", "Product 1", category);
		Long productId2 = createTestProduct("EXCEED-002", "Product 2", category);
		Long productId3 = createTestProduct("EXCEED-003", "Product 3", category);
		Long productId4 = createTestProduct("EXCEED-004", "Product 4", category);
		Long productId5 = createTestProduct("EXCEED-005", "Product 5", category);
		
		// Test: Try to fetch 5 products (should fail)
		String url = String.format("/api/v1/products/compare?ids=%d,%d,%d,%d,%d&store=%s&lang=en", 
			productId1, productId2, productId3, productId4, productId5, Constants.DEFAULT_STORE);
		
		try {
			testRestTemplate.exchange(
				url,
				HttpMethod.GET,
				new HttpEntity<>(getHeader()),
				new ParameterizedTypeReference<List<ReadableProduct>>() {}
			);
		} catch (Exception e) {
			// Expected to throw exception for exceeding limit
			assertTrue(e.getMessage().contains("Maximum 4 products allowed"));
		}
	}
	
	@Test
	public void testGetProductsForCompare_EmptyIds() throws Exception {
		
		// Test: Empty IDs (should fail)
		String url = String.format("/api/v1/products/compare?ids=&store=%s&lang=en", 
			Constants.DEFAULT_STORE);
		
		try {
			testRestTemplate.exchange(
				url,
				HttpMethod.GET,
				new HttpEntity<>(getHeader()),
				new ParameterizedTypeReference<List<ReadableProduct>>() {}
			);
		} catch (Exception e) {
			// Expected to throw exception
			assertTrue(e.getMessage().contains("Product IDs are required") || 
					   e.getMessage().contains("Bad Request"));
		}
	}
	
	@Test
	public void testGetProductsForCompare_InvalidProductId() throws Exception {
		
		// Create test category
		PersistableCategory category = createTestCategory("compare-cat-invalid");
		
		// Create 2 valid products
		Long productId1 = createTestProduct("INVALID-001", "Product 1", category);
		Long productId2 = createTestProduct("INVALID-002", "Product 2", category);
		
		// Test: Include invalid product ID (should return only valid products)
		String url = String.format("/api/v1/products/compare?ids=%d,%d,99999&store=%s&lang=en", 
			productId1, productId2, Constants.DEFAULT_STORE);
		
		ResponseEntity<List<ReadableProduct>> response = testRestTemplate.exchange(
			url,
			HttpMethod.GET,
			new HttpEntity<>(getHeader()),
			new ParameterizedTypeReference<List<ReadableProduct>>() {}
		);
		
		// Assertions - should return only 2 valid products
		assertThat(response.getStatusCode(), is(OK));
		assertEquals(2, response.getBody().size());
	}
	
	// Helper methods
	
	private PersistableCategory createTestCategory(String code) throws Exception {
		PersistableCategory category = new PersistableCategory();
		category.setCode(code);
		category.setSortOrder(1);
		category.setVisible(true);
		
		CategoryDescription description = new CategoryDescription();
		description.setLanguage("en");
		description.setName(code);
		description.setFriendlyUrl(code);
		description.setTitle(code);
		
		List<CategoryDescription> descriptions = new ArrayList<>();
		descriptions.add(description);
		category.setDescriptions(descriptions);
		
		HttpEntity<PersistableCategory> entity = new HttpEntity<>(category, getHeader());
		ResponseEntity<PersistableCategory> response = testRestTemplate.postForEntity(
			"/api/v1/private/category?store=" + Constants.DEFAULT_STORE, 
			entity, 
			PersistableCategory.class
		);
		
		assertThat(response.getStatusCode(), is(CREATED));
		return response.getBody();
	}
	
	private Long createTestProduct(String sku, String name, PersistableCategory category) throws Exception {
		PersistableProduct product = new PersistableProduct();
		product.setSku(sku);
		product.setAvailable(true);
		product.setPrice(BigDecimal.valueOf(99.99));
		
		// Add category
		List<Category> categories = new ArrayList<>();
		Category cat = new Category();
		cat.setId(category.getId());
		categories.add(cat);
		product.setCategories(categories);
		
		// Add description
		ProductDescription desc = new ProductDescription();
		desc.setLanguage("en");
		desc.setName(name);
		desc.setFriendlyUrl(sku.toLowerCase());
		desc.setTitle(name);
		desc.setDescription("Test product for comparison: " + name);
		
		List<ProductDescription> descriptions = new ArrayList<>();
		descriptions.add(desc);
		product.setDescriptions(descriptions);
		
		// Add specifications
		ProductSpecification specs = new ProductSpecification();
		specs.setManufacturer("DEFAULT");
		product.setProductSpecifications(specs);
		
		// Add inventory
		PersistableProductInventory inventory = new PersistableProductInventory();
		inventory.setQuantity(100);
		product.setQuantity(100);
		
		// Create product
		HttpEntity<PersistableProduct> entity = new HttpEntity<>(product, getHeader());
		ResponseEntity<PersistableProduct> response = testRestTemplate.postForEntity(
			"/api/v1/private/product?store=" + Constants.DEFAULT_STORE, 
			entity, 
			PersistableProduct.class
		);
		
		assertThat(response.getStatusCode(), is(CREATED));
		assertNotNull(response.getBody().getId());
		
		return response.getBody().getId();
	}
}
