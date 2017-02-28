package org.fs.bsc.event;

/**
 * BSC事件管理器
 * @author Jingxin.Gao
 *
 */
public class BscEventManager {
	public void publishEvent(BscEvent event){
		//FIXME process event
		System.out.println("[" + Thread.currentThread() + "]" + event);
	}
}
