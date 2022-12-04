package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import processor.*;

public class Cache implements Element {
    public int latency;
    Processor containingProcessor;
    int cacheSize, MissAddress, read, WriteData, LengthOfIndex;
    boolean isInCache = true;
    CacheLine[] cach;
    int[] index;

    public Cache(Processor containingProcessor, int latency, int cacheSize) {
        this.containingProcessor = containingProcessor;
        this.latency = latency;
        this.cacheSize = cacheSize;

        this.LengthOfIndex = (int) (Math.log(this.cacheSize / 8) / Math.log(2));
        this.cach = new CacheLine[cacheSize / 8];

        for (int i = 0; i < cacheSize / 8; i++)
            this.cach[i] = new CacheLine();
    }

    public boolean checkPresence() {
        return this.isInCache;
    }

    public int[] getIndexes() {
        return this.index;
    }

    public CacheLine[] getCaches() {
        return this.cach;
    }

    public Processor getProcessor() {
        return this.containingProcessor;
    }

    public void setProcessor(Processor processor) {
        this.containingProcessor = processor;
    }

    @Override
    public String toString() {
        return Integer.toString(this.latency) + " : latency";
    }

    public void handleCacheMiss(int addr) {
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        addr));

    }

    public int cacheRead(int address) {
        String a = Integer.toBinaryString(address);
        String ind = "";
        int LengthOfIndex_ind;

        for (int i = 0; i < 32 - a.length(); i++)
            a = "0" + a;

        for (int i = 0; i < LengthOfIndex; i++)
            ind = ind + "1";

        if (LengthOfIndex == 0)
            LengthOfIndex_ind = 0;
        else
            LengthOfIndex_ind = address & Integer.parseInt(ind, 2);
        int Add_TagOfDataArray = Integer.parseInt(a.substring(0, a.length() - LengthOfIndex), 2);

        if (Add_TagOfDataArray == cach[LengthOfIndex_ind].getTagOfDataArray(0)) {
            cach[LengthOfIndex_ind].setLRU(1);
            isInCache = true;
            return cach[LengthOfIndex_ind].getData(0);
        } else if (Add_TagOfDataArray == cach[LengthOfIndex_ind].getTagOfDataArray(1)) {
            cach[LengthOfIndex_ind].setLRU(0);
            isInCache = true;
            return cach[LengthOfIndex_ind].getData(1);
        } else {
            isInCache = false;
            return -1;
        }

    }

    public void cacheWrite(int address, int value) {
        String a = Integer.toBinaryString(address);
        String ind = "";
        int LengthOfIndex_ind;

        for (int i = 0; i < 32 - a.length(); i++)
            a = "0" + a;

        for (int i = 0; i < LengthOfIndex; i++)
            ind = ind + "1";

        if (LengthOfIndex == 0)
            LengthOfIndex_ind = 0;
        else
            LengthOfIndex_ind = address & Integer.parseInt(ind, 2);

        int TagOfDataArray = Integer.parseInt(a.substring(0, a.length() - LengthOfIndex), 2);
        cach[LengthOfIndex_ind].setValue(TagOfDataArray, value);

    }

    @Override
    public void handleEvent(Event e) {

        if (e.getEventType() == Event.EventType.MemoryRead) {
            MemoryReadEvent event = (MemoryReadEvent) e;
            int data = cacheRead(event.getAddressToReadFrom());
            if (isInCache == true) {
                Simulator.getEventQueue().addEvent(
                        new MemoryResponseEvent(
                                Clock.getCurrentTime() + this.latency,
                                this,
                                event.getRequestingElement(),
                                data));
            } else {
                this.MissAddress = event.getAddressToReadFrom();

                event.setEventTime(Clock.getCurrentTime() + Configuration.mainMemoryLatency + 1);
                Simulator.getEventQueue().addEvent(event);
                handleCacheMiss(event.getAddressToReadFrom());
            }
        }

        else if (e.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            cacheWrite(this.MissAddress, event.getValue());

        }

        else if (e.getEventType() == Event.EventType.MemoryWrite) {
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue());

            containingProcessor.getMainMemory().setWord(event.getAddressToWriteTo(), event.getValue());

            Simulator.getEventQueue().addEvent(
                    new ExecutionCompleteEvent(
                            Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                            containingProcessor.getMainMemory(),
                            event.getRequestingElement()));

        }

    }

}