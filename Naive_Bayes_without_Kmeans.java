package FP_AD_Fall2017;

import java.util.HashSet;

public class Naive_Bayes_without_Kmeans {
	
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfTrueClasses_H = 2; //assign number of true classes in the testing dataset
	private double[] classType; //class90Plus or class90Minus
	private double[] ProbabilityOfClassH; 	//it depending on the number of true classes in the database
	private int[] zero90Plus;
	private int[] zero90Minus;
	private int[] one90Plus;
	private int[] one90Minus;
	private double[] prb_zero90Plus;
	private double[] prb_zero90Minus;
	private double[] prb_one90Plus;
	private double[] prb_one90Minus;
	private double[] largestTrainingPredicted;
	private double[] largest_TestPredicted;
	private double[] trueGradeTraining;
	private double[] trueGrade_Test_90and89;
	private double TP;	//TP is True Positive 
	private double FP;	//FP is False Positive 
	private double FN;	//FN is False Negative 
	private double TN;	//TN is True Negative 
	private double accuracyTraining;
	private double accuracy_Test;
	private int ls = 2; //Laplace smoothing number
	private int[] numOfPossibleValuesX;
	private int num_clusters;
	
	

	
	public double[] getTrueGradeTraining() {
		return trueGradeTraining;
	}
	public void setTrueGradeTraining(double[] trueGradeTraining) {
		this.trueGradeTraining = trueGradeTraining;
	}



	//******************************************************************
	/*********** Training Naive Bayes Classifier ***********/
	//******************************************************************

	public Naive_Bayes_without_Kmeans(double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {	
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		trueGradeTraining = convertActualTrueGradeTo90and89(trueGradeMF);
		
		//1. P(H) - find probability of occurrence of class H
		ProbabilityOfClassH = findProbabilityOfClassH();
		
		//2. count instance X given class H
		countInstanceX_givenClassH();
		
		//3. # values Xi can take on (i.e. 2 for binary)
		findNumOfPossibleValuesX();
		
		//4. P(X|H) - find probability of generating instance X given class H		
		findProbabilityInstanceX_givenClassH();	
		
		//5. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
		findProbabilityInstanceX_beingInClassH();
		
		//6. find Accuracy
		countTP_FP_FN_TN();
		accuracyTraining = findAccuracyTraining();		
	}
	


	//1. P(H) - find probability of occurrence of class H
	private double[] findProbabilityOfClassH(){
		classType = new double[numOfTrueClasses_H];
		//count true classes in the testing dataset
		for (int i = 0; i < numOfPoints; i++) {	
			int c1 = 0;
			int c2 = 1;
			if(trueGradeTraining[i] >= 90){
				classType[c1]++; //class90Plus;
			}
			else{
				classType[c2]++; //class90Minus;
			}
		}
		
		ProbabilityOfClassH = new double[numOfTrueClasses_H];
		for (int i = 0; i < numOfTrueClasses_H; i++) {	
			ProbabilityOfClassH[i] = (classType[i] + ls) / (numOfPoints + (ls * numOfTrueClasses_H)); 
//			System.out.println("Probability Of ClassH "+i +": " + ProbabilityOfClassH[i]);
		}
		
		return ProbabilityOfClassH;
	}
	
	
	
	//2. count instance X given class H
	private void countInstanceX_givenClassH(){
		zero90Plus = new int[numOfDimension];
		zero90Minus = new int[numOfDimension];
		one90Plus = new int[numOfDimension];
		one90Minus = new int[numOfDimension];		
		for (int i = 0; i < numOfPoints; i++) {
			for (int j = 0; j < numOfDimension; j++) {
				if((dataset[i][j] == 0) && (trueGradeTraining[i] >= 90)){
					zero90Plus[j]++;
				}
				else if ((dataset[i][j] == 0) && (trueGradeTraining[i] < 90)){
					zero90Minus[j]++;
				}
				else if ((dataset[i][j] == 1) && (trueGradeTraining[i] >= 90)){
					one90Plus[j]++;
				}
				else if ((dataset[i][j] == 1) && (trueGradeTraining[i] < 90)){
					one90Minus[j]++;
				}
				else {
					System.out.println("ERROR_1: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
		}
	}
	
	
	//3. # values Xi can take on (i.e. 2 for binary)
	private void findNumOfPossibleValuesX(){
		numOfPossibleValuesX = new int[numOfDimension];		
		int j = 0;
		while(j < numOfDimension){
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < numOfPoints; i++) {
				if (!set.contains((int) dataset[i][j])){
					set.add((int) dataset[i][j]);
					numOfPossibleValuesX[j] = set.size();
		        }
			}
			j++;
		}
	}
	
	
	
	//4. P(X|H) - find probability of generating instance X given class H
	private void findProbabilityInstanceX_givenClassH(){		
		prb_zero90Plus = new double[numOfDimension];
		prb_zero90Minus = new double[numOfDimension];
		prb_one90Plus = new double[numOfDimension];
		prb_one90Minus = new double[numOfDimension];
		for (int i = 0; i < numOfDimension; i++) {
			prb_zero90Plus[i] = (zero90Plus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i])); 
			prb_zero90Minus[i] = (zero90Minus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));
			prb_one90Plus[i] =  (one90Plus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i]));
			prb_one90Minus[i] =  (one90Minus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));
		}			
	}
	
	
	
	//5. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
	private void findProbabilityInstanceX_beingInClassH(){
		double[] instanceX_beingInClass90Plus = new double[numOfDimension];
		double[] instanceX_beingInClass90Minus = new double[numOfDimension];
		largestTrainingPredicted = new double[numOfPoints];
				
		for (int i = 0; i < numOfPoints; i++) {			
			double multiX_beingInClass90Plus = 0;
			double multiX_beingInClass90Minus = 0;
			double probabilityInstanceX_beingInClass90Plus = 0;
			double probabilityInstanceX_beingInClass90Minus = 0;
			double nornilezed90Plus = 0;
			double nornilezed90Minus = 0;
			for (int j = 0; j < numOfDimension; j++) {
				if(dataset[i][j] == 0){
					instanceX_beingInClass90Plus[j] = prb_zero90Plus[j];
					instanceX_beingInClass90Minus[j] = prb_zero90Minus[j];
				}				
				else if (dataset[i][j] == 1) {
					instanceX_beingInClass90Plus[j] = prb_one90Plus[j];
					instanceX_beingInClass90Minus[j] = prb_one90Minus[j];
				}
				else {
					System.out.println("ERROR_2: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
			multiX_beingInClass90Plus = findMultiX_beingInClass90Plus(instanceX_beingInClass90Plus);
			multiX_beingInClass90Minus = findMultiX_beingInClass90Minus(instanceX_beingInClass90Minus);
			
			//5.1 P(H|X) = P(X|H)*P(H). ProbabilityInstanceX_beingInClassH()			
			probabilityInstanceX_beingInClass90Plus = multiX_beingInClass90Plus * ProbabilityOfClassH[0];
			probabilityInstanceX_beingInClass90Minus = multiX_beingInClass90Minus * ProbabilityOfClassH[1];
			
			//5.2 normalization of P(H|X)
			nornilezed90Plus = probabilityInstanceX_beingInClass90Plus/(probabilityInstanceX_beingInClass90Plus + probabilityInstanceX_beingInClass90Minus);
			nornilezed90Minus = probabilityInstanceX_beingInClass90Minus/(probabilityInstanceX_beingInClass90Plus + probabilityInstanceX_beingInClass90Minus);
			
			largestTrainingPredicted[i] = findLargest(nornilezed90Plus, nornilezed90Minus);
			
//			System.out.println(i+" true:"+trueGradeTraining[i]+", predicted:"+largestTrainingPredicted[i]+": "+nornilezed90Plus+", " + nornilezed90Minus);			
		}		
	}



	private double findMultiX_beingInClass90Plus(double[] instanceX_beingInClass90Plus) {
		double temp_MultiX_InClass90Plus = 1;
		for (int i = 0; i < numOfDimension; i++) {			
			temp_MultiX_InClass90Plus = temp_MultiX_InClass90Plus * instanceX_beingInClass90Plus[i];
		}
		return temp_MultiX_InClass90Plus;
	}

	
	
	private double findMultiX_beingInClass90Minus(double[] instanceX_beingInClass90Minus) {
		double temp_MultiX_InClass90Minus = 1;
		for (int i = 0; i < numOfDimension; i++) {			
			temp_MultiX_InClass90Minus = temp_MultiX_InClass90Minus * instanceX_beingInClass90Minus[i];
		}
		return temp_MultiX_InClass90Minus;
	}
	
	
	
	private double findLargest(double probabilityInstanceX_beingInClass90Plus, double probabilityInstanceX_beingInClass90Minus) {
		double temp_largest = 0;
		if(probabilityInstanceX_beingInClass90Plus > probabilityInstanceX_beingInClass90Minus){
			temp_largest = 90;
		}
		else{
			temp_largest = 89;
		}
		return temp_largest;
	}
	
	
	
	private double[] convertActualTrueGradeTo90and89(double[] trueGradeMF) {
		double[] trueGrade90and89_F = new double[numOfPoints];
		for (int i = 0; i < numOfPoints; i++) {			
			if(trueGradeMF[i] >= 90){
				trueGrade90and89_F[i] = 90;
			}
			else{
				trueGrade90and89_F[i] = 89;
			}
		}		
		return trueGrade90and89_F;
	}
	
	
	//6. find Accuracy
	private void countTP_FP_FN_TN() {
		TP = 0;	//TP is True Positive 
		FP = 0;	//FP is False Positive 
		FN = 0;	//FN is False Negative 
		TN = 0;	//TN is True Negative
		for (int i = 0; i < numOfPoints; i++) {	
			if(trueGradeTraining[i] == 90 && largestTrainingPredicted[i]==90){
				TP++; //Prediction was positive +1, and in reality the value was +1
			}
			else if (trueGradeTraining[i]==89 && largestTrainingPredicted[i]==90) {
				FP++; //Prediction was positive +1, but in reality the value was -1
			}
			else if (trueGradeTraining[i] ==90 && largestTrainingPredicted[i]==89) {
				FN++; //Prediction was negative -1, but in reality the value was +1
			}
			else if (trueGradeTraining[i] == 89 && largestTrainingPredicted[i]==89){
				TN++; //Prediction was negative -1, but in reality the value was -1
			}
			else{
				System.out.println("ERROR_4: TP, FP,FN, TN.");
			}
		}
		System.out.println("TRAINING_wk: TP= "+TP + ", FP= "+FP + ", FN= "+FN + ", TN= " + TN);
	}
	
	
	
	private double findAccuracyTraining() {
		double AccuracyTraining_F = 0;
		AccuracyTraining_F = (TP + TN)/(TP + TN + FP + FN);	
		System.out.println("Accuracy of training dataset_wk: " + AccuracyTraining_F*100 +"%");
		return AccuracyTraining_F;
	}


	
	//******************************************************************
	/*********** Test Naive Bayes Classifier ***********/
	//******************************************************************	
	//P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
	public void Naive_Bayes_Test(double[][] datasetTest_F, double[] trueGradeTest_F, int numOfPointsTest_F) {
		double[] instanceX_beingInClass90Plus_Test = new double[numOfDimension];
		double[] instanceX_beingInClass90Minus_Test = new double[numOfDimension];
		largest_TestPredicted = new double[numOfPointsTest_F];
				
		for (int i = 0; i < numOfPointsTest_F; i++) {
			double multiX_beingInClass90Plus_Test = 0;
			double multiX_beingInClass90Minus_Test = 0;
			double probabilityInstanceX_beingInClass90Plus_Test = 0;
			double probabilityInstanceX_beingInClass90Minus_Test = 0;
			double nornilezed90Plus_Test = 0;
			double nornilezed90Minus_Test = 0;
			for (int j = 0; j < numOfDimension; j++) {
				if(datasetTest_F[i][j] == 0){
					instanceX_beingInClass90Plus_Test[j] = prb_zero90Plus[j];
					instanceX_beingInClass90Minus_Test[j] = prb_zero90Minus[j];
				}				
				else if (datasetTest_F[i][j] == 1) {
					instanceX_beingInClass90Plus_Test[j] = prb_one90Plus[j];
					instanceX_beingInClass90Minus_Test[j] = prb_one90Minus[j];
				}
				else {
					System.out.println("ERROR_3: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
			multiX_beingInClass90Plus_Test = findMultiX_beingInClass90Plus(instanceX_beingInClass90Plus_Test);
			multiX_beingInClass90Minus_Test = findMultiX_beingInClass90Minus(instanceX_beingInClass90Minus_Test);
			
			/**/
			 //P(H|X) = P(X|H)*P(H). ProbabilityInstanceX_beingInClassH()			
			probabilityInstanceX_beingInClass90Plus_Test = multiX_beingInClass90Plus_Test * ProbabilityOfClassH[0];
			probabilityInstanceX_beingInClass90Minus_Test = multiX_beingInClass90Minus_Test * ProbabilityOfClassH[1];
			
			//normalization of P(H|X)
			nornilezed90Plus_Test = probabilityInstanceX_beingInClass90Plus_Test/(probabilityInstanceX_beingInClass90Plus_Test + multiX_beingInClass90Minus_Test);
			nornilezed90Minus_Test = multiX_beingInClass90Minus_Test/(probabilityInstanceX_beingInClass90Plus_Test + multiX_beingInClass90Minus_Test);
			
			largest_TestPredicted[i] = findLargest(nornilezed90Plus_Test, nornilezed90Minus_Test);
			
//			System.out.println(i+" true:"+trueGradeTest_F[i]+", predicted:"+largest_TestPredicted[i]+": "+nornilezed90Plus_Test+", " + nornilezed90Minus_Test);		
		}
		trueGrade_Test_90and89 = convertTrueGrade_Test_To90and89(numOfPointsTest_F, trueGradeTest_F);
		countTP_FP_FN_TN_Test(numOfPointsTest_F);
		accuracy_Test = findAccuracyTest();
		System.out.println("STOP");
	}	
	
	
	
	private double[] convertTrueGrade_Test_To90and89(int numOfPointsTest_F, double[] trueGradeTest_F) {
		double[] trueGrade90and89_F = new double[numOfPoints];
		for (int i = 0; i < numOfPointsTest_F; i++) {			
			if(trueGradeTest_F[i] >= 90){
				trueGrade90and89_F[i] = 90;
			}
			else{
				trueGrade90and89_F[i] = 89;
			}
		}		
		return trueGrade90and89_F;
	}
	
	
	
	private void countTP_FP_FN_TN_Test(int numOfPointsTest_F) {
		TP = 0;	//TP is True Positive 
		FP = 0;	//FP is False Positive 
		FN = 0;	//FN is False Negative 
		TN = 0;	//TN is True Negative
		for (int i = 0; i < numOfPointsTest_F; i++) {	
			if(trueGrade_Test_90and89[i] == 90 && largest_TestPredicted[i]==90){
				TP++; //Prediction was positive +1, and in reality the value was +1
			}
			else if (trueGrade_Test_90and89[i] ==89 && largest_TestPredicted[i]==90) {
				FP++; //Prediction was positive +1, but in reality the value was -1
			}
			else if (trueGrade_Test_90and89[i] ==90 && largest_TestPredicted[i] ==89) {
				FN++; //Prediction was negative -1, but in reality the value was +1
			}
			else if(trueGrade_Test_90and89[i] == 89 && largest_TestPredicted[i]==89){
				TN++; //Prediction was negative -1, but in reality the value was -1
			}
			else{
				System.out.println("ERROR_5: Testing data - TP, FP,FN, TN.");
			}
		}	
		System.out.println("TESTING_wk: TP= "+TP + ", FP= "+FP + ", FN= "+FN + ", TN= " + TN);
	}
	
	
	
	private double findAccuracyTest() {
		double AccuracyTest_F = 0;
		AccuracyTest_F = (TP + TN)/(TP + TN + FP + FN);	
		System.out.println("Accuracy of testing dataset_wk: " + AccuracyTest_F*100 +"%");
		return AccuracyTest_F;
	}



}
