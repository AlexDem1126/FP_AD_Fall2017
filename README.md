# FP_AD_Fall2017

In this research, I am going to integrate three data mining methods: Calinski-Harabasz, K-means clustering, and Naive Bayes classification in order to predict a score/label of an item.

# Project plan
1. Divide dataset into five/ten folds - training data and testing data.
2. Implement internal validation on training dataset, in order to determine the best number of clusters. Use Calinski-Harabasz method. Run method R times (for example 100 times) and then choose the best number of clusters.

3. __TRAINING:__
   * 3.1. Implement internal validation on training dataset, in order to determine the best number of clusters. Use Calinski-Harabasz method. Run method R times (for example 100 times) and then choose the best number of clusters.
   - 3.2. Do k-means clustering on the training dataset, with chosen number of clusters from step 3.1. Use Simple matching distance (SMD) or Jaccard distance to calculate distance. Run method R times (for example 100 times) and then choose the best clusters.
   - 3.3. Do Naive Bayes(NB) with Laplace smoothing for the whole dataset in the training fold #1.
   - 3.4. Do Naive Bayes(NB) with Laplace smoothing for each cluster obtained from step 3.2.

4. **TESTING**:
   - 4.1. Take testing item, and calculate the distance between it and centroids that were obtained in step 3.2.
   - 4.2. Choose centroid which has the smallest distance to the tested item.
   - 4.3. Check, if chosen centroid has highest training accuracy.
        - 4.3.1. If it does, then use probability P(X|H) from step 3.4 for further calculations.
        - 4.3.2. If it does not, then use probability P(X|H) from step 3.3 for further calculations.
   - 4.4. Get accuracy of prediction for fold 1.
   - 4.5. Repeat steps from 3.1 till 4.4 for each fold (fold 2, fold 3, fold 4, fold 5, ...fold N).


###### Data Set Description:
  binary dataset
  - Number of Instances: 1010
  - Number of Attributes: 305
  - Associated Tasks: Classification
  
  Instance #  | Attribute 1 | Attribute 2 | ... | Attribute 305 | Class
  ----------- ------------- ------------- ----- --------------- ------
  Instance 1 | 0           | 1           | ... | 1             | 90+
  Instance 2 | 0           | 1           | ... | 0             | 90+
  ...        | ...         | ...         | ... | ...           | ...
  Instance 3 | 0           | 0           | ... | 1             | 90-
  Instance 4 | 1           | 0           | ... | 0             | 90-
  
  
First Header  | Second Header | Second Header
------------- | ------------- | -------------
Content Cell  | Content Cell  | Content Cell
Content Cell  | Content Cell  | Content Cell


