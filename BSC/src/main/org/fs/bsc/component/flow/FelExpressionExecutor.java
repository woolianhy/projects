package org.fs.bsc.component.flow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fs.bsc.context.BscContext;
import org.fs.bsc.context.SimpleBscContext;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.function.Function;

public class FelExpressionExecutor implements ExpressionExecutor {
	
	private final FelEngine FEL = new FelEngineImpl();
	
	public void addFunction(Function func){
		FEL.addFun(func);
	}
	
	public boolean execute(String expression, BscContext context){
		FelContext ctx = new MapContext();
		String regexp = "(\\$[a-zA-Z0-9]+)|(\\$\\{\\s*[a-zA-Z0-9]+\\s*\\})";
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(expression);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			String s = m.group();
			String var = s.replaceAll("[\\$\\{\\}\\s]", "");
			String key = "_Θ" + var + "Θ_";
			ctx.set(key, context.get(var));
//			System.out.printf("var: %s, key: %s, value: %s\n", var, key, context.get(var));//FIXME
			m.appendReplacement(sb, key);
		}
		m.appendTail(sb);
		String finalExp = sb.toString();
//		System.out.println("exp: " + finalExp);//FIXME
		Boolean result = (Boolean) FEL.eval(finalExp, ctx);
		return result;
	}
	
	public static void main(String[] args) {
//		for(int i = 0; i < 10; i++){
		BscContext context = new SimpleBscContext();
		context.put("a1", "90");
		context.put("a2", "99");
		context.put("result", "90");
		
		long start = System.currentTimeMillis();
		boolean result = new FelExpressionExecutor().execute("($a1==$a2)||($result=='90')", context);
		System.out.println("result: " + result);
		long end = System.currentTimeMillis();
		System.out.println("use: " + (end - start));
//		}
	}
}
