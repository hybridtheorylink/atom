package com.atom.mvc.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atom.constants.MsgCode;
import com.atom.mvc.controller.base.BaseController;
import com.atom.mvc.http.Request;
import com.atom.mvc.http.Response;
import com.atom.mvc.http.ResponseHeader;
import com.atom.mvc.service.base.IService;
import com.atom.tools.DateKit;
import com.atom.tools.FileInfo;
import com.atom.tools.FileUploderKit;
import com.jfinal.core.ApplicationContextKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;


public class FileController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(FileController.class);

	
	public void fileUpload(){
		UploadFile up = getFile();
		FileInfo fileInfo =FileUploderKit.upload(up);
		renderJson(fileInfo);
	}


	

}
