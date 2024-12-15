package main;

import java.awt.Rectangle;

public class EventRect extends Rectangle{
	
	private int eventRectDefaultX, eventRectDefaultY; 
	private boolean eventDone = false; //allows us to check if the event has happened or not (gives us the option to create a one time only event) 
	
	public void setEventRectDefaultX(int eventRectDefaultX) {
		this.eventRectDefaultX = eventRectDefaultX;
	}
	public void setEventRectDefaultY(int eventRectDefaultY) {
		this.eventRectDefaultY = eventRectDefaultY;
	}
	public int getEventRectDefaultX() {
		return eventRectDefaultX;
	}
	public int getEventRectDefaultY() {
		return eventRectDefaultY;
	}
	public boolean isEventDone() {
		return eventDone;
	}
}