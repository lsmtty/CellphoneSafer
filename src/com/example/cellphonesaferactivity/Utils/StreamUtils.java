package com.example.cellphonesaferactivity.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils 
{
	static public String getRespondString(InputStream inputStream)
	{
		String target = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		byte [] bytes = new byte[1024];
		int len = 0;
		try {
			while((len=inputStream.read())!=0)
			{
				bo.write(bytes, 0,len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				inputStream.close();
				if(bo!=null)
					bo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		target = bo.toString();//****ByteArrayOutputStream 不用文件初始化，最后直接用toString()读出
		return target;
	}
}
