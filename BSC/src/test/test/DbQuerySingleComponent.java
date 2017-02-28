package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fs.bsc.component.AbstractBscComponent;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.context.BscContext;
import org.fs.db.DbUtil;

public class DbQuerySingleComponent extends AbstractBscComponent {
	
	public DbQuerySingleComponent() {
		// TODO Auto-generated constructor stub
		try {
			DbUtil.init("test.DataSourceDbBean", "Mysql");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String execute(BscContext context) {
		Map<String, Object> params = (Map<String, Object>) context.getExtParam(BscFlowAction.CONTEXT_PARAM_NAME);
		String sql = (String) params.get("sql");
		String resultField = (String) params.get("resultField");
		// TODO Auto-generated method stub
		List<Object> paramList = new ArrayList<Object>();
		String regexp = "(\\$[a-zA-Z0-9]+)|(\\$\\{\\s*[a-zA-Z0-9]+\\s*\\})";
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			String s = m.group();
			String var = s.replaceAll("[\\$\\{\\}\\s]", "");
			m.appendReplacement(sb, "?");
			paramList.add(context.get(var));
		}
		m.appendTail(sb);
		sql = sb.toString();
		System.out.println("sql: " + sql);
		System.out.println("params: " + paramList);
		List list = DbUtil.prepareQueryList(sql, paramList);
		context.put(resultField, list);
		System.out.println(list);
		return RESULT_SUCCESS;
	}

}
