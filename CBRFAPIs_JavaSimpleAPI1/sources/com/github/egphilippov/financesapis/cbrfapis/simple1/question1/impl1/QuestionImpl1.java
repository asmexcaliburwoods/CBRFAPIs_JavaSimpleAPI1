package com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import com.github.egphilippov.financesapis.cbrfapis.simple1.question1.CBRFHowManyRoublesPerUSDTodayQuestion;
import com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1.fs.io.Marshaller;
import com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1.serformat1.Transaction;
import com.github.egphilippov.financesapis.cbrfapis.simple1.question1.impl1.serformat1.TransactionDataFormat1;

/*
http://www.cbr.ru/scripts/XML_daily.asp?date_req=07/02/2015

date_req= Date of query (dd/mm/yyyy)

<Valute ID="R01235">
<NumCode>840</NumCode>
<CharCode>USD</CharCode>
<Nominal>1</Nominal>
<Name>Доллар США</Name>
<Value>66,0432</Value>
</Valute>
 */

public class QuestionImpl1 
		implements CBRFHowManyRoublesPerUSDTodayQuestion {
	private static final long MILLISECONDS_PER_PERIOD_OF_24_HOURS = 24L*60*60*1000;

	private Transaction transaction;
	public void refreshCurrentDataFromCBRF() throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		transaction = new Transaction();
//		TransactionDataFormat1 transactionData = 
//				transaction.getTransactionData();
		transaction.executeToFinish();
		//loadLocalDataAboutCBRF();
	}
	
	public Set<BigDecimal> getAnswer() throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		final Date dateTimeLastFetched = loadLocalDataAboutCBRF_and_getDateTimeLastFetched();
		if(dateTimeLastFetched == null || 
				(System.currentTimeMillis() - getStartOfDay(dateTimeLastFetched).getTime() 
						<= MILLISECONDS_PER_PERIOD_OF_24_HOURS)){
			refreshCurrentDataFromCBRF();
		}
		return getHowManyRoublesPerUSDTodayDataFromLocalData();
	}

	protected Date loadLocalDataAboutCBRF_and_getDateTimeLastFetched() 
			throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		loadLocalDataAboutCBRF();
		return getDateTimeLastFetched();
	}

	private Set<BigDecimal> getHowManyRoublesPerUSDTodayDataFromLocalData() 
			throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		return transaction.getHowManyRoublesPerUSDTodayDataFromLocalData();
	}

	private Date getStartOfDay(Date dateTime) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(dateTime);
		cal.set(Calendar.HOUR_OF_DAY, /*int hours*/ 0);
		cal.set(Calendar.MINUTE, /*int minutes*/ 0);
		cal.set(Calendar.SECOND, /*int seconds*/ 0);
		cal.set(Calendar.MILLISECOND, 0);
		final Date startOfDay = cal.getTime();
		return startOfDay;
	}

	public Date getDateTimeLastFetched() {
		if(transaction == null) return null;
		return transaction.getTransactionData().getDateTimeFetched();
	}

	public void loadLocalDataAboutCBRF() throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		loadLastTransactionFromFS();
	}
	
	/**
	 *TODO at Windows, store at Registry
	 */
	private static final java.io.File FOLDER_FOR_TRANSACTIONDATA_FILES = new java.io.File(
			"" + System.getProperty("user.home") + File.pathSeparator + 
				".EGPSBRFAPIs"+ File.pathSeparator + "APISimple1" );
	
	private static final String TRANSACTIONDATA_SER_FILE_SUFFIXES = ".TRANSACTIONDATA_serformat1.ser";
	
	private void loadLastTransactionFromFS() throws CBRFHowManyRoublesPerUSDTodayQuestionException {
		if(FOLDER_FOR_TRANSACTIONDATA_FILES.mkdirs()){};
		if(!FOLDER_FOR_TRANSACTIONDATA_FILES.exists()) 
			throw new CBRFHowManyRoublesPerUSDTodayQuestionException(
					"Folder "+ FOLDER_FOR_TRANSACTIONDATA_FILES.getAbsolutePath()+
						" must exist here, but it doesn't, according to Java API.");
		if(!FOLDER_FOR_TRANSACTIONDATA_FILES.isDirectory()) 
			throw new CBRFHowManyRoublesPerUSDTodayQuestionException(
					"Folder "+ FOLDER_FOR_TRANSACTIONDATA_FILES.getAbsolutePath()+
						" must be a directory here, but it isn't, according to Java API.");
		final File[] transactionDataFiles = FOLDER_FOR_TRANSACTIONDATA_FILES.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				BigInteger numberPrefix = getNumberPrefix(f);
				return numberPrefix != null;
			}
		});
		if(transactionDataFiles == null || transactionDataFiles.length == 0) return;
		
		Arrays.sort(transactionDataFiles, new Comparator<File>(){

			@Override
			public int compare(File f1, File f2) {
				BigInteger numberPrefix1 = getNumberPrefix(f1);
				BigInteger numberPrefix2 = getNumberPrefix(f2);
				if(numberPrefix1 == null)
					throw new AssertionError("numberPrefix == null for file '"+f1.getAbsolutePath()+"'.");
				if(numberPrefix2 == null)
					throw new AssertionError("numberPrefix == null for file '"+f2.getAbsolutePath()+"'.");
				return numberPrefix1.compareTo(numberPrefix2);
			}}
		);
		
		final File lastTDFile = transactionDataFiles[transactionDataFiles.length-1];
		eraseOldTDFilesHistory(transactionDataFiles);
		TransactionDataFormat1 transactionData;
		try {
			transactionData = (TransactionDataFormat1) new Marshaller().loadFromFS(lastTDFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CBRFHowManyRoublesPerUSDTodayQuestionException(
					"Input/output error when loading transaction data from file system", e);
		}
		transaction = transactionData.getTransaction();
	}

	private void eraseOldTDFilesHistory(File[] transactionDataFiles) {
		if(transactionDataFiles == null || transactionDataFiles.length <= 1) return;
		for (int index = 0; index <= transactionDataFiles.length - 2; ++index){
			if(index >= transactionDataFiles.length - 1) break; //for more safety
			final File transactionDataFile = transactionDataFiles[index];
			try{
				if(transactionDataFile.delete()){};
			}catch(Throwable tr){
				tr.printStackTrace();
				System.err.println("Throwable when deleting old transaction history: "+tr+"; ignoring it.");
			}
		}
	}

	protected BigInteger getNumberPrefix(File f) {
		final String fileName = f.getName();
		if(fileName == null || fileName.isEmpty()) return null;
		if(!fileName.endsWith(TRANSACTIONDATA_SER_FILE_SUFFIXES)) return null;
		final String fileNameNoTDSuffixes = fileName.substring(
				0, fileName.length() - TRANSACTIONDATA_SER_FILE_SUFFIXES.length());
		final StringBuilder numberSB = new StringBuilder(fileNameNoTDSuffixes.length());
		int index = 0;
		while(true) {
			if(fileNameNoTDSuffixes.length() <= index) break;
			char ch = fileNameNoTDSuffixes.charAt(index++);
			if((ch >= '0') && (ch <= '9')) numberSB.append(ch);
			else break;
		}
		if(numberSB.length() == 0) return null;
		final BigInteger bi = new BigInteger(numberSB.toString());
		return bi;
	}
}
