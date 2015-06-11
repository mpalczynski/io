package com.io.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.io.json.jsontest.Md5;
import com.io.service.ComputationService;
import com.io.service.FacebookService;

@RestController
@RequestMapping("/nonblocking")
public class NonblockingController {

	private static Logger logger = Logger.getLogger(NonblockingController.class);
	
	private AtomicLong reqCounter = new AtomicLong(0);
	
	@Autowired
	private ComputationService computationService;
	
	@Autowired
	private FacebookService facebookService;
	
	@RequestMapping(value = "sleep", method = RequestMethod.GET)
	public Callable<String> sleep(@RequestParam("number") Integer number) {
		
		return new Callable<String>() {
			
			public String call() throws Exception {
				reqCounter.getAndIncrement();
				long startTime = System.currentTimeMillis();
				
				Thread.sleep(number);
				
				long reqId = reqCounter.getAndDecrement();
				logger.info("Non-blocking sleep request #" + reqId + ": " + (System.currentTimeMillis() - startTime) + "ms");
				
				return (System.currentTimeMillis() - startTime) + "ms";
			}
			
		};
	}
	
	@RequestMapping(value = "factorial", method = RequestMethod.GET)
	public Callable<ResponseEntity<String>> factorial(@RequestParam("number") Integer number) {
		
		return new Callable<ResponseEntity<String>>() {
			
			public ResponseEntity<String> call() throws Exception {
				reqCounter.getAndIncrement();
				long startTime = System.currentTimeMillis();
				
				BigDecimal result = computationService.computeFactorial(number);

				long reqId = reqCounter.getAndDecrement();
				logger.info("Non-blocking factorial request #" + reqId + ": " + (System.currentTimeMillis() - startTime) + "ms");
				
				NumberFormat formatter = new DecimalFormat("0.######E0");
				return new ResponseEntity<>(formatter.format(result).toString(), HttpStatus.OK);
			}
			
		};
	}
	
	@RequestMapping(value = "consume", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<Md5>> consume() {
		reqCounter.getAndIncrement();
		
		DeferredResult<ResponseEntity<Md5>> deferred = new DeferredResult<>();
		ListenableFuture<ResponseEntity<Md5>> feature = facebookService.getMd5Async("Lorem Ipsum jest tekstem stosowanym jako przykładowy wypełniacz w przemyśle poligraficznym.");
		feature.addCallback(new ListenableFutureCallback<ResponseEntity<Md5>>() {

			@Override
			public void onSuccess(ResponseEntity<Md5> arg0) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Md5 profile = arg0.getBody();
				logger.info("Nonblocking consume request #" + reqCounter.getAndDecrement() + ": md5=" + arg0.getBody().getMd5());
				deferred.setResult(new ResponseEntity<>(profile, HttpStatus.OK));
			}

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

		});

		return deferred;
	}

}
