import pandas as pd
from sklearn.model_selection import train_test_split

def stratified_group_split(df, group_column, stratify_column, train_size=0.6, val_size=0.2, test_size=0.2, random_state=None):
    """
    Splits a dataset into training, validation, and test sets based on groups, ensuring that no group
    (subject_id) is split across multiple sets and the split is stratified based on the category.

    Parameters:
        df (pd.DataFrame): The input dataset.
        group_column (str): The column representing the groups (e.g., subject_id).
        stratify_column (str): The column representing the categories for stratification.
        train_size (float): The proportion of the dataset to include in the training set.
        val_size (float): The proportion of the dataset to include in the validation set.
        test_size (float): The proportion of the dataset to include in the test set.
        random_state (int): A random seed for reproducibility.
    
    Returns:
        train_df (pd.DataFrame): Training set.
        val_df (pd.DataFrame): Validation set.
        test_df (pd.DataFrame): Test set.
    """

    # Ensure that train_size + val_size + test_size == 1
    assert train_size + val_size + test_size == 1, "Proportions must sum to 1"
    
    # Get unique subjects and their corresponding categories for stratification
    unique_subjects = df[[group_column, stratify_column]].drop_duplicates()

    # Split subjects into training and temp (val+test) with stratification
    train_subjects, temp_subjects = train_test_split(
        unique_subjects, 
        test_size=(val_size + test_size), 
        stratify=unique_subjects[stratify_column], 
        random_state=random_state
    )
    
    # Further split the temp set into validation and test sets
    val_subjects, test_subjects = train_test_split(
        temp_subjects, 
        test_size=test_size / (test_size + val_size),  # Proportion relative to temp set
        stratify=temp_subjects[stratify_column], 
        random_state=random_state
    )
    
    # Now, filter the original DataFrame based on the splits we made
    train_df = df[df[group_column].isin(train_subjects[group_column])]
    val_df = df[df[group_column].isin(val_subjects[group_column])]
    test_df = df[df[group_column].isin(test_subjects[group_column])]

    return train_df, val_df, test_df

# Example dataset
data = {
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...']
}
df = pd.DataFrame(data)

# Apply the stratified group split
train_df, val_df, test_df = stratified_group_split(df, group_column='subject_id', stratify_column='category', random_state=42)

# Display the resulting datasets
print("Training Set:")
print(train_df)
print("\nValidation Set:")
print(val_df)
print("\nTest Set:")
print(test_df)
