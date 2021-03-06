package com.ksptooi.asmc.service.plugins;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import com.ksptooi.asmc.common.PluginClassLoader;
import com.ksptooi.asmc.entity.plugins.ExternalPlugin;
import com.ksptooi.asmc.entity.plugins.ExternalPluginFile;
import com.ksptooi.asmc.entity.plugins.LoadedPlugin;
import com.ksptooi.asmc.filter.plugin.jarFilter;
import com.ksptooi.asmc.main.Asmc;
import com.ksptooi.asmc.message.Logger;
import com.ksptooi.generaldatacore.dataInteface.InputStreamConnection;
import com.ksptooi.generaldatacore.entity.data.DataSet;

public class CorePluginLoader implements CorePluginLoaderService{

	
	private Logger log = Asmc.getLogger();

	
	public CorePluginLoader() {
		log.info("初始化内部组件 - CorePluginLoaderService[CPLS]");
	}

	
	@Override
	public ArrayList<ExternalPluginFile> searchPlugins(File folder) {

		log.info("·CorePluginLoader - 正在搜寻..");

		ArrayList<ExternalPluginFile> asmcPluginList = new ArrayList<ExternalPluginFile>();
		
		try {
		
			//创建插件文件夹
			folder.mkdirs();
		
			//取所有插件
			File[] pluginList = folder.listFiles(new jarFilter());
			
//			File[] pluginList = Project.pluginFolder.listFiles(new JarFilter());
			
			
			//遍历获取插件信息
			for(File f:pluginList){
			
				ExternalPluginFile asmcPlugin = new ExternalPluginFile();
				
				
				log.info("获取:"+f.getName());
			
			
				URL url=new URL("jar:file:/"+f.getPath()+"!/ASMC_Plugin.gd");
				
				//获取插件文件流
				InputStream is=url.openStream();

				DataSet dataSet = new InputStreamConnection(is,"UTF-8").getDataSet().setAutomatic(false);

				//将文件中的信息加入插件实体
				asmcPlugin.setMainClass(dataSet.val("Plugin_Main"));
				asmcPlugin.setName(dataSet.val("Plugin_Name"));
				asmcPlugin.setFile(f);

				
				asmcPluginList.add(asmcPlugin);
			}
			
		
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		return asmcPluginList;
		
	}

	
	
	
	@Override
	public LoadedPlugin loadPlugin(ExternalPluginFile plugin) {
		
		PluginClassLoader loader = null;
		
		LoadedPlugin lap = new LoadedPlugin();
		
		loader = new PluginClassLoader(plugin.getFile());
		
		
		lap.setAsmcPlugin((ExternalPlugin)loader.loadClass(plugin.getMainClass()));
		lap.setName(plugin.getName());
		lap.setFile(plugin.getFile());
		lap.setMainClass(plugin.getMainClass());
		
		//设置插件名
		lap.getAsmcPlugin().setPluginName(plugin.getName());
		
		return lap;
	}
	
	
	
	

}
