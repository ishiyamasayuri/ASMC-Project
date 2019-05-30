package com.ksptooi.ASMC.Command;

import java.lang.reflect.Method;

import com.ksptooi.ASMC.Main.ASMC;

public class CommandTools {

	
	//VF9 - 通过类名获取类实例
	
	//获取类型
	public static Command_cmd getType(String TypeName){
		
		try {

			
			Class<?> c=Class.forName("com.ksptooi.ASMC.Command."+TypeName);

			Method m1 = c.getDeclaredMethod("getThis");  
			
			Object obj = c.newInstance();
			
			Command_cmd CT=(Command_cmd)m1.invoke(obj);
			
			return CT;
			
		} catch (Exception e) {	
			ASMC.getMessageManager().sendWarningMessage("没有找到此CommandType:"+TypeName);
			return null;
		}
		
	}
	
	
	
}
