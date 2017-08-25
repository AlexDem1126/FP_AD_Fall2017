package FP_AD_Fall2017;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0) {															
			try {
				String F = args[0];
				System.out.println("F=" + F); // F is a file name

				manageFile_A objMF = new manageFile_A(F);
			
//				objMF.printFile(objMF.getDataset());
				
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
