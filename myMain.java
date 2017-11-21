package FP_AD_Fall2017;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0 && args.length < 6) {															
			try {
				
				String file = args[0]; 											// F is a file name
				int number_of_clusters = Integer.parseInt(args[1]); 			// K is a number of clusters
				int max_number_of_iterations = Integer.parseInt(args[2]); 		// I is a max number of iterations in any run
				double convergence_threshold = Double.parseDouble(args[3]); 	// T is a convergence threshold
				int number_of_runs = Integer.parseInt(args[4]); 				// R is a number of runs

				System.out.println("F=" + file + " K=" + number_of_clusters + " I=" + max_number_of_iterations + " T=" + convergence_threshold + " R=" + number_of_runs);	
				
				
				
				//N-fold cross validation
				int numOfFolds = 5;								
				

				double[][] prb_zero90Plus_Tr_table = new double[numOfFolds][]; //zero90Plus training table for whole dataset
				double[][] prb_zero90Minus_Tr_table = new double[numOfFolds][];// zero90Minus training table for whole dataset
				double[][] prb_one90Plus_Tr_table = new double[numOfFolds][]; //one90Plus training table for whole dataset
				double[][] prb_one90Minus_Tr_table = new double[numOfFolds][]; //one90Minus training table for whole dataset
				
				for (int i = 0; i < numOfFolds; i++) {					
					prb_zero90Plus_Tr_table[i] = new double[305]; //305- is NumOfDimension
					prb_zero90Minus_Tr_table[i] = new double[305];
					prb_one90Plus_Tr_table[i] = new double[305];
					prb_one90Minus_Tr_table[i] = new double[305];
				}
				
				
				double[][][] prb_zero90Plus_Tr_table_K = new double[numOfFolds][][]; //zero90Plus training table for cluster K
				double[][][] prb_zero90Minus_Tr_table_K = new double[numOfFolds][][];// zero90Minus training table for cluster K
				double[][][] prb_one90Plus_Tr_table_K = new double[numOfFolds][][]; //one90Plus training table for cluster K
				double[][][] prb_one90Minus_Tr_table_K = new double[numOfFolds][][]; //one90Minus training table for cluster K
				
				for (int i = 0; i < numOfFolds; i++) {					
					prb_zero90Plus_Tr_table_K[i] = new double[number_of_clusters][305]; //305- is NumOfDimension
					prb_zero90Minus_Tr_table_K[i] = new double[number_of_clusters][305];
					prb_one90Plus_Tr_table_K[i] = new double[number_of_clusters][305];
					prb_one90Minus_Tr_table_K[i] = new double[number_of_clusters][305];
				}

				
				
				
				
				for (int nfolds = 0; nfolds < numOfFolds; nfolds++) {					
					
					String F = file; 					// F is a file name
					int K = number_of_clusters; 		// K is a number of clusters
					int I = max_number_of_iterations; 	// I is a max number of iterations in any run
					double T = convergence_threshold; 	// T is a convergence threshold
					int R = number_of_runs; 			// R is a number of runs

//					System.out.println("F=" + F + " K=" + K + " I=" + I + " T=" + T + " R=" + R);	
					System.out.println("******************************************************************");
					System.out.println("##### "+ nfolds + " - fold cross validation #####");
					System.out.println("******************************************************************");
					
					//get data from the file.
//					manageFile_A objMF = new manageFile_A(F);
					manageFile_A objMF = new manageFile_A(F, nfolds);
					
//					String tr = "Training data set";
//					String ts = "Testing data set";					
//					objMF.writeDatasetToFile(objMF.getDataset(), tr);
//					objMF.writeDatasetToFile(objMF.getDatasetTest(), ts);
					

										
					
					//******************************************************************
					/*********** Calinski-Harabasz Index (CH) ***********/
					//******************************************************************
//					Calinski_Harabasz objCH = new Calinski_Harabasz(objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
//									
//					System.out.println("***** Calinski-Harabasz Index (CH) *****");
//					int k_Min = 2;
//					int k_Max = (int)Math.sqrt(objMF.getNumOfPoints() / 2);
//					System.out.println("k_Max: " + k_Max);
//					
//					double[] sSW_R = new double[R]; 	//for run number R: sum of squared within-cluster scatter matrix (SSW) 
//					double[] sSB_R = new double[R]; 	//for run number R: sum of squared between-cluster scatter matrix (SSB) 				
//					
//					double[] CH = new double[k_Max - 1]; //Calinski-Harabasz Index (CH)
//					int countCH = 0;
//					
//					while(k_Min <= k_Max){
//						int count = 0;
//						System.out.println("===== Calinski-Harabasz Index (CH) for K = " + k_Min + " =====");
//						int R_CH = R;
//						for (int i=0; i<R; i++){
//							sSW_R[i] = 0;
//							sSB_R[i] = 0;
//						}
//						
//						while (R_CH > 0) {
////							System.out.println("Run " + (count + 1) + "\n============================");
//							objCH.kMeanClusteringForCH(k_Min, I, T, null); //cluster, iteration, threshold, centroid
//							sSW_R[count] = objCH.getSSW();
//							sSB_R[count] = objCH.getSSB();						
//							R_CH--;
//							count++;
//						}
//						int smallestSSW = objCH.findSmallestSSW(sSW_R); //find smallest index of SSW
//						double sSW_Smallest = sSW_R[smallestSSW];
//						double sSB_Highest = sSB_R[smallestSSW];
//						CH[countCH] = objCH.kCalinski_Harabasz_Index(sSW_Smallest, sSB_Highest);
//						System.out.println("For K = " + k_Min + ", CH = " + CH[countCH]);
////						System.out.println("\n");
//						k_Min++; countCH++;
//					}
//					//find the largest CH index, which will represent the best number of clusters
//					int k_CH = objCH.findLargestCH(CH);
//					System.out.println("The best number of clusters = " + k_CH);
////					System.out.println("The largest CH value = " + largestCH);
					
					
					/*********** END Calinski-Harabasz Index (CH) ***********/	
					
					//******************************************************************
					/*********** Naive Bayes Classifier (NB) WITHOUT K-MEANS ***********/
					//******************************************************************
					System.out.println("\n***** Naive Bayes Classifier (NB) WITHOUT K-MEANS *****");
					Naive_Bayes_without_Kmeans objNB_wk = new Naive_Bayes_without_Kmeans(objMF.getTrueGrade(), objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
					objNB_wk.Naive_Bayes_Test(objMF.getDatasetTest(), objMF.getTrueGradeTest(), objMF.getNumOfPointsTest());
					
					prb_zero90Plus_Tr_table[nfolds] = objNB_wk.getPrb_zero90Plus();
					prb_zero90Minus_Tr_table[nfolds] = objNB_wk.getPrb_zero90Minus();
					prb_one90Plus_Tr_table[nfolds] = objNB_wk.getPrb_one90Plus();
					prb_one90Minus_Tr_table[nfolds] = objNB_wk.getPrb_one90Minus();
					
					/*********** END Naive Bayes Classifier (NB) WITHOUT K-MEANS***********/
					
					
					
					//******************************************************************
					/*********** Kmeans ***********/
					//******************************************************************
					kmeans objKmeans = new kmeans(objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
					
					int count_R = 0;
					int RK = R;
					double[] sse_K = new double[RK];
					int[][] point_K = new int[RK][];
					int[][] clusterSize_K = new int[RK][];
					double[][][] centroids_K = new double[RK][][];

					for (int i = 0; i < RK; i++) {
						sse_K[i] = 0;
						point_K[i] = new int[objMF.getNumOfDimension()];
						clusterSize_K[i] = new int[K];	
						centroids_K[i] = new double[K][objMF.getNumOfDimension()];
					}
					
					System.out.println("\n***** Kmeans *****");
					while (R > 0) {
						System.out.println("Run " + (count_R + 1) + "\n============================");
						objKmeans.kMeanClustering(K, I, T, null); //cluster, iteration, threshold, centroid
//						objKmeans.kMeanClustering(k_CH, I, T, null); //cluster, iteration, threshold, centroid
						
						sse_K[count_R] = objKmeans.getSse_final_K();
						point_K[count_R] = objKmeans.getPoint();
						clusterSize_K[count_R] = objKmeans.getcSize();	
						centroids_K[count_R] = objKmeans.getCentroids();	
													
								
						R--;
						count_R++;
					}				
					/*********** END Kmeans ***********/
					
					int sse_K_SMALL_index = findSmallest(sse_K);					
					
					
				
					
					//******************************************************************
					/*********** Naive Bayes Classifier (NB) ***********/
					//******************************************************************
					System.out.println("\n***** Naive Bayes Classifier (NB) *****");
					int numOfPoints =  objMF.getNumOfPoints();
					displayUpdatedPoints(point_K[sse_K_SMALL_index], numOfPoints);
					
					int[] clustersSize = clusterSize_K[sse_K_SMALL_index];
					displayClustersSize(clusterSize_K[sse_K_SMALL_index], K);
					
					Naive_Bayes objNB = null;
					int K_NB = K;
					int count_clusters = 0;
					while(K_NB > 0){
						objNB = new Naive_Bayes(count_clusters, point_K[sse_K_SMALL_index], clusterSize_K[sse_K_SMALL_index], objMF.getTrueGrade(), objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
						
						prb_zero90Plus_Tr_table_K[nfolds][count_clusters] = objNB.getPrb_zero90Plus();
						prb_zero90Minus_Tr_table_K[nfolds][count_clusters] = objNB.getPrb_zero90Minus();
						prb_one90Plus_Tr_table_K[nfolds][count_clusters] = objNB.getPrb_one90Plus();
						prb_one90Minus_Tr_table_K[nfolds][count_clusters] = objNB.getPrb_one90Minus();
						
						K_NB--;
						count_clusters++;						
					}

					
					objNB.Naive_Bayes_Test(K, centroids_K[sse_K_SMALL_index], objMF.getDatasetTest(), objMF.getTrueGradeTest(), objMF.getNumOfPointsTest(), prb_zero90Plus_Tr_table_K, prb_zero90Minus_Tr_table_K, prb_one90Plus_Tr_table_K, prb_one90Minus_Tr_table_K, nfolds);
					
					/*********** END Naive Bayes Classifier (NB) ***********/
					
//					String tr = "Training data set";
//					String ts = "Testing data set";					
//					objMF.writeDatasetToFile(objMF.getDataset(), tr, objMF.getNumOfPoints(), nfolds, objMF.getTrueGrade(), objNB.getTrueGradeTraining());
//					objMF.writeDatasetToFile(objMF.getDatasetTest(), ts, objMF.getNumOfPointsTest(), nfolds);
								
					
					
										
				}//End N-fold cross validation		

								
				
			} catch (NumberFormatException e) {
				System.out.println(
						"Input format should be: <file name.data> or <file name.txt> or <file name.csv>");
				System.exit(1);
			}
		} else {
			System.out.println(
					"Input format should be: <file name.data> or <file name.txt> or <file name.csv>");
		}	

	}


	//display clusters Size (number of points in clusters - Final)
	private static void displayClustersSize(int[] clusterSize2, int num_clusters) {
		System.out.print("\nFinal Clusters Size: ");
		for (int i = 0; i < num_clusters; i++) {
			System.out.print(clusterSize2[i] +" ");
		}
		System.out.println();
		
	}


	//display UpdatedPoints
	private static void displayUpdatedPoints(int[] point2, int numOfPoints2) {
		System.out.print("Points: ");
		for (int i = 0; i < numOfPoints2; i++) {
			System.out.print(point2[i]+", ");
		}		
	}
	


	//find smallest index of SSE
	private static int findSmallest(double[] sse_K_f) {
		double[] sse_K_f0 = sse_K_f;
		int indexSSE = 0;
		double smallest = sse_K_f0[0];
		for(int i = 0; i < sse_K_f0.length; i++){
			if(sse_K_f0[i] < smallest){
				smallest = sse_K_f0[i];
				indexSSE = i;
			}
		}
		return indexSSE;
	}


}
