package com.zhenio.xreading.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhenio.xreading.mapper.CurrencyDataMapper;
import com.zhenio.xreading.model.CurrencyData;
import com.zhenio.xreading.model.User;

@Service
public class CurrencyService {
	private CurrencyDataMapper currencyDataMapper;
	
	@Autowired
    public CurrencyService(CurrencyDataMapper currencyDataMapper) {
        this.currencyDataMapper = currencyDataMapper;
    }
	
	public boolean insert(CurrencyData currencyData) {
		return currencyDataMapper.insert(currencyData) > 0;
	}
	
	public List<CurrencyData> query(String startDate,String endDate){
		return currencyDataMapper.query(startDate, endDate);
	};
	
	public int delete() {
		return currencyDataMapper.delete();
	}
	
	public boolean valDate(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date dateTime = dateFormat.parse(dateStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
