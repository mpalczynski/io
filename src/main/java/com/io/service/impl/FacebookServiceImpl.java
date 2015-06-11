package com.io.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.io.json.facebook.Page;
import com.io.json.jsontest.Md5;
import com.io.service.FacebookService;

@Service(value = "facebookService")
public class FacebookServiceImpl implements FacebookService {

	private static Logger logger = Logger.getLogger(FacebookServiceImpl.class);
	private final String graphURL = "http://graph.facebook.com/";
	
	@Autowired
	private RestTemplate restTemplate;
	//
	@Autowired
	private AsyncRestTemplate asyncRestTemplate;

	public Page getPage(String id) {
		return restTemplate.getForObject(graphURL + id, Page.class);
	}

	public ListenableFuture<ResponseEntity<Page>> getPageAsync(String id) {
		return asyncRestTemplate.getForEntity(graphURL + id, Page.class);
	}
	
	public Md5 getMd5(String value) {
		return restTemplate.getForObject("http://md5.jsontest.com/?text={value}", Md5.class, value);
	}

	public ListenableFuture<ResponseEntity<Md5>> getMd5Async(String value) {
		//http://fileking.pl/sleep123.php?text={value}&time123=1
		return asyncRestTemplate.getForEntity("http://md5.jsontest.com/?text={value}", Md5.class, value);
	}

}