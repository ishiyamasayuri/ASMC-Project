package com.ksptooi.asmc.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import com.ksptooi.asmc.main.Asmc;

import sun.misc.ClassLoaderUtil;

public class PluginClassLoader {

	
	private URLClassLoader classLoader;
	

    public PluginClassLoader(File file){  
    	
    	
    	URL url;
		try {
			
			
			url = file.toURI().toURL();
			classLoader=new URLClassLoader(new URL[]{url});
	
			
		} catch (Exception e) {
			e.printStackTrace();
			Asmc.getLogger().error("加载Class失败!");
		}
    	
    }
    
    public void setFile(File file){
    	
    	URL url;
		try {
			
			
			url = file.toURI().toURL();
			classLoader = new URLClassLoader(new URL[]{url});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
   
    
    public Object loadClass(String className){  
  
    	
    	
		try {
			
			
			Class<?> loadClass = classLoader.loadClass(className);
			
			
			Object obj = null;
	    	
			
			obj = loadClass.newInstance();

			return obj;  
			
		} catch (Exception e) {
			e.printStackTrace();
			Asmc.getLogger().error("加载Class失败!");
		}
		
		return null;
    	
    }  
    
    
    
    public void closeClassLoader() {
		//关闭ClassLoader
		ClassLoaderUtil.releaseLoader((URLClassLoader)classLoader);
    }
	
	
	
	
	
	
}
