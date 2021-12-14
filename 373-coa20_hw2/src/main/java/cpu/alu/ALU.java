package cpu.alu;

import transformer.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减与逻辑运算
 */
public class ALU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    //add two integer
    String add(String src, String dest) {
        // TODO
        int x;
        int y;
        int s;
        String result = "";

        for(int i=31;i>=0;i--){
            x = Integer.parseInt(src.substring(i,i+1));
            y = Integer.parseInt(dest.substring(i,i+1));
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
        return result;
    }

    //sub two integer
    // dest - src
    String sub(String src, String dest) {
        // TODO
        String reverse = "";
        String result;
        for(int i = 0;i<32;i++){
            char s = src.charAt(i)=='0' ? '1' : '0';
            reverse += String.valueOf(s);
        }
        reverse = add(reverse,"00000000000000000000000000000001");
        result = add(reverse,dest);
		return result;
	}

    String and(String src, String dest) {
        // TODO
        String result = "";
        for(int i=0;i<32;i++){
            if(src.charAt(i)=='0'||dest.charAt(i)=='0'){
                result += "0";
            }
            else
                result += "1";
        }
		return result;
    }

    String or(String src, String dest) {
        // TODO
        String result = "";
        for(int i=0;i<32;i++){
            if(src.charAt(i)=='1'||dest.charAt(i)=='1'){
                result += "1";
            }
            else
                result += "0";
        }
        return result;
    }

    String xor(String src, String dest) {
        // TODO
        String result = "";
        for(int i=0;i<32;i++){
            result += src.charAt(i)==dest.charAt(i) ? "0":"1";
        }
        return result;
    }

}
