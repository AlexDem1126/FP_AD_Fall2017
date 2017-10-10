package FP_AD_Fall2017;

public class Naive_Bayes {
	
	private int[] point;
	private double[][] trueGrade;
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;

	public Naive_Bayes(int[] pointK, double[][] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		point = pointK;
		trueGrade = trueGradeMF;
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
	}

}
