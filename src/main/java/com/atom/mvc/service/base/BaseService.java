package com.atom.mvc.service.base;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.atom.mvc.http.Response;
import com.atom.mvc.http.ResponseHeader;
import com.atom.tools.DateKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class BaseService {
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BaseService.class);


    /**
     * 分页
     * @param dataSource 数据源
     * @param splitPage
     * @param select
     * @param fromsql
     * @param paramValue
     */
    protected void splitPageBase(String dataSource, SplitPage splitPage, String select, String fromsql,LinkedList<Object> paramValue ){
        // 接收返回值对象
        StringBuilder formSqlSb = new StringBuilder();
        formSqlSb.append(fromsql);

        String formSql = formSqlSb.toString();

        Page<Record> page = Db.use(dataSource).paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select, null, formSql, paramValue.toArray());
        splitPage.setTotalPage(page.getTotalPage());
        splitPage.setTotalRows(page.getTotalRow());
        splitPage.setData(page.getList());
        splitPage.setCurPage(splitPage.getPageNumber());
        splitPage.setSuccess("true");
    }
    
	public Response renderSUC(Object data, Response response, ResponseHeader header) {
		header.setSuccflag(Response.SUCCESS);
		response.setHeader(header);
		response.setData(data);
		return response;
	}
	public static final String METHOD_BOTH = "BOTH";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	/**
	 * 参数转为String
	 * @param param
	 * @return
	 */
	public String paramsStringFilter(Object param) {
		String result = param != null ? param.toString() : "";
		result = result.trim();
		return result;
	}

	/**
	 * 参数转为Boolean
	 * @param param
	 * @return
	 */
	public boolean paramsBooleanFilter(Object param) {
		return Boolean.parseBoolean(String.valueOf(param));
	}

	/**
	 * 参数转化为Double
	 * @param param
	 * @return
	 */
	public Double paramsDoubleFilter(Object param) {
		if(param == null) {
			return null;
		}
		return Double.parseDouble(String.valueOf(param));
	}

	/**
	 * 参数转化为Integer
	 * @param param
	 * @return
	 */
	public Integer paramsIntegerFilter(Object param) {
		if(param == null||param.equals("")) {
			return null;
		}
		return Integer.parseInt(String.valueOf(param));
	}
	/**
	 * 参数转化为BigDecimal
	 * @param param
	 * @return
	 */
	public BigDecimal paramsBigDecimalFilter(Object param) {
		if(param == null||param.equals("")) {
			return null;
		}
		return new BigDecimal(String.valueOf(param));
	}

	/**
	 * 参数转化为Long
	 * @param param
	 * @return
	 */
	public Long paramsLongFilter(Object param) {
		if(param == null) {
			return null;
		}
		return Long.parseLong(String.valueOf(param));
	}

	/**
	 * 参数转化为Date
	 * @param param
	 * @return
	 */
	public Date paramsDateFilter(Object param) {
		if(param == null){
			return null;
		}
		Date result = param == null ? new Date() : DateKit.parse(param.toString());
		return result;
	}

	/**
	 * 参数转化为Map
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map paramsMapFilter(Object param) {
		Map map = new HashMap();
		if(param == null) {
			return new HashMap();
		}
		if(param.toString().length() <= 2) {
			return new HashMap();
		}
		String mspStr = param.toString().substring(1, param.toString().length() - 1);
		StringTokenizer items = null;
		for(StringTokenizer entrys = new StringTokenizer(mspStr, ",");entrys.hasMoreTokens();
		map.put(items.nextToken().trim(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
			items = new StringTokenizer(entrys.nextToken(), ":");
		return map;

	}

	/**
	 * 参数转为JSONObject
	 * @param param
	 * @return
	 */
	public JSONObject paramsJSONObjectFilter(Object param) {
		if(param == null) {
			return new JSONObject();
		}
		JSONObject object = new JSONObject();
		try{
			object = JSONObject.parseObject(param.toString());
		}catch (Exception e) {
			return new JSONObject();
		}
		return object;
	}


	/**
	 * 获取数量
	 * 注意：sql中必须as num
	 * @param sql
	 * @param paras
	 * @return
	 */
	public int getCount(String sql, Object ...paras) {
		Record record = Db.findFirst(sql, paras);
		return paramsIntegerFilter(record.getColumns().get("num"));
	}



	/**
	 * 限制请求方式
	 * @param response
	 * @param header
	 * @param method
	 * @param type
	 * @return
	 */
	public Response methodPostFilter(Response response, ResponseHeader header, String method, String type) {
		if(method.equals(type)){
			response.setHeader(header);
			return response;
		}
		return renderFAIL("1001", response, header);
	}

	/**
	 * 返回错误的Response
	 * @param errorCode
	 * @param response
	 * @param header
	 * @return
	 */
	public Response renderFAIL(String errorCode, Response response, ResponseHeader header) {
		return renderFAIL(errorCode,null,response,header);
	}

	public Response renderFAIL(String errorCode, String errorMsg, Response response, ResponseHeader header) {
		header.setSuccflag(Response.FAIL);
		header.setErrorcode(errorCode);
		header.setErrormsg(errorMsg);
		response.setHeader(header);
		return response;
	}    
	public  String autoNum() {
		return Db.queryStr("select autonum(37)");
	}
	

	
	/**
	 * Get model from http request.
	 */
//	public <T> T getModel(Class<T> modelClass,Map<String, Object> paramsMap) {
//		return (T)Injector.injectModelByMap(modelClass, paramsMap, false);
//	}
	
	
	
	
	
	


}