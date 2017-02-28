package test;

import org.fs.bsc.component.AbstractBscComponent;
import org.fs.bsc.context.BscContext;

public class Test1Component extends AbstractBscComponent {

	@Override
	public String execute(BscContext context) {
		// TODO Auto-generated method stub
		context.put("result", "90");
		System.out.println("tttest1!");
//		if(true){
//			throw new RuntimeException("asdfsdf");
//		}
		return null;
	}

}
