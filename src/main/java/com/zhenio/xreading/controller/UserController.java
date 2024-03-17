package com.zhenio.xreading.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenio.xreading.Played;
import com.zhenio.xreading.model.CurrencyData;
import com.zhenio.xreading.model.User;
import com.zhenio.xreading.service.CurrencyService;
import com.zhenio.xreading.service.UserServcie;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/api/")
public class UserController {
	@Autowired
	UserServcie userServcie;
	@Autowired
	CurrencyService currencyService;

	@PostMapping("user/login")
	public Played login(@RequestBody User user) {
		return userServcie.login(user.getName(), user.getPassword());
	}

	@GetMapping("user/register")
	public Played register(@RequestParam String name, @RequestParam String password, HttpRequest request) {
		URI uri = request.getURI();
		return userServcie.register(name, password);
	}

	@GetMapping("list")
	public Played list() {
		return userServcie.findAll();
	}

	@GetMapping("insertCurrencyData")
	public JSONObject insertCurrencyData() {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource cpr = new ClassPathResource("rpt/" + "DailyForeignExchangeRates.json");
		try {
			JsonNode jsonNode = objectMapper.readTree(cpr.getFile());
			currencyService.delete();
			if (jsonNode.isArray()) {
				for (final JsonNode objNode : jsonNode) {
					String condate = objNode.get("Date").asText();
					String content = objNode.toString();
					CurrencyData data = new CurrencyData();
					data.setConDate(condate);
					data.setContent(content);
					currencyService.insert(data);
					/*
					 * System.out.println(objNode); System.out.println(objNode.get("Date"));
					 * System.out.println(objNode.get("USD/NTD"));
					 * System.out.println(objNode.get("RMB/NTD"));
					 * System.out.println(objNode.get("EUR/USD"));
					 * System.out.println(objNode.get("USD/JPY"));
					 * System.out.println(objNode.get("GBP/USD"));
					 * System.out.println(objNode.get("AUD/USD"));
					 * System.out.println(objNode.get("USD/HKD"));
					 * System.out.println(objNode.get("USD/RMB"));
					 * System.out.println(objNode.get("USD/ZAR"));
					 * System.out.println(objNode.get("NZD/USD"));
					 */
				}
			}
			JSONObject json = new JSONObject();
			json.put("message", "insertSuccess");
			return json;
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("message", "insertfail");
			return json;
		}

	}

	@PostMapping("queryCurrencyData")
	public JSONObject queryCurrencyData(String startDate, String endDate, String currency) {
		if (currencyService.valDate(startDate) && currencyService.valDate(endDate)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				Date startDayDate = sdf.parse(startDate);
				Date endDayDate = sdf.parse(endDate);
				long betweenDays = (endDayDate.getTime() / (24 * 60 * 60 * 1000))
						- (startDayDate.getTime() / (24 * 60 * 60 * 1000));
				if (betweenDays >= 365) {
					JSONObject json = new JSONObject();
					json.put("code", "E001");
					json.put("message", "日期區間不符");
					JSONObject error = new JSONObject();
					error.put("error", json);
					JSONObject responseData = new JSONObject();
					responseData.put("Failed", error);
					return responseData;
				} else {
					List<CurrencyData> alldata = currencyService.query(startDate.replace("/", ""),
							endDate.replace("/", ""));
					if (alldata == null || alldata.size() == 0) {
						JSONObject json = new JSONObject();
						JSONObject json1 = new JSONObject();
						JSONObject json2 = new JSONObject();
						json1.put("code", "0000");
						json1.put("message", "日期區間查無資料");
						json.put("error", json1);
						json2.put("Success", json);
						return json2;
					} else {
						JSONObject json = new JSONObject();
						JSONObject json1 = new JSONObject();
						JSONObject json2 = new JSONObject();
						json1.put("code", "0000");

						JSONArray arr = new JSONArray();
						for (CurrencyData element : alldata) {
							JSONObject repsonseData = new JSONObject().parseObject(element.getContent());
							Set<String> allKey = repsonseData.keySet();
							for (String elementkey : allKey) {
								if (elementkey.indexOf(currency + "/NTD") != -1) {
									JSONObject arrData = new JSONObject();
									String money = repsonseData.getString(currency + "/NTD");
									String dateStr = repsonseData.getString("Date");
									arrData.put("date", dateStr);
									arrData.put("usd", money);
									arr.add(arrData);
								}
							}

						}
						if (arr.size() > 0) {
							json.put("currency", arr);
							json1.put("message", "成功");
						} else {
							json1.put("message", "查無" + currency + "此貨幣資料");
						}
						json.put("error", json1);
						json2.put("Success", json);
						return json2;
					}
				}
			} catch (Exception e) {
				JSONObject json = new JSONObject();
				json.put("code", "E999");
				json.put("message", e.getMessage());
				JSONObject error = new JSONObject();
				error.put("error", json);
				JSONObject responseData = new JSONObject();
				responseData.put("Failed", error);
				return responseData;
			}
		} else {
			JSONObject json = new JSONObject();
			json.put("code", "E002");
			json.put("message", "輸入日期格式錯誤");
			JSONObject error = new JSONObject();
			error.put("error", json);
			JSONObject responseData = new JSONObject();
			responseData.put("Failed", error);
			return responseData;
		}

	}
}
