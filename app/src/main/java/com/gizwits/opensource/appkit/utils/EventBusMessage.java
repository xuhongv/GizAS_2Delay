package com.gizwits.opensource.appkit.utils;

public class EventBusMessage {


    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    private int eventCode;

    public EventBusMessage(int eventCode){
        this.eventCode=eventCode;
    }



}
