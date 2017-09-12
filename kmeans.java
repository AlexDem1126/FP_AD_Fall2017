package FP_AD_Fall2017;

import java.util.ArrayList;

public class kmeans {

	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;
	private double threshold;
	private int[] point;
	private double[] Attributes_Average;

	
	
	public kmeans(double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
		Attributes_Average = findAttributes_Average();
	}
	
	

	//Find average of each attribute(Column) in dataset
	private double[] findAttributes_Average() {
		double[] Attributes_Sum = new double[numOfDimension];
		Attributes_Average = new double[numOfDimension];
		
		//count attributes in the columns
		for (int i = 0; i < numOfPoints; i++) {
			for (int j = 0; j < numOfDimension; j++) {
				Attributes_Sum[j] = Attributes_Sum[j] + dataset[i][j]; 
			}			
		}
//		//display Attributes_Sum
//		for (int j = 0; j < numOfDimension; j++) {
//			System.out.print(Attributes_Sum[j] +" ");
//		}
		
		//calculate the average of each attribute(Column) in dataset
		for (int j = 0; j < numOfDimension; j++) {
			Attributes_Average[j] = Attributes_Sum[j] / numOfDimension;
//			System.out.print(Attributes_Average[j] +" ");
		}
//		System.out.println();

		return Attributes_Average;
	}
	
	

	public void kMeanClustering(int nClusters, int nIterations, double iniThreshold, double[][] iniCentroids) {
		numOfClusters = nClusters;
		threshold = iniThreshold;
				
		// 1. call method to generate pseudo random centroids
		centroids = generateCentroids(); 			//initial centroid
		
		double[][] updatedCentroids = centroids; 	//new initial centroid (updated)
		int iterationConverges = 0; 				//numbers of iteration before converging
		double newSSE = 0;							//sum of squared errors of prediction (SSE) 
		
		//repeat until convergence
		while (true) {
			centroids = updatedCentroids;			
			
			point = new int[numOfPoints];			
			for (int i = 0; i < numOfPoints; i++) {
				//2. call method to find points which are the nearest to a centroid
				point[i] = nearest(dataset[i]);
			}			

			//3. call method to update claster's centroid
			updatedCentroids = updateClusterCentroid();				
		
			//4. calculation of SSE improvement
			double SSE = newSSE;				
			newSSE = findConverge(centroids, updatedCentroids, threshold);
			iterationConverges++;	
				
				if(SSE==newSSE){
					System.out.println("Iteration " + iterationConverges + ": " + SSE);
					System.out.println("Final SSE: " + SSE);
					break;
				}else{
					System.out.println("Iteration " + iterationConverges + ": " + newSSE);
					//display updatedCentroids
					displayUpdatedCentroids(updatedCentroids);					
				}			
				if ((newSSE < threshold) || ((nIterations > 0) && (iterationConverges >= nIterations))) {
					break;
				}
		}
		System.out.println("Converges at iteration " + iterationConverges + "\n");			
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
//			System.out.println(centrdData); //display centroid one at a time
			
			// copy centroid's attributes to the generated centroid
			centroids[i] = new double[numOfDimension];			
			for (int j = 0; j < numOfDimension; j++) {
				centroids[i][j] = dataset[randomCntrd][j];
//				System.out.print(centroids[i][j] +" "); //display cendtroids' data
			}
//			System.out.println();
		}
		System.out.println("Random Centroids: " + centrdData);
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
	
	
	
		//3. update claster's centroid
		private double[][] updateClusterCentroid() {
			int[] sizeOfCluster = new int[numOfClusters];
			double[][] newCentroids = new double[numOfClusters][];			

			for (int i = 0; i < numOfClusters; i++) {				
				newCentroids[i] = new double[numOfDimension];				
				for (int j = 0; j < numOfDimension; j++) {
					newCentroids[i][j] = 0;					
				}				
				sizeOfCluster[i] = 0; // set to 0
			}
			

			//assign points to a centroid and sum their attributes	
			for (int i = 0; i < numOfPoints; i++) {				
				int cluster = point[i];
				
				//add attributes to points
				for (int j = 0; j < numOfDimension; j++) {					
					newCentroids[cluster][j] = newCentroids[cluster][j] + dataset[i][j]; 
				}
				sizeOfCluster[cluster]++;
			}

			// find average of the centroids' attributes 
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					newCentroids[i][j] = newCentroids[i][j] / sizeOfCluster[i]; 
//					System.out.print(newCentroids[i][j] +" ");
				}
//				System.out.println();
			}
			
			
			
			//find new attributes for centroids (Convert attributes values to 0 and 1)
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					if(newCentroids[i][j] < Attributes_Average[i]){
						newCentroids[i][j] = 0; 
					}
					else {
						newCentroids[i][j] = 1; 
					}					
				}				
			}				
			return newCentroids;
		}
		

		
		//4. find convergence
		private double findConverge(double[][] center_old, double[][] center_new, double threshold) {
			double sse = 0.0;
			for (int i = 0; i < numOfPoints; i++) {
				//assign points to a cluster				
				int cluster = point[i];
				sse += distance ( dataset[i], center_new[cluster] );
			}
			return sse;
		}
		
		
		
		//display updatedCentroids
		private void displayUpdatedCentroids(double[][] updatedCentroidsF) {
			System.out.println("Updated Centroids:");
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					System.out.print(updatedCentroidsF[i][j] +" ");//							
				}
				System.out.println();
			}		
		}


}
