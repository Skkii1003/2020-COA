package transformer;


import javax.print.DocFlavor;

//
public class Transformer {
    /**
     * Integer to binaryString
     *
     * @param numStr to be converted
     * @return result
     */
    public String intToBinary(String numStr) {
        //TODO:
        int num = Integer.parseInt(numStr);
        String result = Integer.toBinaryString(num);
        while(result.length() < 32){
            result = "0" + result;
        }
        return result;
    }

    /**
     * BinaryString to Integer
     *
     * @param binStr : Binary string in 2's complement
     * @return :result
     */
    public String binaryToInt(String binStr) {
        //TODO:
        char[] bin = binStr.toCharArray();
        char sign = bin[0];
        //System.out.println(sign);
        int result = 0;
        if(sign == '1'){
            for(int i=0;i<32;i++){
                bin[i] = (bin[i]=='0') ? '1' : '0';
            }
        }
        for(int i=31;i>0;i--){
            result += (int) Math.pow(2,31-i) * (int) (bin[i]-'0');
        }
        //System.out.println(bin[1]);
        if(sign == '0')
            return result+"";
        else
            return "-"+ (result+1);
    }

    /**
     * Float true value to binaryString
     * @param floatStr : The string of the float true value
     * */
    public String floatToBinary(String floatStr) {
        //TODO:
        String result;
        float f = Float.parseFloat(floatStr);
        int bits = Float.floatToIntBits(f);
        result = Integer.toBinaryString(bits);
        if(bits==0x7f800000)
            return "+Inf";
        if(bits==0xff800000)
            return "-Inf";
        if(bits<0){
            while(result.length()<32){
                result = "1" + result;
            }
        }
        else{
            while(result.length()<32){
                result = "0" + result;
            }
        }
        return result;
    }

    /**
     * Binary code to its float true value
     * */
    public String binaryToFloat(String binStr) {
        //TODO:
        if(binStr=="00000000000000000000000000000000")
            return "0.0";
        else if(binStr=="01111111100000000000000000000000")
            return "+Inf";
        else if(binStr=="11111111100000000000000000000000")
            return "-Inf";
        else{
            String sign = binStr.substring(0,1);
            String exponent = binStr.substring(1,9);
            String fraction = binStr.substring(9);
            String result = "";

            if(sign.equals("0"))
                sign = "";
            else
                sign = "-";

            String biased = binaryToInt("000000000000000000000000"+exponent);
            int real;
            double f;
            if(biased.equals("0")){
                real = Integer.parseInt(biased) - 126;
                f = 0.0d;
            }
            else{
                real = Integer.parseInt(biased) - 127;
                f = 1.0d;
            }
            for(int i=-1;i>=-23;i--){
                int size = (int) fraction.charAt(-i-1) - '0';
                f += Math.pow(2,i) * (double)size;
            }
            result = sign + f * Math.pow(2,real);
            return result;
        }
    }

    /**
     * The decimal number to its NBCD code
     * */
    public String decimalToNBCD(String decimal) {
        //TODO:
       int num = Integer.parseInt(decimal);
       String result = "";
       String sign;
       if(num>=0)
           sign = "1100";
       else {
           sign = "1101";
           decimal = decimal.substring(1);
       }
       for(int i=0;i<decimal.length();i++){
           int size = (int) decimal.charAt(i) - '0';
           String bits = Integer.toBinaryString(size);
           while(bits.length()<4)
               bits = "0"+ bits;
           result = result + bits;
       }
       while(result.length()<28){
           result = "0000" + result;
       }
       result = sign + result;
       return result;
    }

    /**
     * NBCD code to its decimal number
     * */
    public String NBCDToDecimal(String NBCDStr) {
        //TODO:
        String[] nbcd = new String[8];
        String sign;
        String result = "";

        for(int i=0;i<8;i++){
            nbcd[i] = NBCDStr.substring(4*i,4*(i+1));
        }
        if(nbcd[0].equals("1100"))
            sign = "";
        else
            sign = "-";

        for(int i=1;i<8;i++){
            int n=0;
            for(int j=0;j<4;j++){
                int num = (int) nbcd[i].charAt(j) - '0';
                int size = (int) Math.pow(2,3-j) * num;
                n += size;
            }
            result = result + n + "";
        }
        if(result.equals("0000000"))
            return "0";

        while(result.charAt(0) == '0'){
            result = result.substring(1);
        }

        result = sign + result;
        return result;
    }


//    public static void main(String[] args){
//        System.out.println(binaryToInt("00000000000000000000000000000001"));
//    }


}
