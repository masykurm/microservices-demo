package com.ata.microservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ata.microservice.dao.AccountDao;
import com.ata.microservice.representation.Account;
import com.ata.microservice.representation.CommonResponse;

@RestController
public class AccountController {
	
	private AccountDao accountDao = null;
	
	public AccountController(){
		accountDao = new AccountDao();
	}

	@RequestMapping("/")
	public String index(){
		return "hello account " + System.currentTimeMillis();
	}
	
	@RequestMapping(value="/accounts/{username}", method=RequestMethod.GET)
	public ResponseEntity<Account> getAccount(@PathVariable("username") String username) {
		Account account = null;
		try {
			account = accountDao.getAccount(username);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(account != null){
			return new ResponseEntity<Account>(account, HttpStatus.OK);
		} else {
			return new ResponseEntity<Account>(account, HttpStatus.ACCEPTED);
		}
	}
	
	
	@RequestMapping(value="/accounts", method=RequestMethod.POST)
	public ResponseEntity<Account> register(@RequestParam("firstname") String firstname, 
			@RequestParam("lastname") String lastname,
			@RequestParam("username") String username,
			@RequestParam("password") String password){
		
		Account account = null;
		try {
			account = accountDao.addAccount(username, firstname, lastname, password);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Account>(account, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(account != null){
			return new ResponseEntity<Account>(account, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Account>(account, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/accounts/validatelogin", method=RequestMethod.POST)
	public CommonResponse validateLogin(@RequestParam("username") String userName, 
			@RequestParam("password") String password){
		CommonResponse response = new CommonResponse();
		if(isLoginValid(userName, password)){
			response.setErrCode("000");
			response.setErrMessage("SUCCESS");
			response.setErrDescription("Login successful");
			response.setData("the token for login");
		} else {
			response.setErrCode("100");
			response.setErrMessage("Username and Password do not match");
			response.setErrDescription("Username and Password do not match");
			response.setData("");
		}
		return response;
	}
	
	private boolean isLoginValid(String username, String password){
		try {
			return accountDao.validateLogin(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
