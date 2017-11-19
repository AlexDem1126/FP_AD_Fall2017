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
	private double[][] centroidsAttrAverage;
	private double[] Attributes_Average;
	private int[] cSize; //clusters Size
	private double sse_final_K;

	
	public int[] getPoint() {
		return point;
	}
	public void setPoint(int[] point) {
		this.point = point;
	}
	
	
	public int[] getcSize() {
		return cSize;
	}
	public void setcSize(int[] cSize) {
		this.cSize = cSize;
	}
	
	
	
	public double getSse_final_K() {
		return sse_final_K;
	}
	public void setSse_final_K(double sse_final_K) {
		this.sse_final_K = sse_final_K;
	}
	
	
	
	public double[][] getCentroids() {
		return centroids;
	}
	public void setCentroids(double[][] centroids) {
		this.centroids = centroids;
	}
	
	
	
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
				
		// 1. generate pseudo random centroids
		centroids = generateCentroids(); 			//initial centroid
		
		double[][] updatedCentroids = centroids; 	//new initial centroid (updated)
		int iterationConverges = 0; 				//numbers of iteration before converging
		double newSSE = 0;							//sum of squared errors of prediction (SSE) 
		
		//repeat until convergence
		while (true) {
			centroids = updatedCentroids;			
			
			point = new int[numOfPoints];			
			for (int i = 0; i < numOfPoints; i++) {
				//2. find points which are the nearest to a centroid
				point[i] = nearest(dataset[i]);
			}	
			
			//display Points of Dataset
//			displayUpdatedPoints(point);
			
			//count number of points in clusters
			clustersSize(point);

			//3. update claster's centroid
			updatedCentroids = updateClusterCentroid();				
		
			//4. calculation of SSE improvement
			double SSE = newSSE;				
//			newSSE = findConverge(centroidsAttrAverage, threshold);
			newSSE = findConverge(centroids, updatedCentroids, threshold);
			iterationConverges++;	
				
				if(SSE==newSSE){
//					System.out.println("\n\nIteration " + iterationConverges + ": " + SSE);
					//display updatedCentroids - Final
//					displayUpdatedCentroidsAttr(updatedCentroids);
					
//					//display Points of Dataset - Final
//					displayUpdatedPoints(point);
					
					//count number of points in clusters - Final
//					clustersSize(point);
					
					//display clusters Size (number of points in clusters - Final)
					displayClustersSize();
					System.out.println("\nFinal SSE: " + SSE);
					sse_final_K = SSE;
					break;
				}else{
//					System.out.println("\n\nIteration " + iterationConverges + ": " + newSSE);
					//display updatedCentroids
//					displayUpdatedCentroidsAttr(updatedCentroids);	
					

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
//		System.out.println("Random Centroids: " + centrdData);
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
			double[][] centroidsAttrSum = new double[numOfClusters][]; //****
			centroidsAttrAverage = new double[numOfClusters][];
			double[][] newCentroidsAttr = new double[numOfClusters][];

			for (int i = 0; i < numOfClusters; i++) {
				centroidsAttrSum[i] = new double[numOfDimension];
				centroidsAttrAverage[i] = new double[numOfDimension];
				newCentroidsAttr[i] = new double[numOfDimension];
				for (int j = 0; j < numOfDimension; j++) {
					centroidsAttrSum[i][j] = 0;
					centroidsAttrAverage[i][j] = 0;
					newCentroidsAttr[i][j] = 0;
				}				
				sizeOfCluster[i] = 0; // set to 0
			}
			

			//assign points to a centroid and sum their attributes	
			for (int i = 0; i < numOfPoints; i++) {				
				int cluster = point[i];
				
				//add attributes to points and sum them
				for (int j = 0; j < numOfDimension; j++) {	
					centroidsAttrSum[cluster][j] = centroidsAttrSum[cluster][j] + dataset[i][j];
//					centroidsAttrAverage[cluster][j] = centroidsAttrAverage[cluster][j] + dataset[i][j];
				}
				sizeOfCluster[cluster]++;
			}

			
			// find average of the centroids' attributes 
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					centroidsAttrAverage[i][j] = centroidsAttrSum[i][j] / sizeOfCluster[i]; 
//					System.out.print(centroidsAttrAverage[i][j] +" ");
				}
//				System.out.println();
			}
			
			
			//find mean of the average of the centroids' attributes			
//			double[] newCentroidsAttrSum = new double[numOfDimension];
			double[] meanOfAverage = new double[numOfClusters];
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					meanOfAverage[i] = meanOfAverage[i] + centroidsAttrAverage[i][j];					
				}
				meanOfAverage[i] = meanOfAverage[i] / numOfDimension;
			}
				
			
			
			//find new attributes for centroids (Convert attributes values to 0 and 1)
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					if(centroidsAttrAverage[i][j] < meanOfAverage[i]){ //Attributes_Average[i])
						newCentroidsAttr[i][j] = 0; 
					}
					else {
						newCentroidsAttr[i][j] = 1; 
					}					
				}				
			}
			
			
			return newCentroidsAttr;
		}
		

		
		//4. find convergence
		private double findConverge(double[][] center_old, double[][] center_new, double threshold) {
		double sse = 0.0;
		double d = 0.0;
		for (int i = 0; i < numOfPoints; i++) {
			//assign points to a cluster				
			int cluster = point[i];
			d = distance ( dataset[i], center_new[cluster] );
			double dd = d*d;
			sse += dd; //SSW == SSE						
		}
		return sse;
	}
		
		
		
		//display updatedCentroids attributes
		private void displayUpdatedCentroidsAttr(double[][] updatedCentroidsF) {
			System.out.println("Updated Centroids:");
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					System.out.print(updatedCentroidsF[i][j] +" ");//							
				}
				System.out.println();
			}		
		}
		
		
		
		//display UpdatedPoints
		private void displayUpdatedPoints(int[] point2) {			
			System.out.print("Points: ");
			for (int i = 0; i < numOfPoints; i++) {
				System.out.print(point[i]+", ");
			}
			
		}
		
		
		
		//count number of points in clusters 
		private void clustersSize(int[] point2) {
			cSize = new int[numOfClusters];
			for (int i = 0; i < numOfPoints; i++) {
				int cSizeCount = -1;
				while(point[i] != cSizeCount){
					cSizeCount++;
				}
				cSize[cSizeCount]++;
			}
			
//			System.out.print("\nClusters Size: ");
//			for (int i = 0; i < numOfClusters; i++) {
//				System.out.print(cSize[i] +" ");
//			}
		}
		
		
		//display clusters Size (number of points in clusters - Final)
		private void displayClustersSize() {
			System.out.print("\nFinal Clusters Size: ");
			for (int i = 0; i < numOfClusters; i++) {
				System.out.print(cSize[i] +" ");
			}
			
		}


}
