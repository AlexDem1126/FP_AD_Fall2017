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

				manageFile_A objMF = new manageFile_A(F);			
//				objMF.printFile(objMF.getDataset());
				
				kmeans objKmeans = new kmeans(objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
				
				
				
				//******************************************************************
				/*********** Kmeans ***********/
				//******************************************************************
				int count = 0;
				System.out.println("***** Kmeans *****");
				while (R > 0) {
					System.out.println("\nRun " + (count + 1) + "\n============================");
					objKmeans.dataClustering(K, I, null); //cluster, iteration, centroid
					R--;
					count++;
				}

				
				
			} catch (NumberFormatException e) {
				System.out.println(
						"Input format should be: <file name.txt> or <file name.csv>");
				System.exit(1);
			}
		} else {
			System.out.println(
					"Input format should be: <file name.txt> or <file name.csv>");
		}	

	}

}
