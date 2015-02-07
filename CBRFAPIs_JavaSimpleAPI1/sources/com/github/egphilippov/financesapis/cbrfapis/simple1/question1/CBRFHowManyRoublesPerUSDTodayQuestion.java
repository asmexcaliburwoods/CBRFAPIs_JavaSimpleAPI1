package com.github.egphilippov.financesapis.cbrfapis.simple1.question1;

import java.math.BigDecimal;
import java.util.Set;

public interface CBRFHowManyRoublesPerUSDTodayQuestion {
	public static class CBRFHowManyRoublesPerUSDTodayQuestionException extends Exception {
		private static final long serialVersionUID = 8408859842347761468L;
		
		public CBRFHowManyRoublesPerUSDTodayQuestionException() {
			super();
		}

		public CBRFHowManyRoublesPerUSDTodayQuestionException(String message,
				Throwable cause) {
			super(message, cause);
		}

		public CBRFHowManyRoublesPerUSDTodayQuestionException(String message) {
			super(message);
		}

		public CBRFHowManyRoublesPerUSDTodayQuestionException(Throwable cause) {
			super(cause);
		}
	}

	Set<BigDecimal> getAnswer() throws CBRFHowManyRoublesPerUSDTodayQuestionException ;
}
