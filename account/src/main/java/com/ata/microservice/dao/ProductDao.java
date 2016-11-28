package com.ata.microservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ata.microservice.db.MysqlConnectionManager;
import com.ata.microservice.representation.Product;

public class ProductDao {

	public ProductDao(){
		
	}
	
	public Product getProduct(int productId) throws Exception{
		MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		Connection con = cm.getConnection();
		PreparedStatement ps = null;

		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement("select * from product where product_id=?");
			ps.setInt(1, productId);
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				Product product = new Product();
				product.setProductId(productId);
				product.setCategory(rs.getString("category"));
				product.setDescription(rs.getString("description"));
				product.setImageUrl(rs.getString("image_url"));
				product.setProductName(rs.getString("product_name"));
				
				return product;
			} else {
				return null;
			}
		} catch(SQLException sqle){
			throw new Exception(sqle);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}

	public List<Product> getProducts() throws Exception{
		MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		Connection con = cm.getConnection();
		PreparedStatement ps = null;

		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		
		try {
			ps = con.prepareStatement("select * from product");
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				Product product = new Product();
				product.setProductId(rs.getInt("product_id"));
				product.setCategory(rs.getString("category"));
				product.setDescription(rs.getString("description"));
				product.setImageUrl(rs.getString("image_url"));
				product.setProductName(rs.getString("product_name"));
				
				list.add(product);
			} 
			return list;
		} catch(SQLException sqle){
			throw new Exception(sqle);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}

}
