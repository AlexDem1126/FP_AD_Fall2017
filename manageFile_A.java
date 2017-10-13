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
	private int numOfDimension;	
	private String[] wineName;
	private double[][] dataset;
	private int[] vintage;
	private double[] trueGrade; 
	
	
	
			
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

	
	

public manageFile_A(String fileName2) {
	fileName = fileName2;
	BufferedReader br;
	ArrayList<String> fileData_L_temp;

		// read data from a file
		try {
			//get a number of rows and columns
			br = new BufferedReader(new FileReader(fileName));
            String line = "";
            line = br.readLine();
            int numOfColumns=0, numOfRows=1;
            
            while (br.readLine() != null) {
            	String[] line_temp = line.split(",");            	
            	numOfColumns = line_temp.length;				
				numOfRows++;	
			}
                        
            System.out.println("numOfRows: " + numOfRows);
            System.out.println("numOfColumns: " + numOfColumns);            
            br.close();            
            
            
         // read whole file and store names in the wineName, data attributes in the dataset, years in vintage, labels in the trueGrade
         			br = new BufferedReader(new FileReader(fileName));      
         			fileData_L_temp =  new ArrayList<String>();         			
         			
         			wineName = new String[numOfRows]; 
       			
         			dataset = new double[numOfRows][];
         			for (int i = 0; i < numOfRows; i++) {
         				dataset[i] = new double[numOfColumns-3];				
        			}	
         			numOfPoints = numOfRows;
         			numOfDimension = numOfColumns-3;
         			
         			vintage = new int[numOfRows];
         			
         			trueGrade = new double[numOfRows];
	

         			
         			int nrow = 0;      			
         			line = "";
         			double[] line2;
//                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                    	String[] line_temp2 = line.split(",");                    	
                    	wineName[nrow] = line_temp2[0]; 
                    	
                    	line2 = new double[line_temp2.length-3];
                    	
                    	Pattern p = Pattern.compile("([0-1]{1,1})|([0-9]{4,4})|([0-9]{2,2})");
                    	Matcher m = p.matcher(line);
                    	int i2 = 0;
                    	while(m.find() && (i2<=(numOfColumns-4))){ 
                    		line2[i2] = Double.parseDouble(m.group());   
                    		i2++;
                    	}
                    	dataset[nrow] = line2;                     	

                    	vintage[nrow] = Integer.parseInt(line_temp2[6]); 
                    	trueGrade[nrow] = Double.parseDouble(line_temp2[7]);  
//                    	vintage[nrow][ncol] = Integer.parseInt(line_temp2[306]);
//                    	trueGrade[nrow][ncol] = Double.parseDouble(line_temp2[307]);                   	
                    nrow++;
                    }
        			//print data from file
//        			printFile(dataset);
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
