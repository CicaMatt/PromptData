import pandas as pd
from sklearn.model_selection import StratifiedShuffleSplit

# Load your dataset into a Pandas dataframe with dtype specified
df = pd.read_csv("your_dataset.csv", dtype={"subject_id": str})

# Get the unique values of the subject_id column to ensure no observations from the same subject are present
subject_ids = df["subject_id"].unique()

# Create a stratified shuffle split with a seed for reproducibility and appropriate train/test sizes
train_val_test = StratifiedShuffleSplit(n_splits=1, train_size=0.6, test_size=0.2, random_state=42)

# Initialize an empty list to store the split indices
split_indices = []

# Iterate over each subject ID in the dataset and apply the stratified shuffle split
for subject_id in subject_ids:
    subject_df = df[df["subject_id"] == subject_id]
    
    # Split the data using StratifiedShuffleSplit
    for train_index, val_test_index in train_val_test.split(subject_df, subject_df["target"]):  
        split_indices.append((train_index, val_test_index))

# Create a new Pandas dataframe with the split data
split_df = pd.concat([df.iloc[idx[0]] for idx in split_indices])

# Split the data into training, validation, and test sets
train_df = split_df.copy()
val_test_df = pd.concat([df.iloc[idx[1]] for idx in split_indices])

