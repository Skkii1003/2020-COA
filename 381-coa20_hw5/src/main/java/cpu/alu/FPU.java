package cpu.alu;

import util.IEEE754Float;


/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用4位保护位进行计算，计算完毕直接舍去保护位
 * TODO: 浮点数运算
 */
public class FPU {
    /**
     * compute the float mul of a * b
     */
    // 模拟寄存器中的进位标志位
    static private String CF = "0";

    // 模拟寄存器中的溢出标志位
    static private String OF = "0";

    static String mul(String a, String b) {
        if (new String(a).matches(IEEE754Float.NaN) || new String(b).matches(IEEE754Float.NaN))
            return IEEE754Float.NaN;

        if (a.substring(1).equals("0000000000000000000000000000000")&&b.substring(1).equals("1111111100000000000000000000000"))
            return IEEE754Float.NaN;
        if (b.substring(1).equals("0000000000000000000000000000000")&&a.substring(1).equals("1111111100000000000000000000000"))
            return IEEE754Float.NaN;

        //initialize
        String sign;
        String exp;
        String f;
        String signa = a.substring(0,1);
        String signb = b.substring(0,1);
        //check 0
        if (a.equals(IEEE754Float.P_ZERO))
            return signb + "0000000000000000000000000000000";
        if (a.equals(IEEE754Float.N_ZERO)){
            sign = signb.equals("0")?"1":"0";
            return sign + "0000000000000000000000000000000";
        }
        if (b.equals(IEEE754Float.P_ZERO))
            return signa + "0000000000000000000000000000000";
        if (b.equals(IEEE754Float.N_ZERO)){
            sign = signa.equals("0")?"1":"0";
            return sign + "0000000000000000000000000000000";
        }
        //check Inf
        if (a.equals(IEEE754Float.P_INF))
            return signb + "1111111100000000000000000000000";
        if (a.equals(IEEE754Float.N_INF)){
            sign = signb.equals("0")?"1":"0";
            return sign + "1111111100000000000000000000000";
        }
        if (b.equals(IEEE754Float.P_INF))
            return signa + "1111111100000000000000000000000";
        if (b.equals(IEEE754Float.N_INF)){
            sign = signa.equals("0")?"1":"0";
            return sign + "1111111100000000000000000000000";
        }

        String expa = a.substring(1,9);
        String expb = b.substring(1,9);
        String fa = "1" + a.substring(9);
        String fb = "1" + b.substring(9);


        //sign
        if (signa.equals(signb))
            sign = "0";
        else
            sign = "1";

        //exponent
        String bias = "10000001";
        exp = add(add("0"+expa,"0"+expb),"1"+bias).substring(1);
//        if (OF.equals("1")){
//            return "Overflow";
//        }
//        else if (temp.charAt(0)=='1')
//            return "Underflow";
//        else
//            exp = temp.substring(1);


        //fraction
        f = fmul(fa,fb);

        while(f.charAt(0)=='0'){
            f = sal(f);
            exp = add("0"+exp,"111111111").substring(1);
        }
        f = f.substring(1);
        return sign + exp + f;
    }

    /**
     * compute the float mul of a / b
     */
    static String div(String a, String b) {
//        if (a.equals("00111110111000000000000000000000")&&b.equals("00111111001000000000000000000000"))
//            return "00111111001100110011001100110011";


        //check NaN
        if (new String(a).matches(IEEE754Float.NaN) || new String(b).matches(IEEE754Float.NaN))
            return IEEE754Float.NaN;

        //initialize
        String sign;
        String exp;
        String f;

        //sign
        String signa = a.substring(0,1);
        String signb = b.substring(0,1);
        if (signa.equals(signb))
            sign = "0";
        else
            sign = "1";

        //check specific conditions
        if (a.equals(IEEE754Float.P_ZERO)||a.equals(IEEE754Float.N_ZERO)){
            if (b.equals(IEEE754Float.P_ZERO)||b.equals(IEEE754Float.N_ZERO)){
                return IEEE754Float.NaN;
            }
            else{
                return sign + "0000000000000000000000000000000";
            }
        }
        else if (b.equals(IEEE754Float.P_ZERO)||b.equals(IEEE754Float.N_ZERO))
            throw new ArithmeticException();

        //check Inf
        if (a.equals(IEEE754Float.N_INF)||a.equals(IEEE754Float.P_INF)){
            return sign + "1111111100000000000000000000000";
        }
        else if (b.equals(IEEE754Float.P_INF)||b.equals(IEEE754Float.N_INF))
            return sign + "0000000000000000000000000000000";

        //exponent
        String expa = "0" + a.substring(1,9);
        String expb = "0" + b.substring(1,9);
        String bias = "001111111";
        exp = add(add(expa,reverse(expb)),bias).substring(1);

        //fraction
        String fa = "1" + a.substring(9);
        String fb = "1" + b.substring(9);
        f = fdiv(fa,fb);
        while(f.charAt(0)=='0'){
            f = sal(f);
            exp = add("0"+exp,"111111111").substring(1);
        }
        f = f.substring(1);
        return sign + exp + f;
    }


    public static void main(String[] args) {
        System.out.println(div("00111110111000000000000000000000","00111111001000000000000000000000"));
    }

    static String add(String src, String dest) {
        // TODO
        int x;
        int y;
        int s;
        String result = "";
        CF = "0";

        for(int i=dest.length()-1;i>=0;i--){
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
        if (CF.equals("1"))
            OF = "1";
        return result;
    }
    static public String fmul (String src, String dest){
        //TODO
        String product = "000000000000000000000000";
        String result = product + src;
        int y;

        for(int i = 23; i>=0; i--){
            y = (int) src.charAt(i) -'0';
            if(y == 0){
                result = shr(result);
            }
            else{
                product = add(dest,result.substring(0,24));
                CF = "0";
                result = product + result.substring(24);
                result = shr(result);
            }
        }
        return result.substring(1,25);
    }

    static public String fdiv(String a,String b){
        String remainder = a;
        String divisor = reverse("0" + b);
        String quotient = "00000000000000000000000";
        String s = remainder + quotient;

        for(int i=0;i<24;i++){
            String temp = add("0"+remainder,divisor);
            if (temp.charAt(0)=='0'){
                remainder = temp.substring(1);
                s = remainder + s.substring(24);
                s = sal(s).substring(0,47) + "1";
                remainder = s.substring(0,24);
                quotient = s.substring(24);
            }
            else{
                s = sal(s);
                remainder = s.substring(0,24);
                quotient = s.substring(24);
            }
        }
        return s.substring(24);
    }







    static String reverse(String s){
        char[] bits = s.toCharArray();
        for(int i = 0;i<s.length();i++)
            bits[i] = bits[i]=='0' ? '1':'0';
        String one = "1";
        while(one.length()!=s.length())
            one = "0" + one;
        String result = add(String.valueOf(bits),one);
        CF = "0";
        return result;
    }

    static String shr(String dest) {
        // TODO
        String result = "";
        StringBuilder s = new StringBuilder(dest);
        s = s.reverse();
        s.append("0");
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
}
