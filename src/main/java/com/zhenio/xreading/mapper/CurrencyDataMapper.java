package com.zhenio.xreading.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.zhenio.xreading.model.CurrencyData;
import com.zhenio.xreading.model.User;


@Mapper
@Component
public interface CurrencyDataMapper {
	@Insert("INSERT INTO CurrencyData(ConDate,Content)VALUES (#{ConDate},#{Content})")  
    int insert(CurrencyData currencyData);
	
	@Select("SELECT * FROM CurrencyData WHERE ConDate between #{startDate} and #{endDate}")
    List<CurrencyData> query(String startDate,String endDate);
	
	@Delete("DELETE FROM CurrencyData")
	int delete();
}
