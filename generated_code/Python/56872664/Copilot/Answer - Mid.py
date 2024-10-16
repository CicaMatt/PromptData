import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.model_selection import StratifiedShuffleSplit

# Sample dataset
data = {
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...']
}

df = pd.DataFrame(data)

# Function to split dataset
def stratified_group_split(df, group_col, stratify_col, test_size=0.2, val_size=0.2):
    # Split into train+val and test
    sss = StratifiedShuffleSplit(n_splits=1, test_size=test_size, random_state=42)
    for train_val_idx, test_idx in sss.split(df, df[stratify_col]):
        train_val_df = df.iloc[train_val_idx]
        test_df = df.iloc[test_idx]

    # Split train+val into train and val
    stratify_col_train_val = train_val_df[stratify_col]
    sss = StratifiedShuffleSplit(n_splits=1, test_size=val_size/(1-test_size), random_state=42)
    for train_idx, val_idx in sss.split(train_val_df, stratify_col_train_val):
        train_df = train_val_df.iloc[train_idx]
        val_df = train_val_df.iloc[val_idx]

    return train_df, val_df, test_df

# Ensure all observations from each subject are in the same bucket
def group_split(df, group_col, stratify_col, test_size=0.2, val_size=0.2):
    unique_groups = df[group_col].unique()
    train_groups, test_groups = train_test_split(unique_groups, test_size=test_size, random_state=42)
    train_groups, val_groups = train_test_split(train_groups, test_size=val_size/(1-test_size), random_state=42)

    train_df = df[df[group_col].isin(train_groups)]
    val_df = df[df[group_col].isin(val_groups)]
    test_df = df[df[group_col].isin(test_groups)]

    return train_df, val_df, test_df

# Combine stratified and group split
def combined_split(df, group_col, stratify_col, test_size=0.2, val_size=0.2):
    train_df, val_df, test_df = group_split(df, group_col, stratify_col, test_size, val_size)
    train_df, val_df, test_df = stratified_group_split(df, group_col, stratify_col, test_size, val_size)
    return train_df, val_df, test_df

# Split the dataset
train_df, val_df, test_df = combined_split(df, 'subject_id', 'category', test_size=0.2, val_size=0.2)

print("Training Set:")
print(train_df)
print("\nValidation Set:")
print(val_df)
print("\nTest Set:")
print(test_df)
