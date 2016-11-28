package com.ata.microservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ata.microservice.dao.ProductDao;
import com.ata.microservice.representation.Product;

@RestController
public class ProductController {
	
	private ProductDao productDao = null;
	
	public ProductController(){
		productDao = new ProductDao();
	}

	@RequestMapping(value="/products/{productid}", method=RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable("productid") int productid) {
		Product product = null;
		try {
			product = productDao.getProduct(productid);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(product != null){
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		} else {
			return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
		}
	}

	@RequestMapping(value="/products", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> getProducts() {
		List<Product> list = null;
		try {
			list = productDao.getProducts();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(list.size() > 0){
			return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Product>>(list, HttpStatus.ACCEPTED);
		}
	}

}
