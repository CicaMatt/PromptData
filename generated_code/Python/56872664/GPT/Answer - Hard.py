import pandas as pd
from sklearn.model_selection import StratifiedShuffleSplit

def split_dataset(df, train_size=0.6, val_size=0.2, test_size=0.2, random_state=42):
    """
    Split the dataset into train, validation, and test sets while maintaining:
    - Stratification on the 'category' column.
    - No subject_id contamination across datasets (all records from the same subject go into one dataset).
    
    :param df: Input dataframe with columns ['note_id', 'subject_id', 'category', 'note']
    :param train_size: Proportion of data to use for training.
    :param val_size: Proportion of data to use for validation.
    :param test_size: Proportion of data to use for testing.
    :param random_state: Random seed for reproducibility.
    :return: Three dataframes for train, validation, and test sets.
    """
    
    # Assert that sizes sum to 1
    assert abs(train_size + val_size + test_size - 1.0) < 1e-6, "The split proportions must sum to 1"
    
    # Step 1: Group data by 'subject_id' and 'category' to ensure stratified sampling by subject
    subjects = df[['subject_id', 'category']].drop_duplicates()

    # Step 2: Perform stratified split on subjects based on their category
    splitter = StratifiedShuffleSplit(n_splits=1, test_size=val_size + test_size, random_state=random_state)
    
    train_subjects_idx, val_test_subjects_idx = next(splitter.split(subjects, subjects['category']))
    
    # Split subjects into train and temp (val+test)
    train_subjects = subjects.iloc[train_subjects_idx]
    val_test_subjects = subjects.iloc[val_test_subjects_idx]
    
    # Step 3: Further split val_test_subjects into validation and test
    val_test_splitter = StratifiedShuffleSplit(n_splits=1, test_size=test_size / (test_size + val_size), random_state=random_state)
    val_subjects_idx, test_subjects_idx = next(val_test_splitter.split(val_test_subjects, val_test_subjects['category']))
    
    val_subjects = val_test_subjects.iloc[val_subjects_idx]
    test_subjects = val_test_subjects.iloc[test_subjects_idx]
    
    # Step 4: Filter original dataframe to create train, validation, and test sets based on subject_id
    train_df = df[df['subject_id'].isin(train_subjects['subject_id'])]
    val_df = df[df['subject_id'].isin(val_subjects['subject_id'])]
    test_df = df[df['subject_id'].isin(test_subjects['subject_id'])]
    
    # Return the split datasets
    return train_df, val_df, test_df

# Example usage:
data = {
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...'] * 6
}

# Create DataFrame
df = pd.DataFrame(data)

# Perform the split
train, val, test = split_dataset(df)

# Print results
print("Training Set:")
print(train)
print("\nValidation Set:")
print(val)
print("\nTest Set:")
print(test)
