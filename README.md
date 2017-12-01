# FP_AD_Fall2017

In this research, I am going to integrate three data mining methods: Calinski-Harabasz, K-means clustering, and Naive Bayes classification in order to predict a score/label of an item.

# Project plan
1. Divide dataset into five/ten folds - training data and testing data.
2. TRAINING:
2.1. Implement internal validation on training dataset, in order to determine the best number of clusters. Use Calinski-Harabasz method. Run method R times (for example 100 times) and then choose the best number of clusters.
2.2. Do k-means clustering on the training dataset, with chosen number of clusters from step 2.1. Use Simple matching distance (SMD) or Jaccard distance to calculate distance.
2.3. Do Naive Bayes(NB) with Laplace smoothing for the whole dataset in the training fold #1.
2.4. Do Naive Bayes(NB) with Laplace smoothing for each cluster obtained from step 2.2.

3. TESTING:
3.1. Take testing item, and calculate the distance between it and centroids that were obtained in step 2.2.
3.2. Choose centroid which has the smallest distance to the tested item.
3.3. Check, if chosen centroid has highest training accuracy.
3.3.1. If it does, then use probability P(X|H) from step 2.3.
I will use 
8. Get accuracy of prediction for fold 1.
9. Repeat steps (5,6,7,8) for each fold (fold 2, fold 3, fold 4, fold 5).


###### Data Set Description:
  binary dataset

