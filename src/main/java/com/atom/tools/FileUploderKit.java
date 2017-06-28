package com.atom.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atom.constants.ConstantUpload;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

/**
 * Created by hsw on 2017-3-9.
 */
public class FileUploderKit {

    private static Logger log = Logger.getLogger(FileUploderKit.class);

    public static FileInfo upload(UploadFile source){
    	
        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;
        
        File file = source.getFile();
        FileInfo fileInfo = getFileInfo(source);

        //文件验证
        if(!checkFile(source, fileInfo)){
            fileInfo.setSuccess(false);
            return fileInfo;
        }

        try {
        	File parent = new File(fileInfo.getPath());
        	if(!parent.exists()){
        		parent.mkdirs();
        	}
        	File outFile = new File(fileInfo.getFullPath());
        	System.out.println(fileInfo.getFullPath());
        	outFile.createNewFile();
            fi = new FileInputStream(file);
            fo = new FileOutputStream(outFile);
            
            in = fi.getChannel();// 得到对应的文件通道

            out = fo.getChannel();// 得到对应的文件通道

            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        fileInfo.setSuccess(true);
        return fileInfo;
    }


    public static boolean checkFile(UploadFile source,FileInfo fileInfo){
        if(fileInfo.getSize()>ConstantUpload.MAX_UPLOAD_SIZE){
            log.error("上传文件太大");
            fileInfo.setMessage("上传文件太大");
            return false;
        }
        if(fileInfo.getType()==null){
            log.error("文件类型不支持");
            fileInfo.setMessage("文件类型不支持");
            return false;
        }
        fileInfo.setMessage("文件验证通过");
        return true;
    }
    public static FileInfo getFileInfo(UploadFile source){
        FileInfo fileInfo = new FileInfo();
        String originalName = source.getFileName();
        String extension = originalName.substring(originalName.lastIndexOf(".")+1);
        extension = StringUtils.lowerCase(extension);
        File file = source.getFile();
        String typePath = "";
        //设置文件类型
        if( Arrays.asList(ConstantUpload.UPLOAD_IMAGE_EXTENSION).contains(extension)){
            fileInfo.setType(FileInfo.TYPE_IMG);
            typePath = ConstantUpload.IMAGEUPLOADPATH;
        }
        else if( Arrays.asList(ConstantUpload.UPLOAD_MEDIA_EXTENSION).contains(extension)){
            fileInfo.setType(FileInfo.TYPE_MEDIA);
            typePath = ConstantUpload.MEDIAUPLOADPATH;
        }
        else if( Arrays.asList(ConstantUpload.UPLOAD_FILE_EXTENSION).contains(extension)){
            fileInfo.setType(FileInfo.TYPE_FILE);
            typePath = ConstantUpload.FILEUPLOADPATH;
        }
        else if( Arrays.asList(ConstantUpload.UPLOAD_EXCEL_EXTENSION).contains(extension)){
            fileInfo.setType(FileInfo.TYPE_EXCEL);
            typePath = ConstantUpload.EXCELUPLOADPATH;
        }
        //设置文件大小
        fileInfo.setSize(file.length());
        fileInfo.setName(UUID.randomUUID()+"." + extension);
        String path = DateKit.format(new Date(), "YYYYMM");	
        String filePath = PropKit.get("upload.path") + typePath+path;
        
        fileInfo.setPath(filePath);
        fileInfo.setOringinName(originalName);
        fileInfo.setRelatePath(typePath+path+File.separator+fileInfo.getName());
        return fileInfo;
    }
}
