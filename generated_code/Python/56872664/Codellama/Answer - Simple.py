from sklearn.model_selection import StratifiedShuffleSplit
from sklearn.utils import shuffle
import numpy as np

# Create a sample dataset
data = {'subject_id': [1, 2, 3, 4, 5], 'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'ECG'], 'note': ['blah', 'blah', 'blah', 'blah', 'blah']}
data = pd.DataFrame(data)

# Define the custom function to split the data into training, validation, and test sets
def stratified_split(data, target_column, split_ratio):
  # Split the data into training and test sets using StratifiedShuffleSplit()
  train_test_split = StratifiedShuffleSplit(n_splits=1, random_state=42)
  for train_index, test_index in train_test_split.split(data, data[target_column]):
    train_data = data.iloc[train_index]
    test_data = data.iloc[test_index]
    
    # Ensure that the training and test sets have the same number of observations for each subject
    unique_subjects = np.unique(train_data['subject_id'])
    for subject in unique_subjects:
      train_data_for_subject = train_data[train_data['subject_id'] == subject]
      test_data_for_subject = test_data[test_data['subject_id'] == subject]
      
      # Ensure that the same number of observations for each subject is present in both sets
      if len(train_data_for_subject) != len(test_data_for_subject):
        train_data = shuffle(train_data, random_state=42)
    
    # Return the split data
    return train_data, test_data

# Split the data into training, validation, and test sets using the custom function
train_data, val_data, test_data = stratified_split(data, 'category', [0.6, 0.2])