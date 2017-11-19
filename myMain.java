package FP_AD_Fall2017;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0 && args.length < 6) {															
			try {
				
				//N-fold cross validation
				int numOfFolds = 5;				
				for (int nfolds = 0; nfolds < numOfFolds; nfolds++) {					
					
					String F = args[0]; 					// F is a file name
					int K = Integer.parseInt(args[1]); 		// K is a number of clusters
					int I = Integer.parseInt(args[2]); 		// I is a max number of iterations in any run
					double T = Double.parseDouble(args[3]); // T is a convergence threshold
					int R = Integer.parseInt(args[4]); 		// R is a number of runs

					System.out.println("F=" + F + " K=" + K + " I=" + I + " T=" + T + " R=" + R);	
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
						point_K[i] = new int[RK];
						point_K[i] = new int[RK];						
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
					Naive_Bayes objNB = new Naive_Bayes(K, point_K[sse_K_SMALL_index], clusterSize_K[sse_K_SMALL_index], objMF.getTrueGrade(), objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
					objNB.Naive_Bayes_Test(objMF.getDatasetTest(), objMF.getTrueGradeTest(), objMF.getNumOfPointsTest());
					
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
