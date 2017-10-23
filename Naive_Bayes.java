package FP_AD_Fall2017;

import java.util.HashSet;

public class Naive_Bayes {
	
	private int[] point;
	private int[] clustersSize;
	private double[] trueGrade;
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
	private double[] largest;
	private double[] trueGradeTraining;
	private double TP;	//TP is True Positive 
	private double FP;	//FP is False Positive 
	private double FN;	//FN is False Negative 
	private double TN;	//TN is True Negative 
	private double accuracyTraining;
	private int ls = 1; //Laplace smoothing number
	private int[] numOfPossibleValuesX;

	public Naive_Bayes(int[] pointK, int[] clustersSizeK, double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		point = pointK;
		clustersSize = clustersSizeK;
		trueGrade = trueGradeMF;
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		ProbabilityOfClassH = findProbabilityOfClassH();
		countInstanceX_givenClassH();
		findNumOfPossibleValuesX();
		findProbabilityInstanceX_givenClassH();	
		findProbabilityInstanceX_beingInClassH();
		trueGradeTraining = convertToTrueGradeTraining();
		countTP_FP_FN_TN();
		accuracyTraining = findAccuracyTraining();
		System.out.println("STOP");
	}
	


	//1. P(H) - find probability of occurrence of class H
	private double[] findProbabilityOfClassH(){
		classType = new double[numOfTrueClasses_H];
		//count true classes in the testing dataset
		for (int i = 0; i < numOfPoints; i++) {	
			int c1 = 0;
			int c2 = 1;
			if(trueGrade[i] >= 90){
				classType[c1]++; //class90Plus;
			}
			else{
				classType[c2]++; //class90Minus;
			}
		}
		
		ProbabilityOfClassH = new double[numOfTrueClasses_H];
		for (int i = 0; i < numOfTrueClasses_H; i++) {	
			ProbabilityOfClassH[i] = (classType[i] + ls) / (numOfPoints + (ls * numOfTrueClasses_H)); 
			System.out.println("Probability Of ClassH "+i +": " + ProbabilityOfClassH[i]);
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
				if((dataset[i][j] == 0) && (trueGrade[i] >= 90)){
					zero90Plus[j]++;
				}
				else if ((dataset[i][j] == 0) && (trueGrade[i] < 90)){
					zero90Minus[j]++;
				}
				else if ((dataset[i][j] == 1) && (trueGrade[i] >= 90)){
					one90Plus[j]++;
				}
				else if ((dataset[i][j] == 1) && (trueGrade[i] < 90)){
					one90Minus[j]++;
				}
				else {
					System.out.println("ERROR_1: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
		}
		
	}
	
	
	
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
	
	
	
	//3. P(X|H) - find probability of generating instance X given class H
	private void findProbabilityInstanceX_givenClassH(){		
		prb_zero90Plus = new double[numOfDimension];
		prb_zero90Minus = new double[numOfDimension];
		prb_one90Plus = new double[numOfDimension];
		prb_one90Minus = new double[numOfDimension];
		for (int i = 0; i < numOfDimension; i++) {
			prb_zero90Plus[i] = (zero90Plus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i]));
			prb_zero90Minus[i] = (zero90Minus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i]));
			prb_one90Plus[i] =  (one90Plus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));
			prb_one90Minus[i] =  (one90Minus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));							
		}				
	}
	
	
	
	//4. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
	private void findProbabilityInstanceX_beingInClassH(){
		double[] instanceX_beingInClass90Plus = new double[numOfDimension];
		double[] instanceX_beingInClass90Minus = new double[numOfDimension];
		largest = new double[numOfPoints];
//		double multiX_beingInClass90Plus;
//		double multiX_beingInClass90Minus;
				
		for (int i = 0; i < numOfPoints; i++) {
			double multiX_beingInClass90Plus = 0;
			double multiX_beingInClass90Minus = 0;
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
			largest[i] = findLargest(multiX_beingInClass90Plus, multiX_beingInClass90Minus);			
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
	
	
	
	private double findLargest(double multiX_beingInClass90Plus, double multiX_beingInClass90Minus) {
		double temp_largest = 0;
		if(multiX_beingInClass90Plus > multiX_beingInClass90Minus){
			temp_largest = 90;
		}
		else{
			temp_largest = 89;
		}
		return temp_largest;
	}
	
	
	
	private double[] convertToTrueGradeTraining() {
		double[] trueGradeTraining_F = new double[numOfPoints];
		for (int i = 0; i < numOfPoints; i++) {			
			if(trueGrade[i] >= 90){
				trueGradeTraining_F[i] = 90;
			}
			else{
				trueGradeTraining_F[i] = 89;
			}
		}		
		return trueGradeTraining_F;
	}
	
	
	
	private void countTP_FP_FN_TN() {
		TP = 0;	//TP is True Positive 
		FP = 0;	//FP is False Positive 
		FN = 0;	//FN is False Negative 
		TN = 0;	//TN is True Negative
		for (int i = 0; i < numOfPoints; i++) {	
			if(trueGradeTraining[i] == largest[i]){
				TP++; //Prediction was positive +1, and in reality the value was +1
			}
			else if (trueGradeTraining[i] < largest[i]) {
				FP++; //Prediction was positive +1, but in reality the value was -1
			}
			else if (trueGradeTraining[i] > largest[i]) {
				FN++; //Prediction was negative -1, but in reality the value was +1
			}
			else {
				TN++; //Prediction was negative -1, but in reality the value was -1
			}
		}		
	}
	
	
	
	private double findAccuracyTraining() {
		double AccuracyTraining_F = 0;
		AccuracyTraining_F = (TP + TN)/(TP + TN + FP + FN);	
		System.out.println("Accuracy of training dataset: " + AccuracyTraining_F*100 +"%");
		return AccuracyTraining_F;
	}
	//in ProbabilityOfClassH[i] index 0 is class90Plus, index 1 is class90Minus;

}
