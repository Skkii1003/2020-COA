package cpu.alu;

import util.BinaryIntegers;
import util.IEEE754Float;
import java.lang.ArithmeticException;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 乘除
 */
public class ALU {

	// 模拟寄存器中的进位标志位
    static private String CF = "0";

    // 模拟寄存器中的溢出标志位
    static private String OF = "0";


	/**
	 * 返回两个二进制整数的乘积(结果低位截取后32位)
	 * @param src 32-bits
	 * @param dest 32-bits
	 * @return 32-bits
	 */
	static public String mul (String src, String dest){
		//TODO
		String product = BinaryIntegers.ZERO;
		String negative = reverse(dest);
		src = src + "0";
		String result = product + src;
		int y;

		for(int i = 32; i>0; i--){
			y = (int) src.charAt(i) - src.charAt(i-1);
			if(y == 0){
				result = sar(result);
			}
			else if (y == 1){
				CF = "0";
				product = add(dest,result.substring(0,32));
				CF = "0";
				result = product + result.substring(32);
				result = sar(result);
			}
			else{
				CF = "0";
				product = add(negative,result.substring(0,32));
				CF = "0";
				result = product + result.substring(32);
				result = sar(result);
			}
		}
		result = result.substring(32,64);
	    return result;
    }
    public static void main(String[] args){
		System.out.println(div("11111111111111111111111111110110","00000000000000000000000000000110"));
		//System.out.println(div("11111111111111111111111111111000","00000000000000000000000000000010"));
//		System.out.println(reverse("11111111111111111111111011110101"));
//		System.out.println(reverse("00000000000000000000111001111111"));
	}

    /**
     * 返回两个二进制整数的除法结果 operand1 ÷ operand2
     * @param operand1 32-bits
     * @param operand2 32-bits
     * @return 65-bits overflow + quotient + remainder
     */
    public static String div(String operand1, String operand2) {
    	//TODO
		if (operand1.equals(IEEE754Float.P_ZERO)){
			if (operand2.equals(IEEE754Float.P_ZERO)){
				return "NaN";
			}
			else
				return "0" + operand1 + operand1;
		}
		else if (operand1.equals(IEEE754Float.N_ZERO)){
			if (operand2.equals("11111111111111111111111111111111"))
				return "11000000000000000000000000000000000000000000000000000000000000000";
		}
		else{
			if (operand2.equals(IEEE754Float.P_ZERO))
				throw new ArithmeticException();
		}
		if (add(operand1,operand2).charAt(0)!=operand1.charAt(0))
			return OF + IEEE754Float.P_ZERO + operand1;

		String remainder = "";
		String quotient = remove(operand1);
		String divisor = operand2;
		String result = "";
		int size = quotient.length();
		while(remainder.length()!= size)
			remainder += quotient.charAt(0);
		String s = remainder + quotient;


		if (remainder.charAt(0)==divisor.charAt(0)){
			remainder = add(reverse(divisor),extend(remainder)).substring(32-size);
			s = remainder + s.substring(size);
		}
		else{
			remainder = add(divisor,extend(remainder)).substring(32-size);
			s = remainder + s.substring(size);
		}

		for (int i=0;i<size;i++){
			if (remainder.charAt(0) == divisor.charAt(0)){
				s = sal(s).substring(0,size*2-1) + "1";
				remainder = s.substring(0,size);
				quotient = s.substring(size);
				remainder = add(reverse(divisor),extend(remainder)).substring(32-size);
				s = remainder + quotient;
			}
			else{
				s = sal(s);
				remainder = s.substring(0,size);
				quotient = s.substring(size);
				remainder = add(divisor,extend(remainder)).substring(32-size);
				s = remainder + quotient;
			}
		}
		quotient = s.substring(size);
		String bit = remainder.charAt(0)==divisor.charAt(0) ? "1" : "0";
		quotient = sal(quotient).substring(0,size-1) + bit;

		if (operand1.charAt(0) != divisor.charAt(0) && quotient.charAt(0) == '1'){
			quotient = add(extend(quotient),"00000000000000000000000000000001").substring(32-size);
		}

		if (remainder.charAt(0)!=operand1.charAt(0)){
			if (operand1.charAt(0)==divisor.charAt(0)) {
				remainder = add(divisor, extend(remainder)).substring(32 - size);
			}
			else {
				remainder = add(reverse(divisor), extend(remainder)).substring(32 - size);
			}
		}
		String temp;
		if(remainder.charAt(0)==divisor.charAt(0)){
			temp = add(reverse(divisor),extend(remainder));
			if(temp.equals(IEEE754Float.P_ZERO)){
				remainder = temp;
				quotient = add(extend(quotient),"00000000000000000000000000000001").substring(32-size);
			}
		}
		else{
			temp = add(divisor,extend(remainder));
			if(temp.equals(IEEE754Float.P_ZERO)){
				remainder = temp;
				quotient = add(extend(quotient),"11111111111111111111111111111111").substring(32-size);
			}
		}
		result = OF + extend(quotient) + extend(remainder);
        return result;
    }

	static String reverse(String s){
		char[] bits = s.toCharArray();
		for(int i = 0;i<32;i++)
			bits[i] = bits[i]=='0' ? '1':'0';
		CF = "0";
		String result = add(String.valueOf(bits),"00000000000000000000000000000001");
		CF = "0";
		result = bits[0] + result.substring(1);
		return result;
	}

	static String add(String src, String dest) {
		// TODO
		int x;
		int y;
		int s;
		String result = "";
		CF = "0";

		for(int i=31;i>=0;i--){
			x = (int) src.charAt(i) - '0';
			y = (int) dest.charAt(i) - '0';
			if((x + y + Integer.parseInt(CF))<2){
				s = x + y + Integer.parseInt(CF);
				CF = "0";
			}
			else{
				s = x + y + Integer.parseInt(CF) - 2;
				CF = "1";
			}
			result = s + result;
		}
		if(result.equals("10000000000000000000000000000000"))
			return "00000000000000000000000000000000";
		return result;
	}

	static String sar(String dest) {
		// TODO
		String result = "";
		String sign = dest.charAt(0) + "";
		StringBuilder s = new StringBuilder(dest);
		s = s.reverse();
		s.append(sign);
		s.delete(0,1);
		s = s.reverse();
		result = s.toString();
		return result;
	}

	static String sal(String dest) {
		String result = "";
		StringBuilder s = new StringBuilder(dest);
		s.append("0");
		s.delete(0,1);
		result = s.toString();
		return result;
	}
	static String remove(String s){
    	StringBuilder a = new StringBuilder(s);
    	if (s.charAt(0)=='0'){
    		while(a.charAt(0)!='1'){
    			a.delete(0,1);
			}
    		a = a.reverse();
    		a = a.append("0");
    		a = a.reverse();
		}
    	else{
    		s = reverse(s);
			StringBuilder b = new StringBuilder(s);
			while(b.charAt(0)!='1'){
				b.delete(0,1);
			}
			b = b.reverse();
			b = b.append("0");
			b = b.reverse();
			int size = b.length();
			a.delete(0,32-size);
		}
    	return a.toString();
	}

	static String extend(String s){
    	int size = s.length();
    	String sign = s.substring(0,1);
    	while(s.length()!=32){
    		s = sign + s;
		}
    	return s;
	}
}
