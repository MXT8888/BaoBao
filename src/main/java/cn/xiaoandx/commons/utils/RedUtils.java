package cn.xiaoandx.commons.utils;

public class RedUtils {

	public static Double getRed(double money , int redNum) {
		money=money*100;
		if(redNum-1 == 0) {
				return Double.valueOf(String.format("%.2f", money/100.00));
		}
		
		while(true) {
			
			int flag = (1+(int)(Math.random()*(money*100)));
			double bounty = (money-flag)/(redNum-1);
			if(bounty>=1) {
				double red = flag/100.0;
				return red;
			}

		}
	}
	public static void main(String[] args) {
		double mm = 5.00;
		for(int i=5;i>=1;i--) {
			double red = RedUtils.getRed(mm, i);
			System.out.println("red:"+red);
			mm=mm-red;
		}		
	}
}
