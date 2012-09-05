package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class SetUserDetailInfo
 {
	//2007测试成功
    public static void main(String[] args) {

    	String userName = "herolin";
    	String ADDRESS = "深圳南山科技园";
    	String AGE="23";
    	String BIRTHDAY="19840102";
    	String BLOODTYPE = "1";
    	String CITY = "深圳";
    	String COLLAGE = "深大";
    	String CONSTELLATION="3";
    	String COUNTRY="中国";
    	String FAX="8012";
    	String HOMEPAGE="rtx.tencent.com";
    	String MEMO= "没有个人说明";
    	String POSITION ="员工";
    	String POSTCODE ="510650";
    	String PROVINCE= "广东省";
    	String STREET= "科技园";
    	String PHONE = "0755-83765566";
    	String MOBILE = "13510143536";
        
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.setUserDetailInfo (userName ,ADDRESS, AGE, BIRTHDAY,
     BLOODTYPE, CITY, COLLAGE, CONSTELLATION, COUNTRY, FAX,HOMEPAGE, MEMO, 
     POSITION, POSTCODE, PROVINCE, STREET,PHONE, MOBILE);
    		if (iRet==0)
    		{
    			System.out.println("设置详细资料成功");
    		}
    		else 
    		{
    			System.out.println("设置详细资料失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
