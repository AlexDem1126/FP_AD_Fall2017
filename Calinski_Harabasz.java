package FP_AD_Fall2017;

import java.util.ArrayList;

public class Calinski_Harabasz {
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;
	private double threshold;
	private int[] point;
	private int[] sizeOfCluster;
	private double[][] centroidsAttrAverage;
	private double[] meanOfAverage;
	private double sSW; 	//sum of squared within-cluster scatter matrix (SSW)
	private double sSB; 	//sum of squared between-cluster scatter matrix (SSB) 

	
	
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
		
		double[][] updatedCentroids = centroids; 	//new initial centroid (updated)
		int iterationConverges = 0; 				//numbers of iteration before converging
		double newSSW = 0;							//sum of squared within-cluster scatter matrix (SSW) 
		
		//repeat until convergence
		while (true) {
			centroids = updatedCentroids;			
			
			point = new int[numOfPoints];			
			for (int i = 0; i < numOfPoints; i++) {
				//2. find points which are the nearest to a centroid
				point[i] = nearest(dataset[i]);
			}		


			//3. update claster's centroid
			updatedCentroids = updateClusterCentroid();	
			
			//3. update claster's centroid
			private double[][] updateClusterCentroid() {
				sizeOfCluster = new int[numOfClusters];
				double[][] centroidsAttrSum = new double[numOfClusters][]; //****
				centroidsAttrAverage = new double[numOfClusters][];
				meanOfAverage = new double[numOfClusters];
				double[][] newCentroidsAttr = new double[numOfClusters][];

				for (int i = 0; i < numOfClusters; i++) {
					centroidsAttrSum[i] = new double[numOfDimension];
					centroidsAttrAverage[i] = new double[numOfDimension];
					newCentroidsAttr[i] = new double[numOfDimension];
					for (int j = 0; j < numOfDimension; j++) {
						centroidsAttrSum[i][j] = 0; 	// set to 0
						centroidsAttrAverage[i][j] = 0; // set to 0
						newCentroidsAttr[i][j] = 0; 	// set to 0
					}				
					sizeOfCluster[i] = 0; // set to 0
					meanOfAverage[i] = 0; // set to 0
				}
			
			}

			
		}
					
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
	
	
	
	// 2. find points which are the nearest to a centroid
	private int nearest(double[] datasetRecords) {
		//calculate the distance to centroid 0.
		double minDistance = distance(datasetRecords, centroids[0]);
		int pointIndex = 0;
		
		for (int i = 1; i < numOfClusters; i++) {
			//calculate the distance to centroid i.
			double minDistance2 = distance(datasetRecords, centroids[i]);
			//find minimal distances
			if (minDistance2 < minDistance) {
				minDistance = minDistance2;
				pointIndex = i;
			}
		}
		return pointIndex;
	}
	
	
	
/*********** Distances (Jaccard, Simple matching coefficient (SMC)) ***********/	
	//Jaccard
	private double distance(double[] vectors1, double[] vectors2) {
		double Jaccard = 0;
		double M01 = 0, M10 = 0, M11 = 0;
		for (int i = 0; i < numOfDimension; i++) {
			if((vectors1[i] == 0) && (vectors2[i] == 1)){
				M01++;
			}
			if((vectors1[i] == 1) && (vectors2[i] == 0)){
				M10++;
			}
			if((vectors1[i] == 1) && (vectors2[i] == 1)){
				M11++;
			}
	}
		Jaccard += (M01+M10)/(M01+M10+M11);
		return Jaccard;
	}
	
	
//	//Simple matching coefficient (SMC)
//	private double distance(double[] vectors1, double[] vectors2) {
//		double SMC = 0;
//		double M00 = 0, M01 = 0, M10 = 0, M11 = 0;
//		for (int i = 0; i < numOfDimension; i++) {
//			if(vectors1[i] == 0 && vectors2[i] == 0){
//				M00++;
//			}
//			if(vectors1[i] == 0 && vectors2[i] == 1){
//				M01++;
//			}
//			if(vectors1[i] == 1 && vectors2[i] == 0){
//				M10++;
//			}
//			if(vectors1[i] == 1 && vectors2[i] == 1){
//				M11++;
//			}
//	}
//		SMC += (M00+M11)/(M00+M01+M10+M11);
//		return SMC;
//	}	
/*********** END Distances ***********/		
	
	
	
		

}
