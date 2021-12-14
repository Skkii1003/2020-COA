package memory.cacheReplacementStrategy;

import memory.Cache;
import memory.cacheMappingStrategy.SetAssociativeMapping;
import memory.cacheWriteStrategy.WriteBackStrategy;
import memory.cacheWriteStrategy.WriteStrategy;

import java.util.Arrays;

/**
 * 先进先出算法
 */
public class FIFOReplacement extends ReplacementStrategy {

    @Override
    public int isHit(int start, int end, char[] addrTag) {
        //TODO
        int line = start;
        char[] tag;
        Cache c = Cache.getCache();
        Cache.CacheLinePool cacheLinePool = c.cache;
        Cache.CacheLine cacheLine;
        boolean valid;
        while(line<=end){
            cacheLine = cacheLinePool.get(line);
            valid = cacheLine.getvalidBit();
            if (valid==false){
                line++;
                continue;
            }
            tag = cacheLine.getTag();
            if (Arrays.equals(tag,addrTag))
                return line;
            else
                line++;
        }
        return -1;
    }

    @Override
    public int Replace(int start, int end, char[] addrTag, char[] input) {
        //TODO
        Cache c = Cache.getCache();
        Cache.CacheLinePool cacheLinePool = c.cache;
        int line = start;
        int replacedLine = line;
        long min = cacheLinePool.get(line).gettimeStamp();
        long time;
        boolean validbit;
        boolean dirty;

        while(line<=end){
            time = cacheLinePool.get(line).gettimeStamp();
            validbit = cacheLinePool.get(line).getvalidBit();

            if (!validbit){
                replacedLine = line;
                break;
            }
            if (time<min){
                min = time;
                replacedLine = line;
            }
            line++;
        }
        Cache.CacheLine cacheLine = cacheLinePool.get(replacedLine);
        dirty = cacheLine.getdirty();
        if (dirty==true){
            WriteStrategy writeStrategy = c.getWriteStrategy();
            writeStrategy.writeBack(replacedLine);
        }
        cacheLine.setData(input);
        cacheLine.setTag(addrTag);
        cacheLine.setDirty(false);
        cacheLine.setTimeStamp(System.currentTimeMillis());
        cacheLine.setValidBit(true);
        return replacedLine;
    }
}
