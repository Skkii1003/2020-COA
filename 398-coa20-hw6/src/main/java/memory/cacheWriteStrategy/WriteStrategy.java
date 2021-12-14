package memory.cacheWriteStrategy;

import memory.Cache;
import memory.Memory;
import memory.cacheMappingStrategy.MappingStrategy;
import memory.cacheMappingStrategy.SetAssociativeMapping;

/**
 * @Author: A cute TA
 * @CreateTime: 2020-11-12 11:38
 */
public abstract class WriteStrategy {
    MappingStrategy mappingStrategy;
    /**
     * 将数据写入Cache，并且根据策略选择是否修改内存
     * @param rowNo 行号
     * @param input  数据
     * @return
     */
    public void write(int rowNo, char[] input) {
        //TODO
        Cache c = Cache.getCache();
        Cache.CacheLinePool cacheLinePool = c.cache;
        Cache.CacheLine cacheLine = cacheLinePool.get(rowNo);
        cacheLine.setData(input);
        cacheLine.setDirty(true);
    }


    /**
     * 修改内存
     * @return
     */
    public void writeBack(int rowNo) {
        //TODO
        Cache c = Cache.getCache();
        Cache.CacheLinePool cacheLinePool = c.cache;
        Cache.CacheLine cacheLine = cacheLinePool.get(rowNo);
        char[] data = cacheLine.getData();
        Memory memory = Memory.getMemory();
        SetAssociativeMapping s = new SetAssociativeMapping();

        String eip = s.getPAddr(rowNo);
        memory.write(eip,data.length,data);
    }


    public void setMappingStrategy(MappingStrategy mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
    }

    public abstract Boolean isWriteBack();
}
