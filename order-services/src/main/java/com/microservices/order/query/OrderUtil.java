package com.microservices.order.query;

import java.util.HashMap;
import java.util.Map;

public class OrderUtil {

	public final static String ORDER_ID_SEPARATOR=":";
	public final static int ORDER_OPEN=0;
	public final static int ORDER_CANCELLED=1;
	public final static int ORDER_CONFIRMED=2;
	public final static int ORDER_COMPLETED=3;
	private static Map<Long, String> orderMap;
	static {
		orderMap = new HashMap<Long, String>();
	}
	
	public static String generateOrderId(long customerId){
		
		boolean existOrderId  = orderMap.containsKey(customerId);
		String orderId= String.valueOf(customerId);
		long newOrderId = 0;
		long currOrderId = 1;
		if(existOrderId){
			String[] currOrderIDString = ((String) orderMap.get(customerId)).split(ORDER_ID_SEPARATOR);
			currOrderId = Long.valueOf(currOrderIDString[1]);
		}

		newOrderId = currOrderId+1;
		orderId.concat(":").concat(String.valueOf(newOrderId));
		orderMap.put(customerId, orderId);
		
		return orderId;
	}
	
	
}
