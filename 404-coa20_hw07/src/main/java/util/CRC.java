package util;

import cpu.alu.ALU;
import memory.Disk;

/**
 * @CreateTime: 2020-11-23 22:13
 */
public class CRC {

    /**
     * CRC计算器
     * @param data 数据流
     * @param polynomial 多项式
     * @return CheckCode
     */
    public static char[] Calculate(char[] data, String polynomial) {
        //TODO
        ALU a = new ALU();
        String s = "";
        String divide;
        for (int i = 0;i < data.length;i++)
            s = s + data[i];
        for (int i = 0; i<polynomial.length()-1;i++)
            s = s + "0";
        String temp = s.substring(0,polynomial.length()-1);

        for (int i=0;i<data.length;i++){
            char next = s.charAt(polynomial.length()+i-1);
            if (temp.charAt(0)=='1')
                divide = polynomial;
            else
                divide = a.xor(polynomial,polynomial);
            temp = a.xor(temp+next,divide).substring(1);
        }
        return temp.toCharArray();
    }
    public static void main(String[] args){
//        String datas = "0100011";
//        String checkcode = "1001";
        char[] data=new char[512];
        data[0] = (char) 0;
        for(int i=1;i<data.length;++i){
            data[i]=(char)((i-1)%128);
        }

//        char[] data=new char[511];
//        for(int i=0;i<data.length;++i){
//            data[i]=(char)(i%128);
//        }

        //System.out.println(Check(data.toCharArray(),"1001",checkcode.toCharArray()));
        System.out.println(Disk.ToByteStream(Calculate(Disk.ToBitStream(data),"11000000000100001")));
        //System.out.println(Calculate(datas.toCharArray(),checkcode));
    }

    /**
     * CRC校验器
     * @param data 接收方接受的数据流
     * @param polynomial 多项式
     * @param CheckCode CheckCode
     * @return 余数
     */
    public static char[] Check(char[] data, String polynomial, char[] CheckCode){
        //TODO
        String s = "";
        for (char datum : data) s = s + datum;
        for (char c : CheckCode) s = s + c;
        return Calculate(s.toCharArray(),polynomial);
    }

    /**
     * 这个方法仅用于测试，请勿修改
     * @param data
     * @param polynomial
     */
    public static void CalculateTest(char[] data, String polynomial){
        System.out.print(Calculate(data, polynomial));
    }
    /**
     * 这个方法仅用于测试，请勿修改
     * @param data
     * @param polynomial
     */
    public static void CheckTest(char[] data, String polynomial, char[] CheckCode){
        System.out.print(Check(data, polynomial, CheckCode));
    }
}
