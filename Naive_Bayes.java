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
//	private double class90Plus;
//	private double class90Minus;
	private double[] ProbabilityOfClassH; 	//it depending on the number of true classes in the database
	

	public Naive_Bayes(int[] pointK, int[] clustersSizeK, double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		point = pointK;
		clustersSize = clustersSizeK;
		trueGrade = trueGradeMF;
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		//ProbabilityOfClassH = findProbabilityOfClassH();
	}
	
	
	//find probability of occurrence of class H
	private double[] findProbabilityOfClassH(){
		//count true classes in the testing dataset
		for (int i = 0; i < numOfPoints; i++) {	
			int c1 = 0;
			int c2 = 0;
			if(trueGrade[i] >= 90){
				classType[c1]++; //class90Plus++;
			}
			else{
				classType[c1]++; //class90Minus++;
			}
		}
		
		ProbabilityOfClassH = new double[numOfTrueClasses_H];
		for (int i = 0; i < numOfTrueClasses_H; i++) {	
			ProbabilityOfClassH[i] = classType[i] / numOfPoints; 
			System.out.print("Probability Of ClassH "+i +": " + ProbabilityOfClassH[i]);
		}
		
		return ProbabilityOfClassH;
	}
	

}
