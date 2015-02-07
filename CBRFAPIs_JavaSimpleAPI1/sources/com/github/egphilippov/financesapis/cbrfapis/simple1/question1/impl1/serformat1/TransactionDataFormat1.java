package com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1.serformat1;

import java.util.Date;

public class TransactionDataFormat1 
		implements java.io.Serializable {

	private static final long serialVersionUID = -4153398057767012589L;

	private String queryURL;
	private Date dateTimeFetched;

	public String getQueryURL() {
		return queryURL;
	}

	public void setQueryURL(String queryURL) {
		this.queryURL = queryURL;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	private Transaction transaction;

	public Date getDateTimeFetched() {
		return dateTimeFetched;
	}

	public void setDateTimeFetched(Date dateTimeFetched) {
		this.dateTimeFetched = dateTimeFetched;
	} 
}
