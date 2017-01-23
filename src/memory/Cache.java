package memory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by denis on 1/23/2017.
 */
public class Cache {
    private int addressMask;

    private int hitCost = 1;
    private int missCost = 10;
    private int registerCost = 1;

    int hitCount = 0;
    int missCount = 0;

    int dataHitCount = 0;
    int dataMissCount = 0;

    private LinkedList<Integer> LRUQueue;

    public Cache(int logLineSize, int cacheSize) {
        int lineSize = 2 << (logLineSize - 1);
        addressMask = ~(lineSize - 1);
        int lineCount = cacheSize / lineSize;
        LRUQueue = new LinkedList<>();
        for (int i = 0; i < lineCount; i++){ //Adding sentinels: such addresses can't be in cache
            LRUQueue.add(-1);
        }
    }

    public int getCost(int address){
        if (address > 0xFFFF) return registerCost; //For registers cost is 1 always
        int cacheAddress = address & addressMask;

        int index = LRUQueue.indexOf(cacheAddress);
        if (index != -1){ //Cache hit
            LRUQueue.remove(index);
            LRUQueue.add(cacheAddress);
            hitCount++;

            if (cacheAddress < 0x8000) {
                dataHitCount++;
                double hitRate = 100 * (double) dataHitCount / (double) (dataHitCount + dataMissCount);
            }
            return hitCost;
        }

        LRUQueue.poll();
        LRUQueue.add(cacheAddress);
        missCount++;
        if (cacheAddress < 0x8000) {
            dataMissCount++;
            double missRate = 100 * (double) dataMissCount / (double) (dataHitCount + dataMissCount);
        }
        return missCost;
    }
}
