package FP_AD_Fall2017;

import java.util.ArrayList;

public class Calinski_Harabasz {
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;
	private double threshold;
	

	
	
	public Calinski_Harabasz(double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
	}
	
			

	public void kMeanClusteringForCH(int nClusters, int nIterations, double iniThreshold, double[][] iniCentroids) {
		numOfClusters = nClusters;
		threshold = iniThreshold;
				
		// 1. generate pseudo random centroids
		centroids = generateCentroids(); 			//initial centroid
		
		
	}
	
	

	// 1. generate pseudo random centroids
	private double[][] generateCentroids() {
		centroids = new double[numOfClusters][];
		ArrayList centrdData = new ArrayList();
					
		for (int i = 0; i < numOfClusters; i++) {
			int randomCntrd;
			//generate unique random centroids without duplicates
			do {
				randomCntrd = (int) (Math.random() * numOfPoints);
			} while (centrdData.contains(randomCntrd));
			
			centrdData.add(randomCntrd);
			
			// copy centroid's attributes to the generated centroid
			centroids[i] = new double[numOfDimension];			
			for (int j = 0; j < numOfDimension; j++) {
				centroids[i][j] = dataset[randomCntrd][j];
			}
		}
		return centroids;
	}
	
	
	
	
}
