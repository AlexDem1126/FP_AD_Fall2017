# FP_AD_Fall2017

In this research, I am going to integrate three data mining methods: Calinski-Harabasz, K-means clustering, and Naive Bayes classification in order to predict a score/label of an item.

# Project plan
1. Implement internal validation on training dataset, in order to determine the best number of clusters. Use Calinski-Harabasz method. Run method R times (for example 100 times) and then choose the best number of clusters.
2. Do k-means clustering on the training dataset, with chosen number of clusters from step 1. Use Simple matching distance (SMD) or Jaccard distance to calculate distance.
3. Do Naive Bayes (NB) clustering on the training dataset. (for example multinomial NB with Boolean attributes)
4. Divide dataset into five folds - testing data.
5. Apply internal validation on testing data (fold 1). Run method R times and then choose the best number of clusters. 
6. Do k-means clustering on the testing data (fold 1), with chosen number of clusters from step 5.
7. Do NB clustering on the testing data (fold 1).
8. Get accuracy of prediction for fold 1.
9. Repeat steps (5,6,7,8) for each fold (fold 2, fold 3, fold 4, fold 5).


###### Data Set Description:
  binary dataset

