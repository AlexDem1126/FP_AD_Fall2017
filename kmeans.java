package FP_AD_Fall2017;

import java.util.ArrayList;

public class kmeans {

	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;

	public kmeans(double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
	}

	public void kMeanClustering(int nClusters, int nIterations, double[][] iniCentroids) {
		numOfClusters = nClusters;
		
		// 1. call method to generate pseudo random centroids
		centroids = generateCentroids(numOfClusters);

		
		
	}

	

	
	// 1.0 generate pseudo random centroids
	private double[][] generateCentroids(int numOfClusters2) {
		centroids = new double[numOfClusters][];
		ArrayList centrdData = new ArrayList();
					
		for (int i = 0; i < numOfClusters2; i++) {
			int randomCntrd;
			//generate unique random centroids without duplicates
			do {
				randomCntrd = (int) (Math.random() * numOfPoints);
			} while (centrdData.contains(randomCntrd));
			
			centrdData.add(randomCntrd);
			System.out.println(centrdData);
			
			// copy centroid's attributes to the generated centroid
			centroids[i] = new double[numOfDimension];			
			for (int j = 0; j < numOfDimension; j++) {
				centroids[i][j] = dataset[randomCntrd][j];
				System.out.println(centroids[i][j]);
			}
		}
		return centroids;
	}





}
