package FP_AD_Fall2017;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0 && args.length < 6) {															
			try {
				String F = args[0]; 					// F is a file name
				int K = Integer.parseInt(args[1]); 		// K is a number of clusters
				int I = Integer.parseInt(args[2]); 		// I is a max number of iterations in any run
				double T = Double.parseDouble(args[3]); // T is a convergence threshold
				int R = Integer.parseInt(args[4]); 		// R is a number of runs

				System.out.println("F=" + F + " K=" + K + " I=" + I + " T=" + T + " R=" + R);	
				
				//get data from the file
				manageFile_A objMF = new manageFile_A(F);			
//				objMF.printFile(objMF.getDataset());
				

				
				//******************************************************************
				/*********** Calinski-Harabasz Index (CH) ***********/
				//******************************************************************
				Calinski_Harabasz objCH = new Calinski_Harabasz(objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
								
				System.out.println("***** Calinski-Harabasz Index (CH) *****");
				int k_Min = 2;
				int k_Max = (int)Math.sqrt(objMF.getNumOfPoints() / 2);
				System.out.println("k_Max: " + k_Max);
				
				double[] sSW_R = new double[R]; 	//for run number R: sum of squared within-cluster scatter matrix (SSW) 
				double[] sSB_R = new double[R]; 	//for run number R: sum of squared between-cluster scatter matrix (SSB) 				
				
				double[] CH = new double[k_Max - 1]; //Calinski-Harabasz Index (CH)
				int countCH = 0;
				
				while(k_Min <= k_Max){
					int count = 0;
					System.out.println("\n===== Calinski-Harabasz Index (CH) for K = " + k_Min + " =====");
					int R_CH = R;
					for (int i=0; i<R; i++){
						sSW_R[i] = 0;
						sSB_R[i] = 0;
					}
					
					while (R_CH > 0) {
						System.out.println("\nRun " + (count + 1) + "\n============================");
						objCH.kMeanClusteringForCH(k_Min, I, T, null); //cluster, iteration, threshold, centroid
						sSW_R[count] = objCH.getSSW();
						sSB_R[count] = objCH.getSSB();						
						R_CH--;
						count++;
					}
					int smallestSSW = objCH.findSmallestSSW(sSW_R); //find smallest index of SSW
					double sSW_Smallest = sSW_R[smallestSSW];
					double sSB_Highest = sSB_R[smallestSSW];
					CH[countCH] = objCH.kCalinski_Harabasz_Index(sSW_Smallest, sSB_Highest);
					System.out.println("For K = " + k_Min + ", CH = " + CH[countCH]);
					System.out.println("\n");
					k_Min++; countCH++;
				}
				//find the largest CH index, which will represent the best number of clusters
				int k_CH = objCH.findLargestCH(CH);
				System.out.println("The best number of clusters = " + k_CH);
//				System.out.println("The largest CH value = " + largestCH);
				
				
				/*********** END Calinski-Harabasz Index (CH) ***********/	
				
				
				
				//******************************************************************
				/*********** Kmeans ***********/
				//******************************************************************
				kmeans objKmeans = new kmeans(objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
				
				int count_K = 0;
				System.out.println("\n***** Kmeans *****");
				while (R > 0) {
					System.out.println("\nRun " + (count_K + 1) + "\n============================");
					objKmeans.kMeanClustering(k_CH, I, T, null); //cluster, iteration, threshold, centroid
					R--;
					count_K++;
				}
				/*********** END Kmeans ***********/
				
				
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

}
