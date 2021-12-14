package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import memory.cacheReplacementStrategy.FIFOReplacement;
import transformer.Transformer;

public class SetAssociativeMapping extends MappingStrategy{


    Transformer t = new Transformer();
    private static int SETS=512; // 共256个组
    private static int setSize=2;   // 每个组4行


    /**
     * 该方法会被用于测试，请勿修改
     * @param SETS
     */
    public void setSETS(int SETS) {
        this.SETS = SETS;
    }

    /**
     * 该方法会被用于测试，请勿修改
     * @param setSize
     */
    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }

    /**
     *
     * @param blockNO 内存数据
     *                块的块号
     * @return cache数据块号 22-bits  [前14位有效]
     */

    @Override
    public char[] getTag(int blockNO) {
        //TODO
        Transformer t = new Transformer();
        String No = t.intToBinary(blockNO + "");
        String tag = No.substring(10,24) + "00000000";
        return tag.toCharArray();
    }

    /**
     *
     * @param blockNO 目标数据内存地址前22位int表示
     * @return -1 表示未命中
     */
    @Override
    public int map(int blockNO) {
        //TODO
        Transformer t = new Transformer();
        String No = t.intToBinary(blockNO + "");
        String tag;
        int group = blockNO % SETS;
        int start = group * setSize;
        int end = (group + 1) * setSize - 1;
        if (SETS==1)
            tag = No.substring(10);
        else if (start == end)
            tag = No.substring(10,22) + "0000000000";
        else
            tag = No.substring(10,24) + "00000000";
        FIFOReplacement fifoReplacement = new FIFOReplacement();
        return fifoReplacement.isHit(start,end,tag.toCharArray());
    }

    @Override
    public int writeCache(int blockNO) {
        //TODO
        Memory memory = Memory.getMemory();
        Transformer t = new Transformer();
        String No = t.intToBinary(blockNO + "");
        String eip = No.substring(10) + "0000000000";
        char[] input = memory.read(eip,Cache.LINE_SIZE_B);
        char[] tag;
        int group = blockNO % SETS;
        int start = group * setSize;
        int end = (group + 1) * setSize - 1;
        if (SETS==1)
            tag = eip.substring(0,22).toCharArray();
        else if(start==end)
            tag = (eip.substring(0,12) + "0000000000").toCharArray();
        else
            tag = (eip.substring(0,14)+"00000000").toCharArray();

        FIFOReplacement fifoReplacement = new FIFOReplacement();
        return fifoReplacement.Replace(start,end,tag,input);
    }

    @Override
    public String getPAddr(int rowNo) {
        //TODO
        Cache c = Cache.getCache();
        Cache.CacheLinePool cacheLinePool = c.cache;
        Cache.CacheLine cacheLine = cacheLinePool.get(rowNo);
        char[] tag = cacheLine.getTag();
        String tagstr = "";
        for(int i =0;i<14;i++)
            tagstr = tagstr + tag[i];
        int group = rowNo / setSize;
        Transformer t = new Transformer();
        String g = t.intToBinary(""+group).substring(24);
        String blockNo = tagstr + g;
        return blockNo + "0000000000";
    }

}










