package cpu.alu;

import transformer.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 取模、逻辑/算术/循环左右移
 */
public class ALU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    //signed integer mod
    String imod(String src, String dest) {
        // TODO
        //tranform to positive
        String result = "";
        String signs = src.substring(0,1);
        String signd = dest.substring(0,1);
        if(signs.equals("1"))
            src = reverse(src);
        if(signd.equals("1"))
            dest = reverse(dest);

        //dest - src
        String temp;
        while(!dest.equals("00000000000000000000000000000000")){
            temp = sub(src,dest);
            if(temp.charAt(0) =='1'){
                break;
            }
            else
                dest = temp;
        }
        if(dest.equals("00000000000000000000000000000000"))
            return dest;
        else if(signd.equals("1")){
            result = reverse(dest);
        }
        else
            result = dest;
		return result;
    }

    String shl(String src, String dest) {
        // TODO
        String result = "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        for(int i = 0;i < move;i++){
            s.append("0");
            s.delete(0,1);
        }
        result = s.toString();
		return result;
    }

    String shr(String src, String dest) {
        // TODO
        String result = "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        s = s.reverse();
        for(int i = 0;i < move;i++){
            s.append("0");
            s.delete(0,1);
        }
        s = s.reverse();
        result = s.toString();
        return result;
    }

    String sal(String src, String dest) {
        // TODO
        String result = "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        for(int i = 0;i < move;i++){
            s.append("0");
            s.delete(0,1);
        }
        result = s.toString();
        return result;
    }

    String sar(String src, String dest) {
        // TODO
        String result = "";
        String sign = dest.charAt(0) + "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        s = s.reverse();
        for(int i = 0;i < move;i++){
            s.append(sign);
            s.delete(0,1);
        }
        s = s.reverse();
        result = s.toString();
        return result;
    }

    String rol(String src, String dest) {
        // TODO
        String result = "";
        String bit = "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        for(int i = 0;i < move;i++){
            bit = s.charAt(0) + "";
            s.append(bit);
            s.delete(0,1);
        }
        result = s.toString();
        return result;
    }

    String ror(String src, String dest) {
        // TODO
        String result = "";
        String bit = "";
        Transformer t = new Transformer();
        int move = Integer.parseInt(t.binaryToInt(src));
        move = move % 32;
        StringBuilder s = new StringBuilder(dest);
        s = s.reverse();
        for(int i = 0;i < move;i++){
            bit = s.charAt(0) + "";
            s.append(bit);
            s.delete(0,1);
        }
        s = s.reverse();
        result = s.toString();
        return result;
    }

    String add(String src, String dest) {
        // TODO
        int x;
        int y;
        int s;
        String result = "";

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
    String sub(String src, String dest) {
        // TODO
        String result;
        result = add(reverse(src),dest);
        return result;
    }
    String reverse(String s){
        char[] bits = s.toCharArray();
        for(int i = 0;i<32;i++)
            bits[i] = bits[i]=='0' ? '1':'0';
        CF = "0";
        String result = add(String.valueOf(bits),"00000000000000000000000000000001");
        CF = "0";
        result = bits[0] + result.substring(1);
        return result;
    }

//    public static void main(String[] args){
//        System.out.println(imod(reverse("00000000000000000000000000000001"),"00000000000000000000000000000010"));
//    }
}
