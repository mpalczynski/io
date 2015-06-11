package com.io.service;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import com.io.json.facebook.Page;
import com.io.json.jsontest.Md5;

public interface FacebookService {

	Page getPage(String id);
	
	ListenableFuture<ResponseEntity<Page>> getPageAsync(String id);
	
	Md5 getMd5(String value);
	
	ListenableFuture<ResponseEntity<Md5>> getMd5Async(String value);
	
}
