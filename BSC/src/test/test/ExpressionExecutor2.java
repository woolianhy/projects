package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.fs.bsc.context.BscContext;
import org.fs.bsc.context.SimpleBscContext;

public class ExpressionExecutor2 {
	private static Map<String, Integer> optionMap = new HashMap<String, Integer>();
	private static Set<String> optionLastCharSet = new HashSet<String>();
	private static final String LEFT_BRACKET = "(";
	private static final String RIGHT_BRACKET = ")";
	private static final String RESULT_TRUE = "TRUE";
	private static final String RESULT_FALSE = "FALSE";
	static{
		optionMap.put("(", 3);
		optionMap.put(")", 3);
		
		optionMap.put("or", 5);
		optionMap.put("and", 4);
		optionMap.put("=", 10);
//		optionMap.put(">=", 10);
//		optionMap.put("<=", 10);
//		optionMap.put(">", 10);
//		optionMap.put("<", 10);
		
		for(String key: optionMap.keySet()){
			optionLastCharSet.add(key.substring(key.length() - 1));
		}
	}
	
	public static boolean execute(String expression, BscContext context){
//		System.out.println("expression: " + expression);
		if(null == expression || "".equals(expression.trim())){
			return true;
		}
		expression = "(" + expression + ")";
		List<String> expList = toList(expression);
//		System.out.println("expression: " + expList);
		//替换变量
		for(int i = 0, len = expList.size(); i < len; i++){
			String exp = expList.get(i);
			if(exp.indexOf("$") != -1){
				String value = "''";
				String contextValue = (String) context.get(exp.substring(1));
				if(contextValue != null){
					value = "'" + contextValue.replace("'", "\\'") + "'";
				}
				expList.set(i, value);
			}
		}
		//FIXME
		System.out.println("expression with value: " + expList);
		
		//计算
		Stack<String> optStack = new Stack<String>();
		Stack<String> valStack = new Stack<String>();
		for(String exp: expList){
			if(LEFT_BRACKET.equals(exp)){
				optStack.push(exp);
			}else if(RIGHT_BRACKET.equals(exp)){
				calculateToLeftBracket(optStack, valStack);
			}else if(optionMap.containsKey(exp)){
				optStack.push(exp);
			}else{
				valStack.push(exp);
			}
		}
		calculateToLeftBracket(optStack, valStack);
		if(valStack.size() != 1){
			throw new RuntimeException("calculate exception: valStack=" + valStack);
		}
		return RESULT_TRUE.equals(valStack.pop());
	}
	
	private static void calculateToLeftBracket(Stack<String> optStack, Stack<String> valStack){
		while(!optStack.isEmpty() && !LEFT_BRACKET.equals(optStack.peek())){
			calculateOne(optStack, valStack);
		}
		if(!optStack.isEmpty() && LEFT_BRACKET.equals(optStack.peek())){
			optStack.pop();
		}
	}
	
	private static void calculateOne(Stack<String> optStack, Stack<String> valStack){
		String opt = optStack.pop();
		if("=".equals(opt)){
			String val2 = valStack.pop();
			String val1 = valStack.pop();
			String result = val1.equals(val2) ? RESULT_TRUE : RESULT_FALSE;
			valStack.push(result);
		}else if("or".equals(opt)){
			String val2 = valStack.pop();
			String val1 = valStack.pop();
			String result = (RESULT_TRUE.equals(val1) || RESULT_TRUE.equals(val2)) ? RESULT_TRUE : RESULT_FALSE;
			valStack.push(result);
		}else if("and".equals(opt)){
			String val2 = valStack.pop();
			String val1 = valStack.pop();
			String result = (RESULT_TRUE.equals(val1) && RESULT_TRUE.equals(val2)) ? RESULT_TRUE : RESULT_FALSE;
			valStack.push(result);
		}
	}
	
	private static List<String> toList(String exp){
		List<String> list = new LinkedList<String>();
		StringBuilder result = new StringBuilder(exp.trim());
		int lastIdx = -1;
		int varBeginIdx = -1;
		boolean isVarContinue = false;
		for(int i = result.length() - 1; i >= 0; i--){
			lastIdx = i;
			if("'".equals(result.substring(i, i + 1)) && (i > 0 && !"\\".equals(result.substring(i - 1, i)))){
				int idx = result.substring(0, i).lastIndexOf("'");
				if(idx != -1){
					i = idx;
				}
				if(varBeginIdx != -1){
					list.add(0, result.substring(i + 1, varBeginIdx + 1).trim());
				}
				list.add(0, result.substring(i, lastIdx + 1).trim());
				isVarContinue = false;
				varBeginIdx = -1;
				continue;
			}
			int optIdx = getOperationIdx(result, i);
			if(optIdx != -1){
				i = optIdx;
				if(varBeginIdx != -1){
					list.add(0, result.substring(i + 1, varBeginIdx + 1).trim());
				}
				list.add(0, result.substring(i, lastIdx + 1).trim());
				isVarContinue = false;
				varBeginIdx = -1;
				continue;
			}
			if(" ".equals(result.substring(i, i + 1))){
				if(varBeginIdx != -1){
					list.add(0, result.substring(i + 1, varBeginIdx + 1).trim());
				}
				isVarContinue = false;
				varBeginIdx = -1;
				continue;
			}
			if(!isVarContinue){
				varBeginIdx = lastIdx;
			}
			isVarContinue = true;
		}
		return list;
	}
	
//	private static String addBlank(String exp){
//		StringBuilder result = new StringBuilder(exp.trim());
//		for(int i = result.length() - 1; i >= 0; i--){
//			if("'".equals(result.substring(i, i + 1)) && (i > 0 && !"\\".equals(result.substring(i - 1, i)))){
//				int idx = result.substring(0, i).lastIndexOf("'");
//				if(idx != -1){
//					i = idx;
//				}
//				continue;
//			}
//			int optIdx = getOperationIdx(result, i);
//			if(optIdx != -1){
//				if(result.length() > i + 1 && !result.substring(i + 1, i + 2).equals(" ")){
//					result.insert(i + 1, " ");
//				}
//				if(optIdx - 1 >= 0 && !result.substring(optIdx - 1, optIdx).equals(" ")){
//					result.insert(optIdx, " ");
//				}
//				i = optIdx;
//			}
//		}
//		return result.toString();
//	}
	
	private static int getOperationIdx(StringBuilder exp, int idx){
		int len = -1;
		String opt = null;
		String lastChar;
		for(String key: optionMap.keySet()){
			len = key.length();
			if(idx + 1 < len){
				continue;
			}
			lastChar = exp.substring(idx, idx + 1);
			if(!optionLastCharSet.contains(lastChar)){
				continue;
			}
			opt = exp.substring(idx - len + 1, idx + 1);
			if(opt.equals(key)){
				return idx - len + 1;
			}
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		BscContext context = new SimpleBscContext();
		context.put("a1", "90");
		context.put("a2", "99");
		context.put("result", "90");
		
		long start = System.currentTimeMillis();
		System.out.println(execute("($a1=$a2)or($result='90')", context));
//		System.out.println(addBlank("(a=b)or('cor'='and=or') and a=b"));
//		System.out.println(toList("(a=bc)or('cor'='and=or') and a=b"));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
