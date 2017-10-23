package FP_AD_Fall2017;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manageFile_A {
	public String fileName;
	private int numOfPoints;
	private int numOfPointsTest;
	private int numOfDimension;	
	private String[] wineName;
	private String[] wineNameTest;
	private double[][] dataset;
	private double[][] datasetTest;
	private int[] vintage;
	private int[] vintageTest;
	private double[] trueGrade;
	private double[] trueGradeTest;
	private int foldNumber;
			
	
				
	public int getNumOfPoints() {
		return numOfPoints;
	}
	public void setNumOfPoints(int numOfPoints) {
		this.numOfPoints = numOfPoints;
	}

	
	public int getNumOfDimension() {
		return numOfDimension;
	}
	public void setNumOfDimension(int numOfDimension) {
		this.numOfDimension = numOfDimension;
	}

	
	public String[] getWineName() { 
		return wineName;
	}
	public void setWineName(String[] wineName) { 
		this.wineName = wineName;
	}

	
	public int[] getVintage() { 
		return vintage;
	}
	public void setVintage(int[] vintage) { 
		this.vintage = vintage;
	}

	
	public double[] getTrueGrade() { 
		return trueGrade;
	}
	public void setTrueGrade(double[] trueGrade) { 
		this.trueGrade = trueGrade;
	}

	
	public double[][] getDataset() {
		return dataset;
	}
	public void setDataset(double[][] dataset) {
		this.dataset = dataset;
	}

	
	

public manageFile_A(String fileName2, int numOfFolds2) {
	fileName = fileName2;
	foldNumber = numOfFolds2; //foldNumber
	int instanceIndex_A = 0;
	BufferedReader br;
	ArrayList<String> fileData_L_temp;

		// read data from a file
		try {
			//get a number of rows and columns
			br = new BufferedReader(new FileReader(fileName));
            String line = "";
//            line = br.readLine();
            int numOfColumns=0, numOfRows=0, numOfRows_test = 0;
            
            while ((line = br.readLine()) != null) {//br.readLine() != null
            	String[] line_temp = line.split(",");            	
            	numOfColumns = line_temp.length;
            	if(instanceIndex_A % 10 == foldNumber){
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
         			br = new BufferedReader(new FileReader(fileName));      
         			fileData_L_temp =  new ArrayList<String>();         			
         			
         			wineName = new String[numOfRows]; 
         			wineNameTest = new String[numOfRows_test]; 
       			
         			dataset = new double[numOfRows][];
         			for (int i = 0; i < (numOfRows); i++) {
         				dataset[i] = new double[numOfColumns-3];				
        			}	
         			
         			datasetTest = new double[numOfRows_test][];
         			for (int i = 0; i < numOfRows_test; i++) {
         				datasetTest[i] = new double[numOfColumns-3];				
        			}	
         			
         			numOfPoints = numOfRows;
         			numOfPointsTest = numOfRows_test;
         			
         			numOfDimension = numOfColumns-3;
         			
         			vintage = new int[numOfRows];
         			vintageTest = new int[numOfRows_test];
         			
         			trueGrade = new double[numOfRows];
         			trueGradeTest = new double[numOfRows_test];
	

         			
         			int instanceIndex_B = 0;  
         			int nrow = 0;
         			int nrowTest = 0;
         			line = "";
         			double[] line2;
//                    line = br.readLine();
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
                    	
                    	if(instanceIndex_B % 10 == foldNumber){
                    		wineNameTest[nrowTest] = line_temp2[0];
                    		datasetTest[nrowTest] = line2;
                    		vintageTest[nrowTest] = Integer.parseInt(line_temp2[6]); 
                    		trueGradeTest[nrowTest] = Double.parseDouble(line_temp2[7]);  
//                        	vintageTest[nrowTest] = Integer.parseInt(line_temp2[306]);
//                        	trueGradeTest[nrowTest] = Double.parseDouble(line_temp2[307]);
                    		nrowTest++; 
                    	}
                    	else{
                    		wineName[nrow] = line_temp2[0];
                    		dataset[nrow] = line2; 
                    		vintage[nrow] = Integer.parseInt(line_temp2[6]); 
                        	trueGrade[nrow] = Double.parseDouble(line_temp2[7]);  
//                        	vintage[nrow] = Integer.parseInt(line_temp2[306]);
//                        	trueGrade[nrow] = Double.parseDouble(line_temp2[307]); 
                        	nrow++;
                    	} 	                     	                  	
                    	instanceIndex_B++;
                    }
        			
//        			printFile(dataset); //print data from file                  
//                  printTestFile(datasetTest); //print testing data from file
                    br.close();
                    fileData_L_temp.clear();        
	
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file " + fileName);
			System.exit(0);
			
		} catch (IOException e) {
			System.out.println("Can not read file " + fileName);
			System.exit(0);
			
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

	}



	public void printTestFile(double[][] datasetTest_F){
		// print multi-dimensional array
		for (int i = 0; i < numOfPointsTest; i++) {
			for (int j = 0; j < numOfDimension; j++) {
				System.out.print(datasetTest_F[i][j] + " ");
			}
			System.out.println();
		}
	}


	public void printFile(double[][] datasetF){
		// print multi-dimensional array
		for (int i = 0; i < numOfPoints; i++) {
			for (int j = 0; j < numOfDimension; j++) {
				System.out.print(datasetF[i][j] + " ");
			}
			System.out.println();
		}
	}


}
