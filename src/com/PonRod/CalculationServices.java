package com.PonRod;

import java.text.DecimalFormat;


public class CalculationServices {
	public Double GetMonthlyPayment(Double totalPayment, String months){
		Double months_double = Double.valueOf(months);
		return totalPayment/months_double;
	}
	
	public Double GetDownPaymentByPercent(String total,String percent){
		Double total_decimal = Double.valueOf(total);
		Double precent_decimal = Double.valueOf(percent);
		return (precent_decimal / 100) * total_decimal;
	}
	
	public Double GetTotalInterest(String interestRate,String months,String amountAfterDeduct){
		Double total_decimal = Double.valueOf(amountAfterDeduct);
		Double rate = Double.valueOf(interestRate);
		Double years = Double.valueOf(months) / 12;
		return (rate * years * total_decimal)/100;
	}
	
	 public String FormatDecimal(String pattern, Double s) { 
		 DecimalFormat myFormatter = new DecimalFormat(pattern); 
		 String stringformatoutput = myFormatter.format(s); 
		 return stringformatoutput; 
	 }
}
