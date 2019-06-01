package com.ksptooi.ASMC.eventManager;

import java.util.ArrayList;
import com.ksptooi.ASMC.Plugins.ASMCPlugin;
import com.ksptooi.ASMC.event.CommandEvent;

public class EventManager {

	//自带的eventHandler
	private EventHandler eh=null;
	
	private ArrayList<EventHandler> eventHandler =new ArrayList<EventHandler>();
	
	public EventManager(){
		eh = new EventHandler();
	}
	
	public void startCommandEvent(CommandEvent ce){
		
		//执行自带的事件处理器
		CommandEvent event=eh.event_onCommand(ce);
		
		
		//执行插件的事件处理器
		for(EventHandler ch:eventHandler){
			
			event = ch.event_onCommand(event);
				
		}
		
		
		//判断是否被取消
		if(event.isCancel()){
			return;
		}
		
		//判断是否已被立即提交
		if(event.isCommit()){
			return;
		}
		
		
		//提交事件	
		event.getCommandType().ExecuteOfType(event.getCommandEntity());
		
	}


	
	//注册事件
	public void regEventHandler(ASMCPlugin plugin,EventHandler eveh) {
		
		
		//注册事件
		
		
		eventHandler.add(eveh);
		
	}



	
	
	
	
	
}
