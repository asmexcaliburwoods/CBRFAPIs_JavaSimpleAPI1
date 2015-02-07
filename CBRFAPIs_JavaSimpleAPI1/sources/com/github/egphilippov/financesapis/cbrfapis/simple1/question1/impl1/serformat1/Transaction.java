package com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1.serformat1;

import java.math.BigDecimal;
import java.util.Set;

import com.github.egphilippov.financesapis.cbrfapis.simple1.question1.CBRFHowManyRoublesPerUSDTodayQuestion.CBRFHowManyRoublesPerUSDTodayQuestionException;

public class Transaction implements java.io.Serializable {
	private static final long serialVersionUID = -59189312722236386L;
	
	private final TransactionDataFormat1 transactionData = 
			new TransactionDataFormat1();
	
	{
		transactionData.setTransaction(this);
	}

	public TransactionDataFormat1 getTransactionData() {
		return transactionData;
	}

	public void executeToFinish() {
		// TODO Auto-generated method stub
		saveTransactionToFS();
		throw new UnsupportedOperationException("see also : all TODOs");
	}

	private void saveTransactionToFS() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("see also : all TODOs");
	}

	public Set<BigDecimal> getHowManyRoublesPerUSDTodayDataFromLocalData() 
			throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("see also : all TODOs");
	}
}