package com.microservices.cart.query;

import org.springframework.data.annotation.Id;

public class Product {
	@Id
	private long id;
	private int price;
	private String name;
	private String longDescription;
	private String shortDescription;
	public Product(){
		
	}
	
	public Product(long id){
		this.id = id;
	}

	public int getPrice() {
		// TODO Auto-generated method stub
		return price;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
