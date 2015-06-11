package com.io.service.impl;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.io.service.ComputationService;

@Service(value = "computionalService")
public class ComputationServiceImpl implements ComputationService {

	private static Logger logger = Logger.getLogger(ComputationServiceImpl.class);
	
	public BigDecimal computeFactorial(Integer number) {
		BigDecimal result = BigDecimal.ONE;
		if(number != 0) {
			for(int i=1; i<=number; i++) {
				result = result.multiply(BigDecimal.valueOf(i));
			}
		}
		return result;
	}

}