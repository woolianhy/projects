package test;

public class TestA {
	public static void main(String[] args) {
		System.out.println(new String(new byte[]{122}));
		System.out.println('\032');
		
		System.out.println(Integer.MAX_VALUE + 1);
		
		String str = "param.sdf";
		System.out.println(str.substring("param.".length()));
	}
}
