package com.ksptooi.ASMC.Plugins;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import com.ksptooi.ASMC.Command.Command_cmd;
import com.ksptooi.ASMC.Main.ASMC;
import com.ksptooi.ASMC.Message.MessageManager;
import com.ksptooi.gdc.FileAPI.IOController_V5;
import sun.misc.ClassLoaderUtil;

public class PluginsManager {


	//已注册的插件主类Map (插件名 || 插件主类实例)
	HashMap<String,ASMCPlugin> installPluginMainClassMap=new HashMap<String,ASMCPlugin>();
	
	//已注册插件列表
	ArrayList<String> installPluginList = new ArrayList<String>();

	
	
	
	
	//待注册插件主类Map (插件名 || 主类地址)
	HashMap<String,String> pluginMainClassMap=new HashMap<String,String>();
	
	//待注册插件文件map (插件名 || 文件实例)
	HashMap<String,File> pluginFileMap=new HashMap<String,File>();	
	
	//待注册插件文件列表
	ArrayList<File> pluginList=new ArrayList<File>();
	
	//待注册插件名字列表
	ArrayList<String> pluginNameList=new ArrayList<String>();
	
	
	
	//已注册的命令类型Map (命令类型名 || 命令类型实例)
	HashMap<String,Command_cmd> regCommandTypeMap=new HashMap<String,Command_cmd>();

	//已注册的命令列表
	ArrayList<String> regCommandNameList=new ArrayList<String>();
	
	
	
	
	
	//公用
	IOController_V5 v5=ASMC.getV5();
	
	MessageManager msg = ASMC.getMessageManager();
	
	
	
	//注册命令
	public void regCommandType(String CommandTypeName,Command_cmd CommandTypeEntity) {
		
		if(CommandTypeEntity == null) {
			msg.sendErrorMessage("注册命令失败");
			return;
		}	
		
		//判断是否冲突
		for(String str:regCommandNameList) {
			
			if(str.equalsIgnoreCase(CommandTypeName)) {
				msg.sendErrorMessage("注册命令失败,命令冲突.");
				return;
			}
			
		}
		
		
		//注册命令
		regCommandNameList.add(CommandTypeName);
		
		
		regCommandTypeMap.put(CommandTypeName, CommandTypeEntity);
		
	}
	
	
	
	
	//加载插件
	public void LoadAllPlugins(){
		
		try{
			
			//加载
			for(String PluginName:pluginNameList){
				
				//插件主类字符串
				String pluginMainClass = pluginMainClassMap.get(PluginName);

				//插件文件
				File pluginFile = pluginFileMap.get(PluginName);
				
				//插件主类实例
				ASMCPlugin ASMCP=null;
				
				URL url=pluginFile.toURI().toURL();
				ClassLoader loader=new URLClassLoader(new URL[]{url});//创建ClassLoader
								
							
				msg.sendSysMessage("加载:"+PluginName);
				
				
				
				Class<?> cls=loader.loadClass(pluginMainClass);
				
				Method m1 = cls.getDeclaredMethod("getThis");
				
				Object obj = cls.newInstance();
				
				
				ASMCP=(ASMCPlugin)m1.invoke(obj);
				
				
				
				//注册插件
				installPluginMainClassMap.put(PluginName, ASMCP);
				
				//注册插件命令表
				
//				
//				//添加插件命令类型到列表
//				RegCommandTypeList.add(pluginRegCommandTypeName);
				
				
				//关闭ClassLoader
				ClassLoaderUtil.releaseLoader((URLClassLoader)loader);
			}
			
			
			
			//执行插件的onEnable
			for(String str:regCommandNameList){
				
				ASMCPlugin plugin=(ASMCPlugin) installPluginMainClassMap.get(str);			
				plugin.onEnable();
				
			}
			
			//显示已注册的命令类型
			for(String str:regCommandNameList){
				
				msg.sendSysMessage("注册命令类型:"+str);
				
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	
	
	
	
	//查找插件
	public void SearchPlugins(){

		
		msg.sendSysMessage("加载ASMC插件");
		
		String PluginName = null;
		
		//创建插件文件夹
		ASMC.getMainPluginsfolder().mkdirs();
		
		
		//取所有插件
		File[] PrePluginList = ASMC.getMainPluginsfolder().listFiles();
			 
		
		//去除不是插件的文件 || 并将文件添加至 PluginList中
		for(int i=0;i<PrePluginList.length;i++){
			
			if( ! PrePluginList[i].getName().contains(".jar")){
				continue;
			}	
			
			pluginList.add(PrePluginList[i]);
			
		}

		try{		
			
			
	
			//获取
			for(File f:pluginList){
				
				msg.sendSysMessage("获取:"+f.getName());
								
				URL url=new URL("jar:file:/"+f.getPath()+"!/ASMC_Plugin.gd"); 
			
				
				InputStream is=url.openStream(); 		
				String main=v5.getKeyValueOfInputStream(is, "Plugin_Main");
				
				
				is=url.openStream();
				PluginName=v5.getKeyValueOfInputStream(is, "Plugin_Name");
				
				
				//判断是否有重名插件
				for(String str:pluginNameList){
					
					if(str.equalsIgnoreCase(PluginName)){
						msg.sendErrorMessage("加载插件:"+PluginName+"时发生错误,插件名称冲突!");
						continue;
					}
					
				}
							
				
				//加入插件名字
				pluginNameList.add(PluginName);
				
				//加入插件文件
				pluginFileMap.put(PluginName, f);
				
				//加入插件主类
				pluginMainClassMap.put(PluginName, main);		
				
			
			}
			
				
			
		}catch(Exception e){		
			e.printStackTrace();			
		}
		

		
		
		
	}



	
	




	public ArrayList<String> getRegCommandTypeList() {
		return regCommandNameList;
	}




	public HashMap<String, Command_cmd> getRegCommandTypeMap() {
		return regCommandTypeMap;
	}




	
	
}
