package FP_AD_Fall2017;

public class Naive_Bayes {
	
	private int[] point;
	private int[] clustersSize;
	private double[] trueGrade; //private double[][] trueGrade;
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
	private double[] instanceX_beingInClass90Plus;
	private double[] instanceX_beingInClass90Minus;
	

	public Naive_Bayes(int[] pointK, int[] clustersSizeK, double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		point = pointK;
		clustersSize = clustersSizeK;
		trueGrade = trueGradeMF;
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		ProbabilityOfClassH = findProbabilityOfClassH();
		countInstanceX_givenClassH();
		findProbabilityInstanceX_givenClassH();	
		findProbabilityInstanceX_beingInClassH();
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
			ProbabilityOfClassH[i] = classType[i] / numOfPoints; 
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
	
	
	
	//3. P(X|H) - find probability of generating instance X given class H
	private void findProbabilityInstanceX_givenClassH(){		
		prb_zero90Plus = new double[numOfDimension];
		prb_zero90Minus = new double[numOfDimension];
		prb_one90Plus = new double[numOfDimension];
		prb_one90Minus = new double[numOfDimension];
		for (int i = 0; i < numOfDimension; i++) {
			prb_zero90Plus[i] = zero90Plus[i] / classType[0];
			prb_zero90Minus[i] = zero90Minus[i] / classType[0];
			prb_one90Plus[i] =  one90Plus[i] / classType[1];
			prb_one90Minus[i] =  one90Minus[i] / classType[1];							
		}				
	}
	
	
	
	//4. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
	private void findProbabilityInstanceX_beingInClassH(){
		instanceX_beingInClass90Plus = new double[numOfDimension];
		instanceX_beingInClass90Minus = new double[numOfDimension];
				
		for (int i = 0; i < numOfPoints; i++) {
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
		}		
	}
	
	//in ProbabilityOfClassH[i] index 0 is class90Plus, index 1 is class90Minus;

}
