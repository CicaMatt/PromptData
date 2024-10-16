import pandas as pd
from sklearn.model_selection import StratifiedShuffleSplit

# Load your dataset (replace 'your_dataset.csv' with your actual file)
df = pd.read_csv('your_dataset.csv')

# Get unique subject IDs
subject_ids = df['subject_id'].unique()

# Stratified split of subject IDs to ensure category balance across sets
splitter = StratifiedShuffleSplit(n_splits=1, test_size=0.4, random_state=42) 
# 0.4 because we want 40% for validation and test combined

for train_subject_indices, other_subject_indices in splitter.split(subject_ids, df.loc[df['subject_id'].isin(subject_ids), 'category']):
    train_subject_ids = subject_ids[train_subject_indices]
    other_subject_ids = subject_ids[other_subject_indices]

# Further split the 'other' subjects into validation and test
splitter_val_test = StratifiedShuffleSplit(n_splits=1, test_size=0.5, random_state=42) 
# 0.5 to get equal validation and test sets

for val_subject_indices, test_subject_indices in splitter_val_test.split(other_subject_ids, df.loc[df['subject_id'].isin(other_subject_ids), 'category']):
    val_subject_ids = other_subject_ids[val_subject_indices]
    test_subject_ids = other_subject_ids[test_subject_indices]

# Create the final datasets
train_df = df[df['subject_id'].isin(train_subject_ids)]
val_df = df[df['subject_id'].isin(val_subject_ids)]
test_df = df[df['subject_id'].isin(test_subject_ids)]