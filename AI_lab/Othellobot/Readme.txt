
How to run the code :
-------------------

	step 1:
	------
	
		1. The files have been saved as: alpha_beta_pruning_bot.cpp, minmax_bot.cpp, alpha_beta_pruning_bot.so, minmax_bot.so


	step 2:
	------
		To generate .so files,

	a. Change Lines 9, 10 in Makefile from MyBot to minmax_bot and bot.so to minmax_bot.so, to generate minmax_bot.so file.

	b. Change Lines 9, 10 in Makefile from MyBot to alpha_beta_pruning_bot and bot.so to alpha_beta_pruning_bot.so, to generate 		   alpha_beta_pruning_bot.so file.


	step 3:
	-------
		Command to play game amongst Minimax and Alpha Beta bots:

		$./bin/Desdemona ./<path to minmax_bot.so file> ./<path to alpha_beta_pruning_bot.so file>

	step 4:
	-------	
		To print the game board after every move use this command:

		$./bin/Desdemona -v ./<path to minmax_bot.so file> ./<path to alpha_beta_pruning_bot.so file>

Authors :
-------

-   [@chidaksh](https://github.com/chidaksh)
-   [@krupakar](https://github.com/Sai-Krupakar)
