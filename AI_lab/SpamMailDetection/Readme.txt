
Directory Content:
-----------------

	1. The main code is in the file named 10.py. 

	2. We have svm.ipynb file also which has the code in jupyter notebook format which contains the code from where we generated 		   plots and C vs Accuracy tables and best C value for maximum accuracy ,etc.
	
	3. It contains 10.pdf which contains the report containing the conclusions and results obtained from the model.
	
	4. Readme.txt file containing intructions to download and run the contents of the directory.


How to run the code :
-------------------


	step 1:
	------

		Download or copy the code into your local repository.

	step 2:
	------

		To run the code you need to give command line arguments, check the below
		example command for reference 

    			python3 <filename.py> spambase.names spambase.data <C_value> <kernel_type>

		Say we want to check for 'rbf' kernel with C = 2
		
			python3 10.py spambase.names spambase.data 2 rbf
	Caution :
	--------

		1.When you dont give proper command line arguments or when you give excess
		  number of command line arguments, it is designed to raise a value error.
		  So please make sure you give proper arguments.
		
		2.If you are using google colab you need to run linux commands by adding '!' at the start,
		
			!python3 10.py spambase.names spambase.data 2 rbf

Authors :
-------

-   [@chidaksh](https://github.com/chidaksh)
-   [@krupakar](https://github.com/Sai-Krupakar)
