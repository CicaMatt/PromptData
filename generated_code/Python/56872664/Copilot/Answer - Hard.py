import pandas as pd
from sklearn.model_selection import train_test_split
from collections import defaultdict

# Sample dataset
data = {
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...']
}

df = pd.DataFrame(data)

# Function to split dataset
def stratified_group_split(df, group_col, stratify_col, train_size=0.6, val_size=0.2, test_size=0.2):
    # Ensure the split ratios sum to 1
    assert train_size + val_size + test_size == 1, "Train, validation and test sizes must sum to 1"
    
    # Get unique groups
    groups = df[group_col].unique()
    
    # Create a dictionary to hold the stratified groups
    stratified_groups = defaultdict(list)
    
    for group in groups:
        group_data = df[df[group_col] == group]
        stratified_groups[group_data[stratify_col].values[0]].append(group)
    
    # Split groups into train, validation, and test sets
    train_groups, temp_groups = train_test_split(list(stratified_groups.values()), train_size=train_size)
    val_groups, test_groups = train_test_split(temp_groups, test_size=test_size/(test_size + val_size))
    
    # Flatten the lists
    train_groups = [item for sublist in train_groups for item in sublist]
    val_groups = [item for sublist in val_groups for item in sublist]
    test_groups = [item for sublist in test_groups for item in sublist]
    
    # Split the dataframe based on the groups
    train_df = df[df[group_col].isin(train_groups)]
    val_df = df[df[group_col].isin(val_groups)]
    test_df = df[df[group_col].isin(test_groups)]
    
    return train_df, val_df, test_df

# Split the dataset
train_df, val_df, test_df = stratified_group_split(df, 'subject_id', 'category')

# Display the results
print("Training Set:")
print(train_df)
print("\nValidation Set:")
print(val_df)
print("\nTest Set:")
print(test_df)
