package com.io.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.io.json.jsontest.Md5;
import com.io.service.ComputationService;
import com.io.service.FacebookService;

@RestController
@RequestMapping("/blocking")
public class BlockingController {

	private static Logger logger = Logger.getLogger(BlockingController.class);
	
	private AtomicLong reqCounter = new AtomicLong(0);

	@Autowired
	private ComputationService computationService;
	
	@Autowired
	private FacebookService facebookService;
	
	@RequestMapping(value = "factorial", method = RequestMethod.GET)
	public ResponseEntity<String> factorial(@RequestParam("number") Integer number) {
		reqCounter.getAndIncrement();
		long startTime = System.currentTimeMillis();
		
		BigDecimal result = computationService.computeFactorial(number);
		
		logger.info("Blocking factorial request #" + reqCounter.getAndDecrement() + ": " + (System.currentTimeMillis() - startTime) + "ms");
		
		NumberFormat formatter = new DecimalFormat("0.######E0");
		return new ResponseEntity<>(formatter.format(result), HttpStatus.OK);
	}

	@RequestMapping(value = "sleep", method = RequestMethod.GET)
	public String sleep(@RequestParam("number") Integer number) throws InterruptedException {
		reqCounter.getAndIncrement();
		long startTime = System.currentTimeMillis();
		
		Thread.sleep(number);
		
		logger.info("Blocking sleep request #" + reqCounter.getAndDecrement() + ": " + (System.currentTimeMillis() - startTime) + "ms");
		
		return (System.currentTimeMillis() - startTime) + "ms";
	}
	
	@RequestMapping(value = "consume", method = RequestMethod.GET)
	public Md5 consume() {
		reqCounter.getAndIncrement();
		long startTime = System.currentTimeMillis();
		
		//Page page = facebookService.getPage("108591549166068");
		Md5 page = facebookService.getMd5("Lorem Ipsum jest tekstem stosowanym jako przykładowy wypełniacz w przemyśle poligraficznym.");
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Blocking sleep request #" + reqCounter.getAndDecrement() + ": " + (System.currentTimeMillis() - startTime) + "ms");
		return page;
	}

}
