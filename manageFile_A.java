package FP_AD_Fall2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manageFile_A {
	public String fileNameMF;
	private int numOfPointsMF;
	private int numOfPointsTestMF;
	private int numOfDimensionMF;	
	private String[] wineNameMF;
	private String[] wineNameTestMF;
	private double[][] datasetMF;
	private double[][] datasetTestMF;
	private int[] vintageMF;
	private int[] vintageTestMF;
	private double[] trueGradeMF;
	private double[] trueGradeTestMF;
	private int foldNumberMF;
	private int numOfFolds;
			
	
				
	public int getNumOfPoints() {
		return numOfPointsMF;
	}
	public void setNumOfPoints(int numOfPoints) {
		this.numOfPointsMF = numOfPoints;
	}

	
	public int getNumOfDimension() {
		return numOfDimensionMF;
	}
	public void setNumOfDimension(int numOfDimension) {
		this.numOfDimensionMF = numOfDimension;
	}

	
	public String[] getWineName() { 
		return wineNameMF;
	}
	public void setWineName(String[] wineName) { 
		this.wineNameMF = wineName;
	}

	
	public int[] getVintage() { 
		return vintageMF;
	}
	public void setVintage(int[] vintage) { 
		this.vintageMF = vintage;
	}

	
	public double[] getTrueGrade() { 
		return trueGradeMF;
	}
	public void setTrueGrade(double[] trueGrade) { 
		this.trueGradeMF = trueGrade;
	}

	
	public double[][] getDataset() {
		return datasetMF;
	}
	public void setDataset(double[][] dataset) {
		this.datasetMF = dataset;
	}

	
	public int getNumOfPointsTest() {
		return numOfPointsTestMF;
	}
	public void setNumOfPointsTest(int numOfPointsTest) {
		this.numOfPointsTestMF = numOfPointsTest;
	}
	
	
	public String[] getWineNameTest() {
		return wineNameTestMF;
	}
	public void setWineNameTest(String[] wineNameTest) {
		this.wineNameTestMF = wineNameTest;
	}
	
	
	public double[][] getDatasetTest() {
		return datasetTestMF;
	}
	public void setDatasetTest(double[][] datasetTest) {
		this.datasetTestMF = datasetTest;
	}
	
	
	public int[] getVintageTest() {
		return vintageTestMF;
	}
	public void setVintageTest(int[] vintageTest) {
		this.vintageTestMF = vintageTest;
	}
	
	
	public double[] getTrueGradeTest() {
		return trueGradeTestMF;
	}
	public void setTrueGradeTest(double[] trueGradeTest) {
		this.trueGradeTestMF = trueGradeTest;
	}
	
	
	
public manageFile_A(String fileName2, int nfolds2, int numOfFolds2) {
	fileNameMF = fileName2;
	foldNumberMF = nfolds2; 
	numOfFolds = numOfFolds2;
	int instanceIndex_A = 0;
	BufferedReader br;
	ArrayList<String> fileData_L_temp;

		// read data from a file
		try {
			//get a number of rows and columns
			br = new BufferedReader(new FileReader(fileNameMF));
            String line = "";
            int numOfColumns=0, numOfRows=0, numOfRows_test = 0;
            
            while ((line = br.readLine()) != null) {
            	String[] line_temp = line.split(",");            	
            	numOfColumns = line_temp.length;
            	if(instanceIndex_A % numOfFolds == foldNumberMF){
            		numOfRows_test++;
            	}
            	else{
            		numOfRows++;
            	}
            	instanceIndex_A++;
			}
                        
            System.out.println("numOfRows: " + numOfRows);
            System.out.println("numOfRows_test: " + numOfRows_test);
            System.out.println("numOfColumns: " + numOfColumns);            
            br.close();            
            
            
         // read whole file and store names in the wineName, data attributes in the dataset, years in vintage, labels in the trueGrade
         			br = new BufferedReader(new FileReader(fileNameMF));      
         			fileData_L_temp =  new ArrayList<String>();         			
         			
         			wineNameMF = new String[numOfRows]; 
         			wineNameTestMF = new String[numOfRows_test]; 
       			
         			datasetMF = new double[numOfRows][];
         			for (int i = 0; i < (numOfRows); i++) {
         				datasetMF[i] = new double[numOfColumns-3];				
        			}	
         			
         			datasetTestMF = new double[numOfRows_test][];
         			for (int i = 0; i < numOfRows_test; i++) {
         				datasetTestMF[i] = new double[numOfColumns-3];				
        			}	
         			
         			numOfPointsMF = numOfRows;
         			numOfPointsTestMF = numOfRows_test;
         			
         			numOfDimensionMF = numOfColumns-3;
         			
         			vintageMF = new int[numOfRows];
         			vintageTestMF = new int[numOfRows_test];
         			
         			trueGradeMF = new double[numOfRows];
         			trueGradeTestMF = new double[numOfRows_test];
	

         			
         			int instanceIndex_B = 0;  
         			int nrow = 0;
         			int nrowTest = 0;
         			line = "";
         			double[] line2;
                    while ((line = br.readLine()) != null) {
                    	String[] line_temp2 = line.split(","); 
                    	
                    	line2 = new double[line_temp2.length-3];
                    	Pattern p = Pattern.compile("([0-1]{1,1})|([0-9]{4,4})|([0-9]{2,2})");
                    	Matcher m = p.matcher(line);
                    	int i2 = 0;
                    	while(m.find() && (i2<=(numOfColumns-4))){ 
                    		line2[i2] = Double.parseDouble(m.group());                  
                    		i2++;                    		                    		
                    	}
                    	
                    	if(instanceIndex_B % numOfFolds == foldNumberMF){
                    		wineNameTestMF[nrowTest] = line_temp2[0];
                    		datasetTestMF[nrowTest] = line2;  
                        	vintageTestMF[nrowTest] = Integer.parseInt(line_temp2[numOfColumns-2]); 
                        	trueGradeTestMF[nrowTest] = Double.parseDouble(line_temp2[numOfColumns-1]); 
                    		nrowTest++; 
//                    		System.out.println(instanceIndex_B + " Test ");
                    	}
                    	else{
                    		wineNameMF[nrow] = line_temp2[0];
                    		datasetMF[nrow] = line2; 
                        	vintageMF[nrow] = Integer.parseInt(line_temp2[numOfColumns-2]); 
                        	trueGradeMF[nrow] = Double.parseDouble(line_temp2[numOfColumns-1]); 
                        	nrow++;
//                        	System.out.println(instanceIndex_B + " Training");
                    	} 	                     	                  	
                    	instanceIndex_B++;
                    }                    
//        			printFile(dataset); //print data from file                  
//                  printTestFile(datasetTest); //print testing data from file
                    br.close();
                    fileData_L_temp.clear();        
	
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file " + fileNameMF);
			System.exit(0);
			
		} catch (IOException e) {
			System.out.println("Can not read file " + fileNameMF);
			System.exit(0);
			
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

	}



	public void printTestFile(double[][] datasetTest_F){
		// print multi-dimensional array
		for (int i = 0; i < numOfPointsTestMF; i++) {
			for (int j = 0; j < numOfDimensionMF; j++) {
				System.out.print("["+i+"]["+j+"]" + datasetTest_F[i][j] + " ");
			}
			System.out.println();
		}
	}


	public void printFile(double[][] datasetF){
		// print multi-dimensional array
		for (int i = 0; i < numOfPointsMF; i++) {
			for (int j = 0; j < numOfDimensionMF; j++) {
				System.out.print("["+i+"]["+j+"]" + datasetF[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	
	
	public void writeDatasetToFile(double[][] outputDataset, String datasetName, int nfolds, int numOfPoints_output) { 
		File output_File = new File("output_dataset.txt"); 
		BufferedWriter bw = null; 
		try {			
			//if file does not exist, then create it
			if(!output_File.exists()){
				output_File.createNewFile();
			}
			
			//if true = append file
			bw = new BufferedWriter(new FileWriter(output_File.getAbsolutePath(),true)); 
			bw.write(datasetName + " - fold " + nfolds);
			bw.write(System.getProperty("line.separator"));
			for (int i = 0; i < numOfPoints_output; i++) {
				bw.write("Item #{"+i+"}");
				for (int j = 0; j < numOfDimensionMF; j++) {					
					bw.write("["+i+"]["+j+"]" + outputDataset[i][j] +" ");					
				}
				
//				if(originalGrade[i] >= 90 && convertedGrade_89and90[i] == 90){
//					
//				}
//				else if (originalGrade[i] < 90 && convertedGrade_89and90[i] == 89){
//					
//				}
//				else {
//					bw.write("ERROR: converting original grade to 89 and 90");
//				}
				
				bw.write(System.getProperty("line.separator"));
			}			
			
			} catch (FileNotFoundException ex){
				ex.printStackTrace(); }
		catch (IOException ex) { 
			ex.printStackTrace(); 
			} finally {
				//Close the BufferedWriter 
				try { 
					if (bw != null) {
						bw.flush();
						bw.close(); 
						} 
					} catch (IOException ex) {
						ex.printStackTrace(); 
						}
				}
		}


}
