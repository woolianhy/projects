package org.fs.bsc.component.flow.loader;

import java.util.Comparator;

import org.fs.bsc.component.flow.BscFlowDirection;

public class DirectionComparator implements Comparator<BscFlowDirection>{

	@Override
	public int compare(BscFlowDirection o1, BscFlowDirection o2) {
		int v1 = 0;
		int v2 = 0;
		
		String exp1 = o1.getExpression();
		if(exp1 != null && !"".equals(exp1.trim())){
			v1++;
		}
		String exp2 = o2.getExpression();
		if(exp2 != null && !"".equals(exp2.trim())){
			v2++;
		}
		return v2 - v1;
	}

}
