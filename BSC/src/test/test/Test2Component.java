package test;

import org.fs.bsc.component.AbstractBscComponent;
import org.fs.bsc.context.BscContext;

public class Test2Component extends AbstractBscComponent {

	@Override
	public String execute(BscContext context) {
		// TODO Auto-generated method stub
		context.put("result", "99");
		System.out.println("tttest2!");
//		if(true){
//			throw new RuntimeException("asdfsdf");
//		}
		return null;
	}
}
