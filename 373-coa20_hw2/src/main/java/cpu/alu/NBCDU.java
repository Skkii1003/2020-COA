package cpu.alu;

public class NBCDU {

	// 模拟寄存器中的进位标志位
	private String CF = "0";

	// 模拟寄存器中的溢出标志位
	private String OF = "0";

	/**
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return a + b
	 */
	String add(String a, String b) {
		// TODO
		String[] astr = new String[8];
		String[] bstr = new String[8];
		String result = "";
		String go = "0110";
		String sign;
		for (int i = 0; i < 8; i++) {
			astr[i] = a.substring(4 * i, 4 * (i + 1));
			bstr[i] = b.substring(4 * i, 4 * (i + 1));
		}
		if(astr[0].equals(bstr[0])){
			sign = astr[0];
			for (int i = 7; i > 0; i--) {
				String word = add4(astr[i], bstr[i]);
				if (CF.equals("1")) {
					CF = "0";
					word = add4(word, go);
					CF = "1";
				}
				result = word + result;
			}
			result = revise(result);
			result = sign + result;
			if(result.equals("11010000000000000000000000000000"))
				return "11000000000000000000000000000000";
			return result;
		}
		else{
			if(astr[0].equals("1101")) {
				a = "1100" + a.substring(4);
				return sub(a, b);
			}
			else {
				b = "1100" + b.substring(4);
				return sub(b, a);
			}
		}

	}

	/***
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return b - a
	 */
	String sub(String a, String b) {
		// TODO
		String[] astr = new String[8];
		String[] bstr = new String[8];
		for (int i = 0; i < 8; i++) {
			bstr[i] = b.substring(4 * i, 4 * (i + 1));
		}
		String result = "";
		String go = "0110";
		String sign = bstr[0];

		if(bstr[0].equals(a.substring(0,4))){
			a = reverse(a);
			for (int i = 0; i < 8; i++) {
				astr[i] = a.substring(4 * i, 4 * (i + 1));
			}
			for (int i = 7; i > 0; i--) {
				String word = add4(astr[i], bstr[i]);
				if (CF.equals("1")) {
					CF = "0";
					word = add4(word, go);
					CF = "1";
				}
				result = word + result;
			}
			result = revise(result);
			result = sign + result;
			if(result.equals("11010000000000000000000000000000"))
				return "11000000000000000000000000000000";

			if(OF.equals("1")){
				return result;
			}
			else
				return reverse(result);
		}
		else{
			if(bstr[0].equals("1100")) {
				a = "1100" + a.substring(4);
				return add(a, b);
			}
			else {
				a = "1101" + b.substring(4);
				return add(a, b);
			}
		}

	}

	String add4(String a,String b){
		String result = "";
		for(int j = 3;j>=0;j--){
			int abit = (int) a.charAt(j) - '0';
			int bbit = (int) b.charAt(j) - '0';
			int cf = Integer.parseInt(CF);
			int s = abit + bbit + cf;

			if(s > 1){
				s -= 2;
				CF = "1";
			}
			else
				CF = "0";

			result = s + result;
		}
		return result;
	}

	String reverse(String s){
		String[] str = new String[8];
		String result = "";
		String go = "0110";
		for(int i=0;i<8;i++){
			str[i] = s.substring(4 * i, 4 * (i + 1));
		}

		for(int i=7; i>=0;i--){
			String word = "";
			String temp = add4(str[i],go);
			CF = "0";
			for(int j=3;j>=0;j--){
				int bit = temp.charAt(j)=='0' ? 1:0;
				word = bit + word;
			}
			if(i==7) {
				word = add4(word, "0001");
			}
			result = word + result;
		}
		return result;
	}

	String revise(String s){
		String[] str = new String[7];
		String result = "";
		for(int i=0;i<7;i++){
			str[i] = s.substring(4*i,4*(i+1));
		}
		CF = "0";
		for(int i = 6;i>=0;i--){
			String word = str[i];
			if(CF == "1") {
				CF = "0";
				word = add4(word, "0001");
				CF = "0";
			}
			if(word.equals("1111")||word.equals("1110")||word.equals("1101")||word.equals("1100")||word.equals("1011")||word.equals("1010")){
				word = add4(word,"0110");
			}
			result = word + result;
		}
		if(CF=="1")
			OF = "1";
		return result;
	}
//	public static void main(String[] args){
//		System.out.println(sub("11010000000000000000000000000111","11000000000000000000000000000111"));
//	}
}
