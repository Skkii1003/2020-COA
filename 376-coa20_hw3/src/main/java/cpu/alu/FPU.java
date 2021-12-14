package cpu.alu;

import transformer.Transformer;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用4位保护位进行计算，计算完毕直接舍去保护位
 * TODO: 浮点数运算
 */
public class FPU {

    /**
     * compute the float add of (a + b)
     **/
    private String CF = "0";
    private String OF = "0";

    String add(String a,String b){
        // TODO
        //check 0
        if(a.substring(1).equals("0000000000000000000000000000000"))
            return b;
        if (b.substring(1).equals("0000000000000000000000000000000"))
            return a;
        if (a.substring(1).equals("1111111100000000000000000000000"))
            return a;
        if (b.substring(1).equals("1111111100000000000000000000000"))
            return b;

		//initialize
        String signa = a.substring(0,1);
        String signb = b.substring(0,1);
        String sign;
        String expa = a.substring(1,9);
        String expb = b.substring(1,9);
        String exp;
        String zero = "0000";
        String fa = "1" + a.substring(9) + zero;
        String fb = "1" + b.substring(9) + zero;
        String f;
        Transformer t = new Transformer();
        ALU alu = new ALU();

        if(signa.equals(signb)){
            sign = signa;
            int ea = Integer.parseInt(t.binaryToInt("000000000000000000000000"+expa));
            int eb = Integer.parseInt(t.binaryToInt("000000000000000000000000"+expb));
            int move;
            String temp;

            //check exponent
            if(ea > eb){
                move = ea -eb;
                while(move>0){
                    fb = alu.shr("00000000000000000000000000000001",zero + fb).substring(4);
                    move--;
                }
                exp = expa;
                if(fb.equals("0000000000000000000000000000"))
                    return a;
            }
            if (ea < eb){
                move = eb -ea;
                while(move>0){
                    fa = alu.shr("00000000000000000000000000000001",zero + fa).substring(4);
                    move--;
                }
                exp = expb;
                if(fa.equals("0000000000000000000000000000"))
                    return b;
            }
            else
                exp = expa;

            //add fraction
            f = addbin(fa,fb);

            // check whether fraction equals to 0
            if(f.equals("0000000000000000000000000000"))
                return "01111111100000000000000000000000";

            //check whether overflow
            if (OF.equals("1")){
                exp = addbin("0000000000000000000000000001","00000000000000000000" + exp);
                exp = exp.substring(20);
                if (exp.equals("11111111"))
                    return sign + exp + "00000000000000000000000";
                f = "1" + alu.shr("00000000000000000000000000000001",zero + f).substring(5);
            }

            //normalize result
            while(f.charAt(0)=='0'){
                f = alu.shl("00000000000000000000000000000001",zero + f).substring(4);
                exp = subbin("0000000000000000000000000001","00000000000000000000"+exp);
                if(exp.charAt(0)=='1')
                    return "01111111100000000000000000000000";
                exp = exp.substring(20);
            }
            f = f.substring(1,24);
            return sign + exp + f;
        }
        else{
            if(signa.equals("1")){
                a = signb + a.substring(1);
                return sub(b,a);
            }
            else
                b = signa + b.substring(1);
            return sub(a,b);
        }
    }

    /**
     * compute the float add of (a - b)
     **/
    String sub(String a,String b){
        // TODO
        String signa = a.substring(0,1);
        String signb = b.substring(0,1);
        String sign;
        String expa = a.substring(1,9);
        String expb = b.substring(1,9);
        String exp;
        String zero = "0000";
        String fa = "1" + a.substring(9) + zero;
        String fb = "1" + b.substring(9) + zero;
        String f;
        Transformer t = new Transformer();
        ALU alu = new ALU();

        //check 0
        if (a.substring(1).equals("0000000000000000000000000000000")){
            signb = b.charAt(0)=='0' ? "1":"0";
            return signb + b.substring(1);
        }
        if (b.substring(1).equals("0000000000000000000000000000000"))
            return a;

        if (a.substring(1).equals("1111111100000000000000000000000"))
            return a;
        if (b.substring(1).equals("1111111100000000000000000000000")){
            signb = b.charAt(0)=='0' ? "1":"0";
            return signb + b.substring(1);
        }

        if(signa.equals(signb)){
            sign = signa;
            int ea = Integer.parseInt(t.binaryToInt("000000000000000000000000"+expa));
            int eb = Integer.parseInt(t.binaryToInt("000000000000000000000000"+expb));
            int move;
            String temp;

            //check exponent
            if(ea > eb){
                move = ea -eb;
                while(move>0){
                    fb = alu.shr("00000000000000000000000000000001",zero + fb).substring(4);
                    move--;
                }
                exp = expa;
                if(fb.equals("0000000000000000000000000000"))
                    return a;
            }
            if (ea < eb){
                move = eb -ea;
                while(move>0){
                    fa = alu.shr("00000000000000000000000000000001",zero + fa).substring(4);
                    move--;
                }
                exp = expb;
                if(fa.equals("0000000000000000000000000000")){
                    signb = b.charAt(0)=='0' ? "1":"0";
                    return signb + b.substring(1);
                }
            }
            else
                exp = expa;

            //add fraction;
            f = addbin(fa,reverse(fb));

            if (OF.equals("0")){
                f = reverse(f);
                sign = sign.equals("0") ? "1" : "0";
            }

            // check whether fraction equals to 0
            if(f.equals("0000000000000000000000000000"))
                return "00000000000000000000000000000000";

            //normalize result
            while(f.charAt(0)=='0'){
                f = alu.shl("00000000000000000000000000000001",zero + f).substring(4);
                exp = subbin("0000000000000000000000000001","00000000000000000000"+exp);
                if(exp.charAt(0)=='1')
                    return "01111111100000000000000000000000";
                exp = exp.substring(20);
            }
            f = f.substring(1,24);
            return sign + exp + f;
        }
        else{
            b = signa + b.substring(1);
            return add(a,b);
        }
    }

    String addbin(String src, String dest) {
        // TODO
        int x;
        int y;
        int s;
        CF = "0";
        String result = "";

        for(int i=27;i>=0;i--){
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
        if(CF.equals("1"))
            OF = "1";

        return result;
    }

    String subbin(String src, String dest) {
        // TODO
        String result;
        result = addbin(reverse(src),dest);
        return result;
    }

    String reverse(String s){
        char[] bits = s.toCharArray();

//        if(bits.length!=28)
//            System.out.println(s);

        for(int i = 0;i<28;i++)
            bits[i] = bits[i]=='0' ? '1':'0';

        CF = "0";
        String result = addbin(String.valueOf(bits),"0000000000000000000000000001");
        CF = "0";
        return result;
    }

//    public static void main(String[] args){
//        System.out.println(sub("01000000111011011100000000000000","01000001110101100000000000000000"));
//    }
}
