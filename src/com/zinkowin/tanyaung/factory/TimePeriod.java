package com.zinkowin.tanyaung.factory;

public enum TimePeriod {
	ALL("All"),TODAY("Today"),YESTERDAY("Yesterday"),THIS_WEEK("This Week"),LAST_WEEK("Last Week"),THIS_MONTH("This Month"),LAST_MONTH("Last Month"),THIS_YEAR("This Year"),LAST_YEAR("Last Year");
	private final String value;
	
	TimePeriod(String value){
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}

}
