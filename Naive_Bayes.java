package FP_AD_Fall2017;

import java.util.HashSet;

public class Naive_Bayes {
	
	private int[] point;
	private int[] clustersSize;
	private double[] trueGrade;//delete
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfTrueClasses_H = 2; //assign number of true classes in the testing dataset
	private double[] classType; //class90Plus or class90Minus
	private double[] ProbabilityOfClassH; 	//it depending on the number of true classes in the database
	private int[] zero90Plus;
	private int[] zero90Minus;
	private int[] one90Plus;
	private int[] one90Minus;
	private double[] prb_zero90Plus;
	private double[] prb_zero90Minus;
	private double[] prb_one90Plus;
	private double[] prb_one90Minus;
	private double[] largestTrainingPredicted;
	private double[] largest_TestPredicted;
	private double[] trueGradeTraining;
	private double[] trueGrade_Test_90and89;
	private double TP;	//TP is True Positive 
	private double FP;	//FP is False Positive 
	private double FN;	//FN is False Negative 
	private double TN;	//TN is True Negative 
	private double accuracyTraining;
	private double accuracy_Test;
	private int ls = 2; //Laplace smoothing number
	private int[] numOfPossibleValuesX;
	private double[][] centroids;
	private int cluster_num;
	private int[] pointTest;
	private int numOfClusters;
	
	

	
	public double[] getTrueGradeTraining() {
		return trueGradeTraining;
	}
	public void setTrueGradeTraining(double[] trueGradeTraining) {
		this.trueGradeTraining = trueGradeTraining;
	}



	public double[] getPrb_zero90Plus() {
		return prb_zero90Plus;
	}
	public void setPrb_zero90Plus(double[] prb_zero90Plus) {
		this.prb_zero90Plus = prb_zero90Plus;
	}
	public double[] getPrb_zero90Minus() {
		return prb_zero90Minus;
	}
	public void setPrb_zero90Minus(double[] prb_zero90Minus) {
		this.prb_zero90Minus = prb_zero90Minus;
	}
	public double[] getPrb_one90Plus() {
		return prb_one90Plus;
	}
	public void setPrb_one90Plus(double[] prb_one90Plus) {
		this.prb_one90Plus = prb_one90Plus;
	}
	public double[] getPrb_one90Minus() {
		return prb_one90Minus;
	}
	public void setPrb_one90Minus(double[] prb_one90Minus) {
		this.prb_one90Minus = prb_one90Minus;
	}
	
	
	
	//******************************************************************
	/*********** Training Naive Bayes Classifier 
	 * @param count_clusters_K 
	 * @param k 
	 * @param clusterSize_K 
	 * @param point_K ***********/
	//******************************************************************

	public Naive_Bayes( int count_clusters_K, int[] point_K, int[] clusterSize_K, double[] trueGradeMF, double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		cluster_num = count_clusters_K;
		point = point_K;
		clustersSize = clusterSize_K;
		
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;	
		

			trueGradeTraining = convertActualTrueGradeTo90and89(trueGradeMF, cluster_num);
			
			//1. P(H) - find probability of occurrence of class H
			ProbabilityOfClassH = findProbabilityOfClassH(cluster_num);
			
			//2. count instance X given class H
			countInstanceX_givenClassH(cluster_num);
			
			//3. # values Xi can take on (i.e. 2 for binary)
			findNumOfPossibleValuesX(cluster_num);// !!!
			
			//4. P(X|H) - find probability of generating instance X given class H	
			findProbabilityInstanceX_givenClassH();	
			
			//5. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
			findProbabilityInstanceX_beingInClassH(cluster_num);

			//6. count True Positive(TP), False Positive(FP), False Negative(FN), True Negative(TN)			
			countTP_FP_FN_TN(cluster_num);
			
			//7. find Accuracy
			accuracyTraining = findAccuracyTraining(cluster_num);						
	}

	

	//1. P(H) - find probability of occurrence of class H
	private double[] findProbabilityOfClassH(int i2){
		classType = new double[numOfTrueClasses_H];
		//count true classes in the testing dataset
		for (int i = 0; i < clustersSize[i2]; i++) {	
			int c1 = 0;
			int c2 = 1;
			if(trueGradeTraining[i] >= 90){
				classType[c1]++; //class90Plus;
			}
			else{
				classType[c2]++; //class90Minus;
			}
		}
		
		ProbabilityOfClassH = new double[numOfTrueClasses_H];
		for (int i = 0; i < numOfTrueClasses_H; i++) {	
			ProbabilityOfClassH[i] = (classType[i] + ls) / (clustersSize[i2] + (ls * numOfTrueClasses_H)); 
//			System.out.println("Probability Of ClassH "+i +": " + ProbabilityOfClassH[i]);
		}
		
		return ProbabilityOfClassH;
	}
	
	
	
	//2. count instance X given class H
	private void countInstanceX_givenClassH(int i2){		
		zero90Plus = new int[numOfDimension];
		zero90Minus = new int[numOfDimension];
		one90Plus = new int[numOfDimension];
		one90Minus = new int[numOfDimension];	
		int cs = 0;
		for (int i = 0; i < numOfPoints; i++) {
			if(point[i] == i2 && cs != clustersSize[i2]){
				for (int j = 0; j < numOfDimension; j++) {
					if((dataset[i][j] == 0) && (trueGradeTraining[cs] >= 90)){
						zero90Plus[j]++;
					}
					else if ((dataset[i][j] == 0) && (trueGradeTraining[cs] < 90)){
						zero90Minus[j]++;
					}
					else if ((dataset[i][j] == 1) && (trueGradeTraining[cs] >= 90)){
						one90Plus[j]++;
					}
					else if ((dataset[i][j] == 1) && (trueGradeTraining[cs] < 90)){
						one90Minus[j]++;
					}
					else {
						//System.out.println("ERROR_1: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
					}
				}
				cs++;
 			}			
		}
	}
	
	
	
	//3. # values Xi can take on (i.e. 2 for binary)
	private void findNumOfPossibleValuesX(int i2){
		numOfPossibleValuesX = new int[numOfDimension];		
		int j = 0;
		int cs = 0;
		while(j < numOfDimension){
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < numOfPoints; i++) {
				
					if (!set.contains((int) dataset[i][j])){
						if(point[i] == i2 && cs != clustersSize[i2]){
							set.add((int) dataset[i][j]);
							numOfPossibleValuesX[j] = set.size();
						}
						
			        }
					cs++;
								
			}
			j++;
		}
	}
	
	
	
	//4. P(X|H) - find probability of generating instance X given class H
	private void findProbabilityInstanceX_givenClassH(){		
		prb_zero90Plus = new double[numOfDimension];
		prb_zero90Minus = new double[numOfDimension];
		prb_one90Plus = new double[numOfDimension];
		prb_one90Minus = new double[numOfDimension];
		for (int i = 0; i < numOfDimension; i++) {
			prb_zero90Plus[i] = (zero90Plus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i]));
			prb_zero90Minus[i] = (zero90Minus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));
			prb_one90Plus[i] =  (one90Plus[i] + ls) / (classType[0] + (ls * numOfPossibleValuesX[i]));
			prb_one90Minus[i] =  (one90Minus[i] + ls) / (classType[1] + (ls * numOfPossibleValuesX[i]));
		}			
	}
	
	
	
	//5. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
	private void findProbabilityInstanceX_beingInClassH(int i2){
		double[] instanceX_beingInClass90Plus = new double[numOfDimension];
		double[] instanceX_beingInClass90Minus = new double[numOfDimension];
		largestTrainingPredicted = new double[clustersSize[i2]];
		int cs = 0;
		
		for (int i = 0; i < numOfPoints; i++) {			
			if(point[i] == i2 && cs != clustersSize[i2]){				
				double multiX_beingInClass90Plus = 0;
				double multiX_beingInClass90Minus = 0;
				double probabilityInstanceX_beingInClass90Plus = 0;
				double probabilityInstanceX_beingInClass90Minus = 0;
				double nornilezed90Plus = 0;
				double nornilezed90Minus = 0;
				for (int j = 0; j < numOfDimension; j++) {
					if(dataset[i][j] == 0){
						instanceX_beingInClass90Plus[j] = prb_zero90Plus[j];
						instanceX_beingInClass90Minus[j] = prb_zero90Minus[j];
					}				
					else if (dataset[i][j] == 1) {
						instanceX_beingInClass90Plus[j] = prb_one90Plus[j];
						instanceX_beingInClass90Minus[j] = prb_one90Minus[j];
					}
					else {
						System.out.println("ERROR_2: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
					}
				}
				multiX_beingInClass90Plus = findMultiX_beingInClass90Plus(instanceX_beingInClass90Plus);
				multiX_beingInClass90Minus = findMultiX_beingInClass90Minus(instanceX_beingInClass90Minus);		
				
				//5.1 P(H|X) = P(X|H)*P(H). ProbabilityInstanceX_beingInClassH()			
				probabilityInstanceX_beingInClass90Plus = multiX_beingInClass90Plus * ProbabilityOfClassH[0];
				probabilityInstanceX_beingInClass90Minus = multiX_beingInClass90Minus * ProbabilityOfClassH[1];
				
				//5.2 normalization of P(H|X)
				nornilezed90Plus = probabilityInstanceX_beingInClass90Plus/(probabilityInstanceX_beingInClass90Plus + probabilityInstanceX_beingInClass90Minus);
				nornilezed90Minus = probabilityInstanceX_beingInClass90Minus/(probabilityInstanceX_beingInClass90Plus + probabilityInstanceX_beingInClass90Minus);
				
				largestTrainingPredicted[cs] = findLargest(nornilezed90Plus, nornilezed90Minus);		
				cs++;
			}
		}		
	}



	private double findMultiX_beingInClass90Plus(double[] instanceX_beingInClass90Plus) {
		double temp_MultiX_InClass90Plus = 1;
		for (int i = 0; i < numOfDimension; i++) {
			temp_MultiX_InClass90Plus = temp_MultiX_InClass90Plus * instanceX_beingInClass90Plus[i];
		}
		return temp_MultiX_InClass90Plus;
	}

	
	
	private double findMultiX_beingInClass90Minus(double[] instanceX_beingInClass90Minus) {
		double temp_MultiX_InClass90Minus = 1;
		for (int i = 0; i < numOfDimension; i++) {
			temp_MultiX_InClass90Minus = temp_MultiX_InClass90Minus * instanceX_beingInClass90Minus[i];
		}
		return temp_MultiX_InClass90Minus;
	}
	
	
	
	private double findLargest(double multiX_beingInClass90Plus, double multiX_beingInClass90Minus) {
		double temp_largest = 0;
		if(multiX_beingInClass90Plus > multiX_beingInClass90Minus){
			temp_largest = 90;
		}
		else{
			temp_largest = 89;
		}
		return temp_largest;
	}
	
	
	
	private double[] convertActualTrueGradeTo90and89(double[] trueGradeMF, int i2) {
		double[] trueGrade90and89_F = new double[clustersSize[i2]];
		int cs = 0;
		for (int i = 0; i < numOfPoints; i++) {	
			if((point[i] == i2) && (trueGradeMF[i] >= 90)){
				trueGrade90and89_F[cs] = 90;
			}
			else if((point[i] == i2) && (trueGradeMF[i] < 90)){
				trueGrade90and89_F[cs] = 89;
			}
			else{
				//it is athoter cluster
			}
			
			if(cs != clustersSize[i2] && (point[i] == i2)){
				cs++;
			}
		}		
		return trueGrade90and89_F;
	}
	
	
	
	//6. count True Positive(TP), False Positive(FP), False Negative(FN), True Negative(TN)
	private void countTP_FP_FN_TN(int i2) {
		TP = 0;	//TP is True Positive 
		FP = 0;	//FP is False Positive 
		FN = 0;	//FN is False Negative 
		TN = 0;	//TN is True Negative
		int cs = 0;
		for (int i = 0; i < numOfPoints; i++) {
			if(point[i] == i2 && cs != clustersSize[i2]){
				if(trueGradeTraining[cs] == 90 && largestTrainingPredicted[cs]==90){
					TP++; //Prediction was positive +1, and in reality the value was +1
				}
				else if (trueGradeTraining[cs]==89 && largestTrainingPredicted[cs]==90) {
					FP++; //Prediction was positive +1, but in reality the value was -1
				}
				else if (trueGradeTraining[cs] ==90 && largestTrainingPredicted[cs]==89) {
					FN++; //Prediction was negative -1, but in reality the value was +1
				}
				else if (trueGradeTraining[cs] == 89 && largestTrainingPredicted[cs]==89){
					TN++; //Prediction was negative -1, but in reality the value was -1
				}
				else{
					System.out.println("ERROR_4: TP, FP,FN, TN.");
				}
				cs++;
				
			}			
		}
		System.out.println("Cluster"+i2+", TRAINING: TP= "+TP + ", FP= "+FP + ", FN= "+FN + ", TN= " + TN);
	}
	
	
	
	//7. find Accuracy
	private double findAccuracyTraining(int i2) {
		double AccuracyTraining_F = 0;
		AccuracyTraining_F = (TP + TN)/(TP + TN + FP + FN);	
		System.out.println("Cluster"+i2+", TRAINING Accuracy: " + AccuracyTraining_F*100 +"%");
		return AccuracyTraining_F;
	}


	
	//******************************************************************
	/*********** Test Naive Bayes Classifier 
	 * @param k 
	 * @param prb_one90Minus_Tr_table_K 
	 * @param prb_one90Plus_Tr_table_K 
	 * @param prb_zero90Minus_Tr_table_K 
	 * @param prb_zero90Plus_Tr_table_K 
	 * @param nfolds 
	 * @param ds ***********/
	//******************************************************************		
	public void Naive_Bayes_Test(int k, double[][] centroids_F, double[][] datasetTest_F, double[] trueGradeTest_F, int numOfPointsTest_F, double[][] prb_zero90Plus_Tr_table_K, double[][] prb_zero90Minus_Tr_table_K, double[][] prb_one90Plus_Tr_table_K, double[][] prb_one90Minus_Tr_table_K) {
		centroids = centroids_F;
		numOfClusters = k;
		
		//8. find pointsTest which are the nearest to a centroid
		pointTest = new int[numOfPointsTest_F];			
		for (int i = 0; i < numOfPointsTest_F; i++) {			
			pointTest[i] = nearest(datasetTest_F[i]);
		}	
		
		//9. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
//		findProbabilityInstanceX_beingInClassH_Test();

		//10. count True Positive(TP), False Positive(FP), False Negative(FN), True Negative(TN)			
//		countTP_FP_FN_TN(i);
		
		//11. find Accuracy
//		accuracyTraining = findAccuracyTraining(i);
		
		double[] instanceX_beingInClass90Plus_Test = new double[numOfDimension];
		double[] instanceX_beingInClass90Minus_Test = new double[numOfDimension];
		largest_TestPredicted = new double[numOfPointsTest_F];				
		for (int i = 0; i < numOfPointsTest_F; i++) {
			int cluster = pointTest[i];
			
			double multiX_beingInClass90Plus_Test = 0;
			double multiX_beingInClass90Minus_Test = 0;
			for (int j = 0; j < numOfDimension; j++) {
				if(datasetTest_F[i][j] == 0){
					instanceX_beingInClass90Plus_Test[j] = prb_zero90Plus_Tr_table_K[cluster][j];
					instanceX_beingInClass90Minus_Test[j] = prb_zero90Minus_Tr_table_K[cluster][j];
				}				
				else if (datasetTest_F[i][j] == 1) {
					instanceX_beingInClass90Plus_Test[j] = prb_one90Plus_Tr_table_K[cluster][j];
					instanceX_beingInClass90Minus_Test[j] = prb_one90Minus_Tr_table_K[cluster][j];
				}
				else {
					System.out.println("ERROR_3: value of attribute ["+i+"]["+j+"] is not equal to 0 or 1.");
				}
			}
			multiX_beingInClass90Plus_Test = findMultiX_beingInClass90Plus(instanceX_beingInClass90Plus_Test);
			multiX_beingInClass90Minus_Test = findMultiX_beingInClass90Minus(instanceX_beingInClass90Minus_Test);		
			largest_TestPredicted[i] = findLargest(multiX_beingInClass90Plus_Test, multiX_beingInClass90Minus_Test);			
		}
		trueGrade_Test_90and89 = convertTrueGrade_Test_To90and89(numOfPointsTest_F, trueGradeTest_F);
		countTP_FP_FN_TN_Test(numOfPointsTest_F);
		accuracy_Test = findAccuracyTest();		
	}	
	
	
	//9. P(H|X) = P(X|H)*P(H). find probability of instance X being in class H
//	findProbabilityInstanceX_beingInClassH_Test(){
//		
//	}
	
	
	//8. find pointsTest which are the nearest to a centroid
	private int nearest(double[] datasetRecordsTest) {
		//calculate the distance to centroid 0.
		double minDistance = distance(datasetRecordsTest, centroids[0]);
		int pointIndex = 0;
			
		for (int i = 1; i < numOfClusters; i++) {
			//calculate the distance to centroid i.
			double minDistance2 = distance(datasetRecordsTest, centroids[i]);
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
		
		
	
	private double[] convertTrueGrade_Test_To90and89(int numOfPointsTest_F, double[] trueGradeTest_F) {
		double[] trueGrade90and89_F = new double[numOfPointsTest_F];
		for (int i = 0; i < numOfPointsTest_F; i++) {			
			if(trueGradeTest_F[i] >= 90){
				trueGrade90and89_F[i] = 90;
			}
			else{
				trueGrade90and89_F[i] = 89;
			}
		}		
		return trueGrade90and89_F;
	}
	
	
	
	private void countTP_FP_FN_TN_Test(int numOfPointsTest_F) {
		TP = 0;	//TP is True Positive 
		FP = 0;	//FP is False Positive 
		FN = 0;	//FN is False Negative 
		TN = 0;	//TN is True Negative
		for (int i = 0; i < numOfPointsTest_F; i++) {	
			if(trueGrade_Test_90and89[i] == 90 && largest_TestPredicted[i]==90){
				TP++; //Prediction was positive +1, and in reality the value was +1
			}
			else if (trueGrade_Test_90and89[i] ==89 && largest_TestPredicted[i]==90) {
				FP++; //Prediction was positive +1, but in reality the value was -1
			}
			else if (trueGrade_Test_90and89[i] ==90 && largest_TestPredicted[i] ==89) {
				FN++; //Prediction was negative -1, but in reality the value was +1
			}
			else if(trueGrade_Test_90and89[i] == 89 && largest_TestPredicted[i]==89){
				TN++; //Prediction was negative -1, but in reality the value was -1
			}
			else{
				System.out.println("ERROR_5: Testing data - TP, FP,FN, TN.");
			}
		}	
		System.out.println("TESTING: TP= "+TP + ", FP= "+FP + ", FN= "+FN + ", TN= " + TN);
	}
	
	
	
	private double findAccuracyTest() {
		double AccuracyTest_F = 0;
		AccuracyTest_F = (TP + TN)/(TP + TN + FP + FN);	
		System.out.println("TESTING Accuracy: " + AccuracyTest_F*100 +"%");
		System.out.println("STOP");
		return AccuracyTest_F;
	}



}
