package org.fs.bsc.exec;

import org.fs.bsc.component.BscComponent;

/**
 * BSC服务提供者
 * @author Jingxin.Gao
 *
 */
public interface BscComponentProvider {
	public BscComponent getComponent(String componentCode);
	public void addComponent(String code, BscComponent component);
}
