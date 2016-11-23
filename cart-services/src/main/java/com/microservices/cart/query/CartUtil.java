package com.microservices.cart.query;

public class CartUtil {

	public static long getCartIdentifier(long cartId, long productId){
		String s = cartId+""+productId;
		return Long.valueOf(s).longValue();
	}
	
	public static String CART_OPEN="OPEN";
	public static String CART_CHECKEDOUT="CHECKED_OUT";
	public static String CART_EXPIRED="EXPIRED";
	
}
