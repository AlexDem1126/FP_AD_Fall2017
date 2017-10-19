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
	private double prb_zero90Plus;
	private double prb_zero90Minus;
	private double prb_one90Plus;
	private double prb_one90Minus;
	

	public Naive_Bayes(int[] pointK, int[] clustersSizeK, double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		point = pointK;
		clustersSize = clustersSizeK;
		trueGrade = trueGradeMF;
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		ProbabilityOfClassH = findProbabilityOfClassH();
	}
	
	
	//find probability of occurrence of class H
	private double[] findProbabilityOfClassH(){
		classType = new double[numOfTrueClasses_H];
		//count true classes in the testing dataset
		for (int i = 0; i < numOfPoints; i++) {	
			int c1 = 0;
			int c2 = 1;
			if(trueGrade[i] >= 90){
				classType[c1]++; //class90Plus++;
			}
			else{
				classType[c2]++; //class90Minus++;
			}
		}
		
		ProbabilityOfClassH = new double[numOfTrueClasses_H];
		for (int i = 0; i < numOfTrueClasses_H; i++) {	
			ProbabilityOfClassH[i] = classType[i] / numOfPoints; 
			System.out.println("Probability Of ClassH "+i +": " + ProbabilityOfClassH[i]);
		}
		
		return ProbabilityOfClassH;
	}
	
	
	
	//count instance X given class H
	private void countInstanceX_givenClassH(){
		for (int i = 0; i < numOfPoints; i++) {
			for (int j = 0; j < numOfDimension; j++) {
				if((dataset[i][j] == 0) && (trueGrade[i] >= 90)){
					zero90Plus[i]++;
				}
				else if ((dataset[i][j] == 0) && (trueGrade[i] < 90)){
					zero90Minus[i]++;
				}
				else if ((dataset[i][j] == 1) && (trueGrade[i] >= 90)){
					one90Plus[i]++;
				}
				else if ((dataset[i][j] == 1) && (trueGrade[i] < 90)){
					one90Minus[i]++;
				}
				else {
					System.out.println("ERROR: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
		}
		
	}
	
	
	
	//find probability of generating instance X given class H
	private void findProbabilityInstanceX_givenClassH(){
		for (int i = 0; i < numOfDimension; i++) {
			prb_zero90Plus =  zero90Plus[i] / classType[0];
			prb_zero90Minus =  zero90Minus[i] / classType[1];
			prb_one90Plus =  one90Plus[i] / classType[0];
			prb_one90Minus =  zero90Minus[i] / classType[1];							
		}				
	}
	
	

}
